package org.example.auctionflowbe.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "items")
@Getter
@Setter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonManagedReference
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // 판매자

    private String productImageUrl;
    private String title;
    private String productStatus; // 상품 상태 => 깨끗함 정도
    private String description;
    private BigDecimal startingBid;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime auctionEndTime;
    private String itemBidStatus; // 경매 진행 상태 =>  진행중(active) / 종료(end)

    @OneToMany(mappedBy = "item")
    @JsonBackReference
    private Set<Bid> bids;

    @OneToMany(mappedBy = "item")
    private Set<Declaration> declarations;

    @OneToMany(mappedBy = "item")
    private Set<Like> likes;
}
