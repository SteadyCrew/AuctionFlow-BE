package org.example.auctionflowbe.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.example.auctionflowbe.dto.ItemResponse;
import org.example.auctionflowbe.dto.LikeRequest;
import org.example.auctionflowbe.entity.Item;
import org.example.auctionflowbe.entity.Like;
import org.example.auctionflowbe.entity.User;
import org.example.auctionflowbe.repository.ItemRepository;
import org.example.auctionflowbe.service.MyListService;
import org.example.auctionflowbe.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private final ItemRepository itemRepository;


    @GetMapping("/mylist")
    public List<ItemResponse> myItemList(
        @AuthenticationPrincipal OAuth2User oAuth2User,
        @RequestParam(required = false) Integer statusType){

        // String email = oAuth2User.getAttribute("email");
        // User user = userService.findUserByEmail(email);

        User user = userService.findTestUserById(); // 임시 인가

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

        User user = userService.findTestUserById(); // 임시 인가

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
    public List<ItemResponse> myLikeList(
        @AuthenticationPrincipal OAuth2User oAuth2User){

        User user = userService.findTestUserById(); // 임시 인가

        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        return myListService.getLikeList(user);
    }

    // 상품 찜하기
    @PostMapping("/like")
    public void addLike(
        @AuthenticationPrincipal OAuth2User oAuth2User,
        @RequestBody LikeRequest likeRequest) {

        User user = userService.findTestUserById(); // 임시 인가

        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        Item item = itemRepository.findById(likeRequest.getItemId())
            .orElseThrow(() -> new RuntimeException("아이템을 찾을 수 없습니다."));

        Like like = new Like();
        like.setUser(user);
        like.setItem(item);
        like.setCreatedAt(LocalDateTime.now());

        myListService.saveLike(user, item);
    }

    // 상품 찜 삭제
    @DeleteMapping("/like")
    public String removeLike(
        @AuthenticationPrincipal OAuth2User oAuth2User,
        @RequestBody LikeRequest likeRequest) {

        User user = userService.findTestUserById(); // 임시 인가

        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        Item item = itemRepository.findById(likeRequest.getItemId())
            .orElseThrow(() -> new RuntimeException("아이템을 찾을 수 없습니다."));

        return myListService.removeLike(user, item);
    }

    // 상품 찜 랭킹순 조회


}
