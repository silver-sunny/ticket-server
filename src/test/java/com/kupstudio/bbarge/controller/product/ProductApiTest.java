package com.kupstudio.bbarge.controller.product;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductApiTest.class)
public class ProductApiTest {
    public static void main(String[] args) {
        // API 호출 - 요청 타입: POST
        // API 동시성 제어 테스트 (멀티 스레드를 이용해 API를 동시에 호출 하였을 때 DB에서 값이 정상적으로 변화하는지 테스트)
        runPostRequest();

        // API 호출 - 요청 타입: GET
        // API 호출 테스트
//        runGetRequest();
    }

    static void runPostRequest() {
        // API 호출을 위한 RestTemplate 객체를 생성
        RestTemplate restTemplate = new RestTemplate();

        // 테스트 진행할 API URL (상품 구매 API)
        String purchaseApiUrl = "http://localhost:880/v1/api/store/product/purchase";

        //  header 설정을 위해 HttpHeader 클래스를 생성한 후 HttpEntity 객체에 셋팅
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 상품 구매 API - request body 셋팅
        String purchaseRequestBody = "{\"channelEnum\": \"NEVERLAND_HC\", \"productNo\": 89, \"userNo\": 4, \"purchaseQuantity\": 5, \"directOption\": \"2024-02-05\"}";

        // 상품 구매 API를 위한 headers, body가 셋팅된 HTTP entity 생성
        HttpEntity<String> purchaseRequestEntity = new HttpEntity<>(purchaseRequestBody, headers);

        // 요청을 보낼 횟수 (멀티 스레드를 이용해 동시에 보낼 횟수)
        int numRequests = 3;

        // threads 생성 및 실행 - 상품 구매 API
        for (int i = 0; i < numRequests; i++) {
            Thread thread = new Thread(new ApiPostRequestRunnable(purchaseApiUrl, restTemplate, purchaseRequestEntity));
            thread.start();
        }
    }

    static void runGetRequest() {
        // API 호출을 위한 RestTemplate 객체를 생성
        RestTemplate restTemplate = new RestTemplate();

        // 테스트 진행할 API URL (특정 상품 조회 API)
        String productApiUrl = "http://localhost:880/v1/api/store/product/89";

        // 요청을 보낼 횟수 (멀티 스레드를 이용해 동시에 보낼 횟수)
        int numRequests = 1;

        // threads 생성 및 실행 - 특정 상품 조회 API
        for (int i = 0; i < numRequests; i++) {
            Thread thread = new Thread(new ApiGetRequestRunnable(productApiUrl, restTemplate));
            thread.start();
        }
    }

    static class ApiPostRequestRunnable implements Runnable {
        private String apiUrl;
        private RestTemplate restTemplate;
        private HttpEntity<String> requestEntity;

        public ApiPostRequestRunnable(String apiUrl, RestTemplate restTemplate, HttpEntity<String> requestEntity) {
            this.apiUrl = apiUrl;
            this.restTemplate = restTemplate;
            this.requestEntity = requestEntity;
        }

        @Override
        public void run() {
            // exchange 메소드로 API 호출 - POST
            ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

            // 호출 결과 출력
            System.out.println("Response: " + responseEntity.getBody());
        }
    }

    static class ApiGetRequestRunnable implements Runnable {
        private String apiUrl;
        private RestTemplate restTemplate;

        public ApiGetRequestRunnable(String apiUrl, RestTemplate restTemplate) {
            this.apiUrl = apiUrl;
            this.restTemplate = restTemplate;
        }

        @Override
        public void run() {
            // getForEntity 메소드로 API 호출 - GET
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);

            // 호출 결과 출력
            System.out.println("GET Response: " + responseEntity.getBody());
        }
    }
}
