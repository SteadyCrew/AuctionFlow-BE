package org.example.auctionflowbe.repository;

import org.example.auctionflowbe.entity.Notification;
import org.example.auctionflowbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	@Query("SELECT COUNT(n) FROM Notification n WHERE n.user = :user AND n.isRead = false")
	int countUnreadNotificationsByUser(User user);
}
