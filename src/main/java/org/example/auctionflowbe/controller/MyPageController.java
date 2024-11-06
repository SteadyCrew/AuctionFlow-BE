package org.example.auctionflowbe.controller;

import org.example.auctionflowbe.dto.StoreDTO;
import org.example.auctionflowbe.entity.User;
import org.example.auctionflowbe.service.MyStoreService;
import org.example.auctionflowbe.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

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
            @RequestHeader("Authorization") String authorizationHeader,
        @RequestBody StoreDTO storeDTO) {

        User user = userService.authenticateUserByToken
                (authorizationHeader.replace("Bearer ", "")); // 토큰으로 사용자 인증


        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        return myStoreService.createStore(user, storeDTO);
    }

    // 사용자 상점 조회
    @GetMapping ("/storeInfo")
    public StoreDTO showUserStore(
            @RequestHeader("Authorization") String authorizationHeader){

        User user = userService.authenticateUserByToken
                (authorizationHeader.replace("Bearer ", "")); // 토큰으로 사용자 인증

        if(user == null){
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        return myStoreService.getUserStoreInfo(user);
    }

    @PatchMapping
    public StoreDTO updateStore(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody StoreDTO storeDTO) {
        User user = userService.authenticateUserByToken
                (authorizationHeader.replace("Bearer ", "")); // 토큰으로 사용자 인증

        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        return myStoreService.updateStore(user, storeDTO);
    }

    @DeleteMapping
    public String deleteStore(
            @RequestHeader("Authorization") String authorizationHeader) {
        User user = userService.authenticateUserByToken
                (authorizationHeader.replace("Bearer ", "")); // 토큰으로 사용자 인증

        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        myStoreService.deleteStore(user);
        return "상점이 성공적으로 삭제되었습니다.";
    }
}
