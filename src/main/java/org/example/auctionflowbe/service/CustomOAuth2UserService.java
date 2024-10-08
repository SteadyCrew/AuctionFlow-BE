package org.example.auctionflowbe.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.auctionflowbe.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    @Autowired
    private UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oauth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oauth2User.getAttributes();

        // 카카오 사용자 정보에서 필요한 데이터 추출
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount == null) {
            throw new RuntimeException("Kakao account information is missing");
        }

        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        if (profile == null) {
            throw new RuntimeException("Kakao profile information is missing");
        }

        // Null 체크 및 기본값 설정
        Long kakaoId;
        String nickname;
        String email;
        String profileImageUrl;

        try {
            kakaoId = Long.parseLong(attributes.get("id").toString());
            nickname = profile.getOrDefault("nickname", "").toString();
            email = kakaoAccount.getOrDefault("email", "").toString();
            profileImageUrl = profile.getOrDefault("profile_image_url", "").toString();
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Kakao user information", e);
        }

        // Access Token과 Refresh Token 처리
        String accessToken = userRequest.getAccessToken().getTokenValue();
        String refreshToken = userRequest.getAdditionalParameters().getOrDefault("refresh_token", "").toString();

        // 사용자 객체 생성 및 저장
        User user = new User(email, kakaoId, nickname, profileImageUrl, accessToken, refreshToken, null);
        userService.saveOrUpdate(user);

        // 세션에 사용자 정보 저장 (HttpServletRequest 필요)
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession(true); // 새로운 세션 생성
        session.setAttribute("user", user);

        // 사용자 정보 로깅
        logger.info("Kakao User ID: {}", kakaoId);
        logger.info("Kakao Nickname: {}", nickname);
        logger.info("Kakao Email: {}", email);
        logger.info("Kakao Profile Image URL: {}", profileImageUrl);
        logger.info("Access Token: {}", accessToken);
        logger.info("Refresh Token: {}", refreshToken);

        // 새로운 OAuth2User 객체 반환
        return new DefaultOAuth2User(
                oauth2User.getAuthorities(),
                Map.of(
                        "kakaoId", kakaoId,
                        "nickname", nickname,
                        "email", email,
                        "profileImageUrl", profileImageUrl
                ),
                "email"
        );
    }
}
