package org.example.auctionflowbe.controller;

import org.example.auctionflowbe.dto.StoreDTO;
import org.example.auctionflowbe.entity.User;
import org.example.auctionflowbe.service.StoreService;
import org.example.auctionflowbe.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/mypage/store")
@RequiredArgsConstructor
public class MyPageController {

    private final StoreService storeService;
    private final UserService userService;

    @PostMapping
    public StoreDTO createStore(
        @AuthenticationPrincipal OAuth2User oauth2User,
        @RequestBody StoreDTO storeDTO) {

        User user = userService.findTestUserById(); // 임시 인가

        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return storeService.createStore(user, storeDTO);
    }
    @PatchMapping("/{storeId}")
    public StoreDTO updateStore(@PathVariable Long storeId, @RequestBody StoreDTO storeDTO) {
        return storeService.updateStore(storeId, storeDTO);
    }
    @GetMapping("/{storeId}")
    public StoreDTO getStore(@PathVariable Long storeId) {
        return storeService.getStore(storeId);
    }

    @DeleteMapping("/{storeId}")
    public void deleteStore(@PathVariable Long storeId) {
        storeService.deleteStore(storeId);
    }
}
