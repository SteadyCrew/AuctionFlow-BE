package org.example.auctionflowbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "stores")
@Getter
@Setter
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    private String name;
    private String content;
    private int postcode;

    private String basicAddr;
    private String detailAddr;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
