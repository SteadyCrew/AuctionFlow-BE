package org.example.auctionflowbe.service;

import org.example.auctionflowbe.dto.ItemCreateRequest;
import org.example.auctionflowbe.dto.ItemResponse;
import org.example.auctionflowbe.entity.Category;
import org.example.auctionflowbe.entity.Item;
import org.example.auctionflowbe.entity.ItemImage;
import org.example.auctionflowbe.entity.User;
import org.example.auctionflowbe.repository.CategoryRepository;
import org.example.auctionflowbe.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private S3Service s3Service;

    // 상품 등록 메서드
    public ItemResponse registerItem(User user, ItemCreateRequest itemCreateRequest) {
        Item item = new Item();
        // 카테고리 설정
        Category category = categoryRepository.findById(itemCreateRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + itemCreateRequest.getCategoryId()));
        item.setCategory(category);
        // 사용자 설정
        item.setUser(user);

        item.setTitle(itemCreateRequest.getTitle());
        item.setProductStatus(itemCreateRequest.getProductStatus());
        item.setDescription(itemCreateRequest.getDescription());
        item.setStartingBid(itemCreateRequest.getStartingBid());
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());
        item.setAuctionEndTime(itemCreateRequest.getAuctionEndTime());
        item.setItemBidStatus(itemCreateRequest.getItemBidStatus());

        // 이미지 업로드 및 URL 설정
        List<String> imageUrls;
        try {
            imageUrls = s3Service.uploadFiles(itemCreateRequest.getProductImageFiles(), "item-images");
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload images", e);
        }
        // 이미지 엔티티 생성
        List<ItemImage> itemImages = itemCreateRequest.getProductImageFiles().stream()
                .map(file -> {
                    String imageUrl = imageUrls.get(itemCreateRequest.getProductImageFiles().indexOf(file));
                    ItemImage itemImage = new ItemImage();
                    itemImage.setImageUrl(imageUrl);
                    itemImage.setItem(item);
                    return itemImage;
                })
                .collect(Collectors.toList());
        item.setProductImages(itemImages);
        // 아이템 저장
        Item savedItem = itemRepository.save(item);
        return mapToItemResponse(savedItem);
    }
    // 상품 ID로 조회 메서드
    public ItemResponse getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));
        return mapToItemResponse(item);
    }
    // 모든 아이템 dto 을 조회하는 메서드 추가
    public List<ItemResponse> getAllItemResponses() {
        List<Item> items = itemRepository.findAll();
        return items.stream()
                .map(this::mapToItemResponse)
                .collect(Collectors.toList());
    }
    // 엔티티를 DTO로 변환하는 메서드
    private ItemResponse mapToItemResponse(Item item) {
        ItemResponse response = new ItemResponse();
        response.setItemId(item.getItemId());
        response.setCategoryId(item.getCategory().getCategoryId()); // 카테고리 이름 설정
        response.setUserId(item.getUser().getUserId()); // 사용자 이름 설정
        response.setProductImageUrls(item.getProductImages().stream()
                .map(ItemImage::getImageUrl)
                .collect(Collectors.toList()));
        response.setTitle(item.getTitle());
        response.setProductStatus(item.getProductStatus());
        response.setDescription(item.getDescription());
        response.setStartingBid(item.getStartingBid());
        response.setCreatedAt(item.getCreatedAt());
        response.setUpdatedAt(item.getUpdatedAt());
        response.setAuctionEndTime(item.getAuctionEndTime());
        response.setItemBidStatus(item.getItemBidStatus());
        return response;
    }
}