package org.example.auctionflowbe.controller;

import org.example.auctionflowbe.dto.ItemCreateRequest;
import org.example.auctionflowbe.dto.ItemResponse;
import org.example.auctionflowbe.entity.User;
import org.example.auctionflowbe.service.ItemService;
import org.example.auctionflowbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @PostMapping
    public ItemResponse createItem(
            @AuthenticationPrincipal OAuth2User oauth2User,
            @RequestBody ItemCreateRequest itemCreateRequest) {
        String email = oauth2User.getAttribute("email");
        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return itemService.registerItem(user, itemCreateRequest);
    }
    @GetMapping("/{itemId}")
    public ItemResponse getItemById(@PathVariable Long itemId) {
        return itemService.getItemById(itemId);
    }
}