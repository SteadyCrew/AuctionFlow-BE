package org.example.auctionflowbe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.auctionflowbe.dto.ItemCreateRequest;
import org.example.auctionflowbe.dto.ItemResponse;
import org.example.auctionflowbe.entity.User;
import org.example.auctionflowbe.service.ItemService;
import org.example.auctionflowbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ItemResponse> createItem(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestPart("item") String itemJson,
            @RequestPart("images") List<MultipartFile> imageFiles) throws IOException {

        User user = userService.authenticateUserByToken(authorizationHeader.replace("Bearer ", "")); // 토큰으로 사용자 인증

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ItemCreateRequest itemCreateRequest = objectMapper.readValue(itemJson, ItemCreateRequest.class);

        itemCreateRequest.setProductImageFiles(imageFiles);
        ItemResponse itemResponse = itemService.registerItem(user, itemCreateRequest);
        return ResponseEntity.ok(itemResponse);
    }

    // 아이템 ID로 조회
    @GetMapping("/{itemId}")
    public ItemResponse getItemById(@PathVariable Long itemId) {
        return itemService.getItemDtoById(itemId);
    }

    // 모든 아이템 조회
    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAllItems() {
        List<ItemResponse> items = itemService.getAllItemResponses();
        return ResponseEntity.ok(items);
    }

    // 키워드로 아이템 검색
    @GetMapping("/search")
    public ResponseEntity<?> searchItems(@RequestParam String keyword) {
        List<ItemResponse> items = itemService.itemSearchByName(keyword);
        if (items.isEmpty()) {
            return ResponseEntity.ok("해당하는 상품이 없습니다");
        }
        return ResponseEntity.ok(items);
    }
}
