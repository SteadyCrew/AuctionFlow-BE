package org.example.auctionflowbe.service;

import java.util.List;

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

}
