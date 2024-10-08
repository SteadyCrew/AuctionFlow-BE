package org.example.auctionflowbe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String email;

    private Long kakaoId;
    private String nickname;
    private String profileImageUrl;

    @Lob
    private String accessToken;
    @Lob
    private String refreshToken;
    private LocalDateTime tokenExpiry;

    public User(String email, Long kakaoId, String nickname, String profileImageUrl, String accessToken, String refreshToken, LocalDateTime tokenExpiry) {
        this.email = email;
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenExpiry = tokenExpiry;
    }
}