package org.example.auctionflowbe.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    @GetMapping("/loginSuccess")
    public RedirectView loginSuccess(@AuthenticationPrincipal OAuth2User oauth2User) {
        // 리다이렉트할 URL 설정
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://localhost:3000/");

        return redirectView;
    }

    @GetMapping("/loginFailure")
    public Map<String, String> loginFailure() {
        return Map.of("error", "Login failed");
    }


}