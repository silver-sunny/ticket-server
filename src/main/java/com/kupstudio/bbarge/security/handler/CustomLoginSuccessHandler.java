package com.kupstudio.bbarge.security.handler;

import com.kupstudio.bbarge.dto.admin.AdminAuthDto;
import com.kupstudio.bbarge.dto.admin.AdminRefreshTokenDto;
import com.kupstudio.bbarge.security.AuthToken;
import com.kupstudio.bbarge.security.AuthTokenProvider;
import com.kupstudio.bbarge.security.TokenService;
import com.kupstudio.bbarge.service.admin.AdminRefreshService;
import com.kupstudio.bbarge.service.admin.AdminService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthTokenProvider tokenProvider;
    private final AdminRefreshService userRefreshTokenService;
    private final AdminService adminService;
    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String adminId = extractUsername(authentication);

        AdminAuthDto userAuthDto = adminService.getAdminInfoByAdminId(adminId);

        AdminRefreshTokenDto userRefreshToken = userRefreshTokenService.getRefreshTokenByAdminId(adminId);


        AuthToken newAuthRefreshToken = tokenService.createRefreshToken();

        String refreshToken = newAuthRefreshToken.getToken();

        if (userRefreshToken != null) {

            // 기존에 있던 refresh token
            String existingRefreshToken = userRefreshToken.getRefreshToken();

            AuthToken authRefreshToken = tokenProvider.convertAuthToken(userRefreshToken.getRefreshToken());

            Claims claims = authRefreshToken.getExpiredTokenClaims();


            if (claims != null) {
                // refresh token 유효기간이 지났으면
                userRefreshToken.setRefreshToken(newAuthRefreshToken.getToken());
                userRefreshTokenService.insertUserRefreshToken(userRefreshToken);

            } else {
                // 유효기간 안지났으면 기존 코드
                refreshToken = existingRefreshToken;
                userRefreshToken.setRefreshToken(existingRefreshToken);
            }

        } else {
            userRefreshToken = new AdminRefreshTokenDto(userAuthDto.getAdminNo(), userAuthDto.getAdminId(), newAuthRefreshToken.getToken());

            userRefreshTokenService.insertUserRefreshToken(userRefreshToken);
        }

        AuthToken accessToken = tokenService.createAccessToken(
                adminId,
                userAuthDto.getRole()
        );

        response.setHeader("access-token", accessToken.getToken());
        response.setHeader("refresh-token", refreshToken);


    }


    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}

