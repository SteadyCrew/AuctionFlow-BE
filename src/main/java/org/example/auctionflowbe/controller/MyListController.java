package org.example.auctionflowbe.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.example.auctionflowbe.dto.ItemResponse;
import org.example.auctionflowbe.entity.Item;
import org.example.auctionflowbe.entity.User;
import org.example.auctionflowbe.service.MyListService;
import org.example.auctionflowbe.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyListController {

    private final MyListService myListService;
    private final UserService userService;


    @GetMapping("/mylist")
    public List<ItemResponse> myItemList(
        @AuthenticationPrincipal OAuth2User oAuth2User,
        @RequestParam(required = false) Integer statusType){

        // String email = oAuth2User.getAttribute("email");
        // User user = userService.findUserByEmail(email);

        User user = userService.findTestUserById();

        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        List<Item> items = myListService.getMyItemList(user);


        // statusType null : 전체, 1 : 진행 중(item_bid_status : active), 2: 경매 종료(item_bid_status : end)
        final String statusFilter;
        if (statusType == null) {
            statusFilter = null; // statusType이 null일 경우 전체를 반환
        } else if (statusType == 1) {
            statusFilter = "active";
        } else if (statusType == 2) {
            statusFilter = "end";
        } else {
            throw new IllegalArgumentException("잘못된 statusType 값입니다.");
        }

        // 상품 응답
        return items.stream()
            .filter(item -> statusFilter == null || item.getItemBidStatus().equals(statusFilter))
            .map(myListService::convertItemToItemResponse)
            .collect(Collectors.toList());

    }

    @GetMapping("/sell")
    public List<ItemResponse> mySellList(
        @AuthenticationPrincipal OAuth2User oAuth2User,
        @RequestParam(required = false) Integer statusType) {
        // String email = oAuth2User.getAttribute("email");
        // User user = userService.findUserByEmail(email);

        User user = userService.findTestUserById();
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        List<Item> items = myListService.getSellList(user);

        // statusType null : 전체, 1 : 진행 중(item_bid_status : active), 2: 경매 종료(item_bid_status : end)
        final String statusFilter;
        if (statusType == null) {
            statusFilter = null; // statusType이 null일 경우 전체를 반환
        } else if (statusType == 1) {
            statusFilter = "active";
        } else if (statusType == 2) {
            statusFilter = "end";
        } else {
            throw new IllegalArgumentException("잘못된 statusType 값입니다.");
        }

        return items.stream()
            .filter(item -> statusFilter == null || item.getItemBidStatus().equals(statusFilter))
            .map(myListService::convertItemToItemResponse)
            .collect(Collectors.toList());
    }

    // 찜 목록 조회
    @GetMapping("/like")
    public List<ItemResponse> myLikeList(@AuthenticationPrincipal OAuth2User oAuth2User){

        User user = userService.findTestUserById();

        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        return myListService.getLikeList(user);
    }


}
