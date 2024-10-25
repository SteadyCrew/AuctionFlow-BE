package org.example.auctionflowbe.repository;

import java.util.List;

import org.example.auctionflowbe.entity.Like;
import org.example.auctionflowbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByUser(User user);
}
