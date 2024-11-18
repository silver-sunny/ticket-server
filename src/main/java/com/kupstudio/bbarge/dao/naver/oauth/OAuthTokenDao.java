package com.kupstudio.bbarge.dao.naver.oauth;

import com.kupstudio.bbarge.dto.naver.auth.NaverOAuthTokenResponseDto;
import com.kupstudio.bbarge.utils.SignatureGeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthTokenDao {

    private static final String API_URL = "https://api.commerce.naver.com/external/v1/oauth2/token";
    private static final String GRANT_TYPE = "client_credentials";
    private static final String TYPE = "SELF";
    private static final String ERROR_MESSAGE = "인증 토큰을 발급하는 과정에서 오류가 발생하였습니다.";
    @Value("${naver.signature.client-id}")
    private String clientId;
    @Value("${naver.signature.client-secret}")
    private String clientSecret;

    public NaverOAuthTokenResponseDto getOAuthToken() {
        try {
            Long timestamp = getTimestamp() - 10000;
            String signature = SignatureGeneratorUtil.generateSignature(clientId, clientSecret, timestamp);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("client_id", clientId);
            params.add("timestamp", String.valueOf(timestamp));
            params.add("client_secret_sign", signature);
            params.add("grant_type", GRANT_TYPE);
            params.add("type", TYPE);

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<NaverOAuthTokenResponseDto> responseEntity = restTemplate.exchange(
                    API_URL,
                    HttpMethod.POST,
                    requestEntity,
                    NaverOAuthTokenResponseDto.class
            );

            return responseEntity.getBody();
        } catch (Exception e) {
            log.error("Error while obtaining OAuth token: {}", e.getMessage(), e);
            return null;
        }
    }


    private long getTimestamp() {
        return System.currentTimeMillis();
    }

    public Request naverPostBuildRequest(String url, RequestBody requestBody) {

        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + getOAuthToken().getAccessToken())
                .addHeader("content-type", "application/json")
                .build();

    }


    public Request naverPostBuildRequest(String url, RequestBody requestBody, String contentType) {

        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + getOAuthToken().getAccessToken())
                .addHeader("content-type", contentType)
                .build();

    }


    public Request naverGetBuildRequest(String url) {

        return new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer " + getOAuthToken().getAccessToken())
                .addHeader("content-type", "application/json")
                .build();

    }

    public Request naverDeleteBuildRequest(String url) {

        return new Request.Builder()
                .url(url)
                .delete()
                .addHeader("Authorization", "Bearer " + getOAuthToken().getAccessToken())
                .addHeader("content-type", "application/json")
                .build();

    }

    public Request naverPutBuildRequest(String url, RequestBody requestBody) {

        return new Request.Builder()
                .url(url)
                .put(requestBody)
                .addHeader("Authorization", "Bearer " + getOAuthToken().getAccessToken())
                .addHeader("content-type", "application/json")
                .build();

    }

}
