package org.example.auctionflowbe.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.auctionflowbe.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/userInfo")
    public ResponseEntity<Map<String, Object>> getUserInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("email", user.getEmail());
                userInfo.put("kakaoId", user.getKakaoId());
                userInfo.put("nickname", user.getNickname());
                userInfo.put("profileImageUrl", user.getProfileImageUrl());
                return ResponseEntity.ok(userInfo);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not authenticated"));
    }
}
