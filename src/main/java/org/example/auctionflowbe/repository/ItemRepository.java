package org.example.auctionflowbe.repository;

import java.util.List;

import org.example.auctionflowbe.entity.Item;
import org.example.auctionflowbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findBySeller(User seller);
}

