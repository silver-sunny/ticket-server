package com.kupstudio.bbarge.security;


import com.kupstudio.bbarge.enumClass.admin.AdminRoleEnum;
import com.kupstudio.bbarge.security.properties.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final AuthTokenProvider tokenProvider;

    private final AppProperties appProperties;


    public AuthToken createRefreshToken() {

        Date now = new Date();


        AuthToken refreshToken = tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                new Date(now.getTime() + appProperties.getAuth().getRefreshTokenExpiry())
        );

        return refreshToken;
    }


    public AuthToken createAccessToken(String sub, AdminRoleEnum role) {

        Date now = new Date();


        AuthToken accessToken = tokenProvider.createAuthToken(
                sub,
                role,
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        return accessToken;

    }


}
