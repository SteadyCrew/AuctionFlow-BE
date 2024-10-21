package org.example.auctionflowbe.service;

import org.example.auctionflowbe.dto.ItemCreateRequest;
import org.example.auctionflowbe.dto.ItemResponse;
import org.example.auctionflowbe.entity.Category;
import org.example.auctionflowbe.entity.Item;
import org.example.auctionflowbe.entity.User;
import org.example.auctionflowbe.repository.CategoryRepository;
import org.example.auctionflowbe.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    // 상품 등록 메서드
    public ItemResponse registerItem(User user, ItemCreateRequest itemCreateRequest) {
        Item item = new Item();
        // CategoryRepository를 통해 카테고리를 조회하고 Item에 설정
        Category category = categoryRepository.findById(itemCreateRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + itemCreateRequest.getCategoryId()));
        item.setCategory(category);
        item.setUser(user);
        item.setProductImageUrl(itemCreateRequest.getProductImageUrl());
        item.setTitle(itemCreateRequest.getTitle());
        item.setProductStatus(itemCreateRequest.getProductStatus());
        item.setDescription(itemCreateRequest.getDescription());
        item.setStartingBid(itemCreateRequest.getStartingBid());
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());
        item.setAuctionEndTime(itemCreateRequest.getAuctionEndTime());
        item.setItemBidStatus(itemCreateRequest.getItemBidStatus());
        Item savedItem = itemRepository.save(item);
        return mapToItemResponse(savedItem);
    }
    // 상품 ID로 조회 메서드
    public ItemResponse getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));
        return mapToItemResponse(item);
    }
    // 엔티티를 DTO로 변환하는 메서드
    private ItemResponse mapToItemResponse(Item item) {
        ItemResponse response = new ItemResponse();
        response.setItemId(item.getItemId());
        // Category ID를 응답에 포함
        response.setCategoryId(item.getCategory().getCategoryId());
        response.setUserId(item.getUser().getUserId());
        response.setProductImageUrl(item.getProductImageUrl());
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