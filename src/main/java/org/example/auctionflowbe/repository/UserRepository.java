package org.example.auctionflowbe.repository;

import org.example.auctionflowbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);

}