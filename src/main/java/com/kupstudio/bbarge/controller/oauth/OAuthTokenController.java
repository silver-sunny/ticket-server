package com.kupstudio.bbarge.controller.oauth;

import com.kupstudio.bbarge.dao.naver.oauth.OAuthTokenDao;
import com.kupstudio.bbarge.dto.naver.auth.NaverOAuthTokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthTokenController {

    private final OAuthTokenDao tokenService;

    @GetMapping(value = "/token")
    public NaverOAuthTokenResponseDto getOAuthToken() {

        return tokenService.getOAuthToken();
    }
}