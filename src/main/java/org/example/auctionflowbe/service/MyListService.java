package org.example.auctionflowbe.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.auctionflowbe.dto.ItemResponse;
import org.example.auctionflowbe.entity.Bid;
import org.example.auctionflowbe.entity.Item;
import org.example.auctionflowbe.entity.User;
import org.example.auctionflowbe.repository.BidRepository;
import org.example.auctionflowbe.repository.ItemRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyListService {

    private final ItemRepository itemRepository;
    private final BidRepository bidRepository;

    public List<Item> getMyItemList(User user) {
        List<Bid> bids = bidRepository.findByUser(user);
        return bids.stream()
            .map(Bid::getItem)
            .distinct()
            .toList();
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

}
