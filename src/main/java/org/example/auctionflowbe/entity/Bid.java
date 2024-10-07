package org.example.auctionflowbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bid", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"item_id", "bidAmount"})
})
@Getter
@Setter
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // 금액 제시자 => 구매자

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private BigDecimal bidAmount; // 최대 금액
    private LocalDateTime bidTime;

}
