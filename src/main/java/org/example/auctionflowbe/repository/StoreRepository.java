package org.example.auctionflowbe.repository;

import org.example.auctionflowbe.entity.Store;
import org.example.auctionflowbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Store findByUser(User user);
}
