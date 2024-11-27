package org.example.auctionflowbe.repository;

import org.example.auctionflowbe.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
