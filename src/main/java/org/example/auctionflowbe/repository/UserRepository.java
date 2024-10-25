package org.example.auctionflowbe.repository;

import org.example.auctionflowbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
    User findByUserId(Long userId);

}
