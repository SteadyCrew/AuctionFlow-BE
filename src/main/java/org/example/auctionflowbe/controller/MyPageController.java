package org.example.auctionflowbe.controller;

import org.example.auctionflowbe.dto.StoreDTO;
import org.example.auctionflowbe.entity.User;
import org.example.auctionflowbe.service.MyStoreService;
import org.example.auctionflowbe.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/mypage/store")
@RequiredArgsConstructor
public class MyPageController {

    private final MyStoreService myStoreService;
    private final UserService userService;

    // 상점 생성 => 사용자 당 하나의 상점 생성 가능
    @PostMapping
    public StoreDTO createStore(
        @AuthenticationPrincipal OAuth2User oauth2User,
        @RequestBody StoreDTO storeDTO) {

        User user = userService.findTestUserById(); // 임시 인가

        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        return myStoreService.createStore(user, storeDTO);
    }

    // 사용자 상점 조회
    @GetMapping ("/storeInfo")
    public StoreDTO showUserStore(@AuthenticationPrincipal OAuth2User oAuth2User){
        User user = userService.findTestUserById(); // 임시 인가
        if(user == null){
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        return myStoreService.getUserStoreInfo(user);
    }

    @PatchMapping
    public StoreDTO updateStore(@AuthenticationPrincipal OAuth2User oauth2User,
        @RequestBody StoreDTO storeDTO) {
        User user = userService.findTestUserById(); // 임시 인가
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        return myStoreService.updateStore(user, storeDTO);
    }

    @DeleteMapping
    public String deleteStore(
        @AuthenticationPrincipal OAuth2User oauth2User) {
        User user = userService.findTestUserById(); // 임시 인가
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        myStoreService.deleteStore(user);
        return "상점이 성공적으로 삭제되었습니다.";
    }
}
