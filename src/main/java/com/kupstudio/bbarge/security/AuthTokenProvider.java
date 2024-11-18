package com.kupstudio.bbarge.security;

import com.kupstudio.bbarge.enumClass.admin.AdminRoleEnum;
import com.kupstudio.bbarge.exception.common.ConditionFailException;
import com.kupstudio.bbarge.security.admin.AdminPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.Date;

@Slf4j
public class AuthTokenProvider {


    private final Key key;

    public AuthTokenProvider(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }


    public AuthToken createAuthToken(String id, Date expiry) {
        return new AuthToken(id, expiry, key);
    }

    public AuthToken createAuthToken(String id, AdminRoleEnum role, Date expiry) {
        return new AuthToken(id, role, expiry, key);
    }


    public AuthToken convertAuthToken(String token) {
        return new AuthToken(token, key);
    }

    public Authentication getAuthentication(AuthToken authToken, AdminPrincipal userPrincipal) {

        if (authToken.validate()) {

            Claims claims = authToken.getTokenClaims();

            log.debug("claims subject := [{}]", claims.getSubject());


            return new UsernamePasswordAuthenticationToken(userPrincipal, authToken, userPrincipal.getAuthorities());
        } else {
            throw new ConditionFailException();
        }
    }
}

