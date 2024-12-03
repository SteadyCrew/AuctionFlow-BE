package org.example.auctionflowbe.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ItemResponse {
    private Long itemId;
    private String categoryName;
    private Long sellerId;
    private List<String> productImageUrls;  // 여러 이미지 URL을 담는 리스트로 변경
    private String title;
    private String productStatus;
    private String description;
    private BigDecimal startingBid;
    private BigDecimal currentBid; // 현재 입찰 가격
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime auctionEndTime;
    private String itemBidStatus;
}