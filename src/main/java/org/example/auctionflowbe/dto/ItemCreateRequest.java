package org.example.auctionflowbe.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ItemCreateRequest {
    private Long categoryId;
    private List<MultipartFile> productImageFiles;  // 이미지 파일 리스트

    private String title;
    private String productStatus;
    private String description;
    private BigDecimal startingBid;
    private LocalDateTime auctionEndTime;
    private String itemBidStatus;
}