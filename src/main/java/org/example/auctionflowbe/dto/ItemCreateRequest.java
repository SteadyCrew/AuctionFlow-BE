package org.example.auctionflowbe.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
public class ItemCreateRequest {
    private Long categoryId;
    private String productImageUrl;
    private String title;
    private String productStatus;
    private String description;
    private BigDecimal startingBid;
    private LocalDateTime auctionEndTime;
    private String itemBidStatus;
}