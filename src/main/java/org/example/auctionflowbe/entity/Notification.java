package org.example.auctionflowbe.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "notifications")
@Entity
@Getter
@Setter
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long notificationId;

	private String title;

	private String itemTitle;

	private BigDecimal price;

	private Boolean isRead;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
}
