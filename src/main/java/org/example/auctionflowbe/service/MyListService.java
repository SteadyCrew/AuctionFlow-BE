package org.example.auctionflowbe.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.auctionflowbe.dto.ItemResponse;
import org.example.auctionflowbe.entity.Bid;
import org.example.auctionflowbe.entity.Item;
import org.example.auctionflowbe.entity.Like;
import org.example.auctionflowbe.entity.User;
import org.example.auctionflowbe.repository.BidRepository;
import org.example.auctionflowbe.repository.ItemRepository;
import org.example.auctionflowbe.repository.LikeRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyListService {

    private final ItemRepository itemRepository;
    private final BidRepository bidRepository;
    private final LikeRepository likeRepository;

    public List<Item> getMyItemList(User user) {
        List<Bid> bids = bidRepository.findByUser(user);
        return bids.stream()
            .map(Bid::getItem)
            .distinct()
            .toList();
    }

    public List<Item> getSellList(User user) {
        return itemRepository.findByUser(user);
    }

    public ItemResponse convertItemToItemResponse(Item item) {
        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setItemId(item.getItemId());
        itemResponse.setCategoryId(item.getCategory().getCategoryId());
        itemResponse.setUserId(item.getUser().getUserId());
        itemResponse.setProductImageUrls(item.getProductImages().stream()
            .map(itemImage -> itemImage.getImageUrl())
            .collect(Collectors.toList()));
        itemResponse.setTitle(item.getTitle());
        itemResponse.setProductStatus(item.getProductStatus());
        itemResponse.setDescription(item.getDescription());
        itemResponse.setStartingBid(item.getStartingBid());
        itemResponse.setCreatedAt(item.getCreatedAt());
        itemResponse.setUpdatedAt(item.getUpdatedAt());
        itemResponse.setAuctionEndTime(item.getAuctionEndTime());
        itemResponse.setItemBidStatus(item.getItemBidStatus());
        return itemResponse;
    }

    // 찜 목록 조회
    public List<ItemResponse> getLikeList(User user){
        List<Like> likes = likeRepository.findByUser(user);
        return likes.stream()
            .map(like -> convertItemToItemResponse(like.getItem()))
            .collect(Collectors.toList());
    }

}
