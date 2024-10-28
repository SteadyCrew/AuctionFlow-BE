package org.example.auctionflowbe.scheduler;

import org.example.auctionflowbe.entity.Bid;
import org.example.auctionflowbe.entity.Item;
import org.example.auctionflowbe.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class AuctionScheduler {

    @Autowired
    private ItemService itemService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Scheduled(cron = "0 * * * * ?")
    public void checkAuctionEndTimes() {
        List<Item> activeItems = itemService.getAllItems().stream()
                .filter(item -> "active".equals(item.getItemBidStatus()) &&
                        item.getAuctionEndTime() != null &&
                        item.getAuctionEndTime().isBefore(LocalDateTime.now()))
                .toList();

        for (Item item : activeItems) {
            Optional<Bid> highestBid = item.getBids().stream()
                    .max(Comparator.comparing(Bid::getBidAmount));

            item.setItemBidStatus("end");

            if (highestBid.isPresent()) {
                // 최고 입찰자를 구매자로 설정
                item.setBuyer(highestBid.get().getUser());
                itemService.saveItem(item);

                String message = String.format("경매가 종료되었습니다. 최고 입찰자는 사용자 ID: %d, 입찰 금액: %s원입니다.",
                        highestBid.get().getUser().getUserId(),
                        highestBid.get().getBidAmount().toString());
                messagingTemplate.convertAndSend("/topic/auction/" + item.getItemId(), message);
            } else {
                itemService.saveItem(item);  // 입찰자가 없는 경우에도 상태를 저장
                messagingTemplate.convertAndSend("/topic/auction/" + item.getItemId(), "경매가 종료되었습니다. 입찰자가 없습니다.");
            }
        }
    }
}
