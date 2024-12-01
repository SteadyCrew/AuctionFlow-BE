package org.example.auctionflowbe.service;

import java.util.List;

import org.example.auctionflowbe.dto.NotificationResponse;
import org.example.auctionflowbe.entity.Notification;
import org.example.auctionflowbe.entity.User;
import org.example.auctionflowbe.repository.NotificationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
				notification.getItemTitle(),
				notification.getPrice(),
				notification.getIsRead()
			))
			.toList();
	}

	public NotificationResponse getNotification(User user, Long notificationId) {
		Notification notification = notificationRepository.findById(notificationId)
			.orElseThrow(() -> new IllegalArgumentException("해당 알림을 찾을 수 없습니다."));

		if(!notification.getUser().equals(user)){
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인의 알림만 접근 가능합니다.");
		}

		notification.setIsRead(true);
		notificationRepository.save(notification);

		return new NotificationResponse(
			notification.getNotificationId(),
			notification.getTitle(),
			notification.getItemTitle(),
			notification.getPrice(),
			notification.getIsRead()
		);
	}
}
