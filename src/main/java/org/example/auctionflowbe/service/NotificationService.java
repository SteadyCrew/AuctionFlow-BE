package org.example.auctionflowbe.service;

import java.util.List;

import org.example.auctionflowbe.dto.NotificationResponse;
import org.example.auctionflowbe.entity.Notification;
import org.example.auctionflowbe.entity.User;
import org.example.auctionflowbe.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;

	public void saveNotification(Notification notification) {
		notificationRepository.save(notification);
	}

	public int getUnreadNotificationCount(User user) {
		return notificationRepository.countUnreadNotificationsByUser(user);
	}

	public List<NotificationResponse> getUserNotifications(User user) {

		List<Notification> notifications = notificationRepository.findByUser(user);

		return notifications.stream()
			.map(notification -> new NotificationResponse(
				notification.getNotificationId(),
				notification.getTitle(),
				notification.getContent(),
				notification.getIsRead()
			))
			.toList();
	}
}
