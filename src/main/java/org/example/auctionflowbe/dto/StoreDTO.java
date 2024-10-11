package org.example.auctionflowbe.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class StoreDTO {
    private Long storeId;
    private String name;
    private String content;
    private int postcode;
    private String basicAddr;
    private String detailAddr;
    private Long userId;
}
