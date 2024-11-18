package com.kupstudio.bbarge.security;

import javax.servlet.http.HttpServletRequest;

public class HeaderUtil {

    private final static String HEADER_AUTHORIZATION = "Authorization";

    private final static String HEADER_REFRESH = "req";


    private final static String ACCESS_TOKEN_PREFIX = "Bearer ";

    private final static String REFRESH_TOKEN_PREFIX = "Refresh ";


    public static String getAccessToken(HttpServletRequest request) {
        String headerValue = request.getHeader(HEADER_AUTHORIZATION);

        if (headerValue == null) {
            return null;
        }

        if (headerValue.startsWith(ACCESS_TOKEN_PREFIX)) {
            return headerValue.substring(ACCESS_TOKEN_PREFIX.length());
        }

        return null;
    }

    public static String getRefreshToken(HttpServletRequest request) {
        String headerValue = request.getHeader(HEADER_REFRESH);

        if (headerValue == null) {
            return null;
        }

        if (headerValue.startsWith(REFRESH_TOKEN_PREFIX)) {
            return headerValue.substring(REFRESH_TOKEN_PREFIX.length());
        }
        return null;

    }

}
