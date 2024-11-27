package org.example.auctionflowbe.service;

import org.example.auctionflowbe.dto.BidDTO;
import org.example.auctionflowbe.dto.BidNotification;
import org.example.auctionflowbe.entity.Bid;
import org.example.auctionflowbe.entity.Item;
import org.example.auctionflowbe.entity.Notification;
import org.example.auctionflowbe.entity.User;
import org.example.auctionflowbe.repository.BidRepository;
import org.example.auctionflowbe.repository.ItemRepository;
import org.example.auctionflowbe.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class AuctionService {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @Transactional  // 트랜잭션을 시작하는 어노테이션
    public void placeBid(User user, Long itemId, BigDecimal bidAmount) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 아이템을 찾을 수 없습니다: " + itemId));

        if (!"active".equals(item.getItemBidStatus())) {
            throw new RuntimeException("경매가 진행 중이 아닙니다.");
        }
        // 기존 입찰자 중 최고 입찰액보다 높은 경우에만 입찰
        BigDecimal highestBid = item.getBids().stream()
                .map(Bid::getBidAmount)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        if (bidAmount.compareTo(highestBid) > 0&& bidAmount.compareTo(item.getStartingBid()) >= 0) {
            // 입찰 처리
            Bid bid = new Bid();
            bid.setUser(user);
            bid.setItem(item);
            bid.setBidAmount(bidAmount);
            bid.setBidTime(LocalDateTime.now());

            bidRepository.save(bid);
            // 웹소켓을 통해 실시간 입찰 정보 전송
            BidNotification bidNotification = new BidNotification();
            bidNotification.setUserId(user.getUserId());
            bidNotification.setBidAmount(bidAmount);
            messagingTemplate.convertAndSend("/topic/auction/" + itemId, bidNotification);
        }
        else if (bidAmount.compareTo(item.getStartingBid()) < 0) {
            throw new RuntimeException("입찰 금액이 시작 가격보다 낮습니다.");
        }else {
            throw new RuntimeException("입찰 금액이 현재 최고 입찰가보다 낮습니다.");
        }
    }

    public List<BidDTO> findBidsByItemId(Long itemId) {
        List<Bid> bids = bidRepository.findByItem_ItemId(itemId);
        return bids.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public String endAuction(Long itemId, User currentUser) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("아이템을 찾을 수 없습니다."));

        // 현재 로그인한 사용자가 아이템의 등록자인지 확인
        if (!currentUser.equals(item.getSeller())) {
            throw new RuntimeException("경매를 종료할 권한이 없습니다.");
        }

        if (!"active".equals(item.getItemBidStatus())) {
            throw new RuntimeException("경매가 진행 중이 아닙니다.");
        }

        // 최고 입찰자 가져오기
        Optional<Bid> highestBid = item.getBids().stream()
                .max(Comparator.comparing(Bid::getBidAmount));

        item.setItemBidStatus("end");
        item.setUpdatedAt(LocalDateTime.now());

        if (highestBid.isPresent()) {
            item.setBuyer(highestBid.get().getUser());
            itemRepository.save(item);  // Item 저장

            // 최고 입찰자 정보와 함께 경매 종료 메시지 전송
            String message = String.format("경매가 종료되었습니다. 최고 입찰자는 사용자 ID: %d, 입찰 금액: %s원입니다.",
                    highestBid.get().getUser().getUserId(),
                    highestBid.get().getBidAmount().toString());
            messagingTemplate.convertAndSend("/topic/auction/" + itemId, message);
            return message;
        } else {
            itemRepository.save(item);  // 입찰자가 없는 경우에도 상태 저장
            messagingTemplate.convertAndSend("/topic/auction/" + itemId, "경매가 종료되었습니다. 입찰자가 없습니다.");
            return "경매가 종료되었습니다. 입찰자가 없습니다.";
        }
    }

    //클라이언트에게 입찰 정보를 전송할 때 사용
    private BidDTO convertToDTO(Bid bid) {
        BidDTO dto = new BidDTO();
        dto.setBidId(bid.getBidId());
        dto.setUserId(bid.getUser().getUserId());
        dto.setUserNickname(bid.getUser().getNickname());
        dto.setItemId(bid.getItem().getItemId());
        dto.setTitle(bid.getItem().getTitle());  // 아이템의 제목 가져오기
        dto.setBidAmount(bid.getBidAmount());
        dto.setBidTime(bid.getBidTime());
        return dto;
    }
}
