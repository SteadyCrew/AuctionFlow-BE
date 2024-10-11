package org.example.auctionflowbe.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "declarations")
public class Declaration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long declarationId;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String reason;
    private LocalDateTime createdAt;
    private String declarationStatus;
}
