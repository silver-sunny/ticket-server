package com.kupstudio.bbarge.security;

import com.kupstudio.bbarge.dto.admin.AdminAuthDto;
import com.kupstudio.bbarge.dto.admin.AdminRefreshTokenDto;
import com.kupstudio.bbarge.security.admin.AdminPrincipal;
import com.kupstudio.bbarge.security.properties.AppProperties;
import com.kupstudio.bbarge.service.admin.AdminRefreshService;
import com.kupstudio.bbarge.service.admin.AdminService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final AuthTokenProvider tokenProvider;


    private final AdminRefreshService adminRefreshService;

    private final AppProperties appProperties;
    private final AdminService adminService;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String tokenStr = HeaderUtil.getAccessToken(request);

        String refreshToken = HeaderUtil.getRefreshToken(request);
        AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);


        if (tokenStr != null) {
            AuthToken authToken = tokenProvider.convertAuthToken(tokenStr);

            if (authToken.validate()) {
                this.setAuthentication(authToken);

            } else if (!authRefreshToken.validateToken(authToken.getToken()) && refreshToken != null) {

                Claims claims = authToken.getExpiredTokenClaims();

                String sub = claims.getSubject();


                AdminRefreshTokenDto userRefreshTokenDto = adminRefreshService.getRefreshTokenByAdminId(sub);


                if (userRefreshTokenDto != null) {
                    Date now = new Date();
                    AuthToken newAccessToken = tokenProvider.createAuthToken(
                            sub,
                            new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
                    );
                    this.setAuthentication(newAccessToken);
                    response.setHeader(AUTHORIZATION, newAccessToken.getToken());

                }


            }

        }


        filterChain.doFilter(request, response);
    }

    public void setAuthentication(AuthToken token) {

        Claims claims = token.getTokenClaims();

        String sub = claims.getSubject();

        AdminAuthDto adminDto = adminService.getAdminInfoByAdminId(sub);


        if (adminDto != null) {
            AdminPrincipal adminPrincipal = new AdminPrincipal(adminDto);

            Authentication authentication = tokenProvider.getAuthentication(token, adminPrincipal);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } else {
            throw new UsernameNotFoundException("ADMIN NOT EXIST");
        }


    }
}

