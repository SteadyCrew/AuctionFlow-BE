package org.example.auctionflowbe.service;

import org.example.auctionflowbe.entity.User;
import org.example.auctionflowbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveOrUpdate(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser == null) {
            userRepository.save(user);
        } else {
            existingUser.setNickname(user.getNickname());
            existingUser.setProfileImageUrl(user.getProfileImageUrl());
            existingUser.setAccessToken(user.getAccessToken());
            existingUser.setRefreshToken(user.getRefreshToken());
            existingUser.setTokenExpiry(user.getTokenExpiry());
            userRepository.save(existingUser);
        }
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
