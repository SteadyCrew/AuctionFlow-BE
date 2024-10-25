package org.example.auctionflowbe.repository;

import java.util.List;

import org.example.auctionflowbe.entity.Bid;
import org.example.auctionflowbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByUser(User user);
}
