package org.example.auctionflowbe.repository;

import java.util.List;
import java.util.Optional;

import org.example.auctionflowbe.entity.Item;
import org.example.auctionflowbe.entity.Like;
import org.example.auctionflowbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByUser(User user);

    Optional<Like> findByUserAndItem(User user, Item item);
}
