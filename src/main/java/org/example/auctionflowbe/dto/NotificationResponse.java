package org.example.auctionflowbe.dto;

import java.math.BigDecimal;

public record NotificationResponse(

	Long notificationId,
	String title,
	String itemTitle,
	BigDecimal price,
	Boolean isRead
) {
}
