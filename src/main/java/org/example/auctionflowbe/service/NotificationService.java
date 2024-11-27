package org.example.auctionflowbe.service;

import org.example.auctionflowbe.entity.Notification;
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
}
