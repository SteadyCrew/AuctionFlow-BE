package org.example.auctionflowbe.controller;

import org.example.auctionflowbe.dto.BidDTO;
import org.example.auctionflowbe.entity.User;
import org.example.auctionflowbe.service.AuctionService;
import org.example.auctionflowbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/auction")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;
    @Autowired
    private UserService userService;

    //특정 아이템에 대한 입찰 목록을 조회
    @GetMapping("/bids/{itemId}")
    public ResponseEntity<List<BidDTO>> getBidsByItemId(@PathVariable Long itemId) {
        List<BidDTO> bidDTOs = auctionService.findBidsByItemId(itemId);
        return ResponseEntity.ok(bidDTOs);
    }

    //사용자 인증 후 itemId와 bidAmount 를 받아서 입찰을 진행
    @PostMapping("/bid")
    public ResponseEntity<?> placeBid(
            @AuthenticationPrincipal OAuth2User oauth2User,
            @RequestParam Long itemId,
            @RequestParam BigDecimal bidAmount) {

        // String email = oauth2User.getAttribute("email");
        // User user = userService.findUserByEmail(email);

        User user = userService.findTestUserById(); // 임시 인가

        if (user == null) {
            return ResponseEntity.status(404).body("사용자를 찾을 수 없습니다.");
        }

        try {
            auctionService.placeBid(user, itemId, bidAmount);
            return ResponseEntity.ok("입찰이 성공적으로 완료되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //임의로 경매를 종료시키는 api
    @PostMapping("/end/{itemId}")
    public ResponseEntity<?> endAuction(
            @AuthenticationPrincipal OAuth2User oauth2User,
            @PathVariable Long itemId) {
        try {
            // String email = oauth2User.getAttribute("email");
            // User user = userService.findUserByEmail(email);
            User user = userService.findTestUserById(); // 임시 인가

            if (user == null) {
                return ResponseEntity.status(404).body("사용자를 찾을 수 없습니다.");
            }

            String message = auctionService.endAuction(itemId, user);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
