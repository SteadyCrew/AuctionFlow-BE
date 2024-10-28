package org.example.auctionflowbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bids", uniqueConstraints = {
        //item_id와 bidAmount 컬럼의 조합이 고유(Unique)하도록 제한/ 동시성 문제 해결
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
