package org.example.auctionflowbe.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BidNotification {
    private Long userId;
    private BigDecimal bidAmount;
}
