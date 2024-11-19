package org.example.auctionflowbe.controller;

import org.example.auctionflowbe.dto.UserLoginDto;
import org.example.auctionflowbe.dto.UserRegistrationDto;
import org.example.auctionflowbe.entity.User;
import org.example.auctionflowbe.security.JwtTokenProvider;
import org.example.auctionflowbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // 회원가입 요청 처리
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        if (userService.existsByEmail(registrationDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 이메일입니다.");
        }
        userService.registerUser(registrationDto); // 회원가입 처리
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }

    // 로그인 요청 처리
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto loginDto) {
        User user = userService.authenticateUser(loginDto.getEmail(), loginDto.getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 이메일 또는 비밀번호입니다.");
        }
        String token = jwtTokenProvider.generateToken(user); // JWT 토큰 발급
        String nickname = user.getNickname();
        return ResponseEntity.ok(Map.of("token", token, "nickname", nickname)); // JWT 토큰, 닉네임 반환
    }
}
