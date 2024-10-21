package org.example.auctionflowbe.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
public class ItemResponse {
    private Long itemId;
    private Long categoryId;
    private Long userId;
    private String productImageUrl;
    private String title;
    private String productStatus;
    private String description;
    private BigDecimal startingBid;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime auctionEndTime;
    private String itemBidStatus;
}