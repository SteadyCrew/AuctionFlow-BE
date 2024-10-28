package org.example.auctionflowbe.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class BidDTO {
    private Long bidId;
    private Long userId;
    private String userNickname;
    private Long itemId;
    private String title;
    private BigDecimal bidAmount;
    private LocalDateTime bidTime;
}