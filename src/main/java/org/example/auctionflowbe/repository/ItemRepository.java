package org.example.auctionflowbe.repository;

import java.util.List;

import org.example.auctionflowbe.entity.Item;
import org.example.auctionflowbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findBySeller(User seller);

    List<Item> findByTitleContainingOrDescriptionContaining(String keyword, String description);

    @Query("SELECT i FROM Item i LEFT JOIN i.likes l GROUP BY i ORDER BY COUNT(l) DESC")
    List<Item> findItemsOrderByLikeCount();
}

