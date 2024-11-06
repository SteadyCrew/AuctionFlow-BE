package org.example.auctionflowbe.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDto {
    private String email;
    private String nickname;
    private String password;
}