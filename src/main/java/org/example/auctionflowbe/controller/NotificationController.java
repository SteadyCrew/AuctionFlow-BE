package org.example.auctionflowbe.controller;

import org.example.auctionflowbe.entity.User;
import org.example.auctionflowbe.service.NotificationService;
import org.example.auctionflowbe.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

	private final NotificationService notificationService;
	private final UserService userService;

	@GetMapping("/count")
	public int getUserUnreadCount(@RequestHeader("Authorization") String authorizationHeader) {
		User user = userService.authenticateUserByToken
			(authorizationHeader.replace("Bearer ", "")); // 토큰으로 사용자 인증

		int count = 0;
		count = notificationService.getUnreadNotificationCount(user);
		return count;
	}


}
