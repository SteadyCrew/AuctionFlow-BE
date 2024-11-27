package org.example.auctionflowbe.dto;

public record NotificationResponse(

	Long notificationId,
	String title,
	String content,
	Boolean isRead
) {
}
