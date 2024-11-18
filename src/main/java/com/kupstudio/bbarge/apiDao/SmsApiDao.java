package com.kupstudio.bbarge.apiDao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kupstudio.bbarge.dto.sms.MessageDto;
import com.kupstudio.bbarge.dto.sms.SmsRequestDto;
import com.kupstudio.bbarge.dto.sms.SmsResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SmsApiDao {

    @Value("${naver-cloud-sms.accessKey}")
    private String ACCESS_KEY;
    @Value("${naver-cloud-sms.secretKey}")
    private String SECRET_KEY;
    @Value("${naver-cloud-sms.serviceId}")
    private String SERVICE_ID;
    @Value("${naver-cloud-sms.senderPhone}")
    private String SENDER_PHONE;

    public SmsResponseDto sendMessage(MessageDto messageDto) throws NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException {
        long time = System.currentTimeMillis();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", Long.toString(time));
        headers.set("x-ncp-iam-access-key", ACCESS_KEY);
        headers.set("x-ncp-apigw-signature-v2", makeSignature(time));

        List<MessageDto> messages = new ArrayList<>();
        messages.add(messageDto);

        String type = messageDto.getContent().length() > 45 ? "LMS"  : "SMS"; // SMS : 한글기준 45자(90byte) 내외 / LMS : 한글기준 1,000자(2,000byte) 내외

        SmsRequestDto request = SmsRequestDto.builder()
                .type(type)
                .contentType("COMM")
                .countryCode("82")
                .from(SENDER_PHONE)
                .content(messageDto.getContent())
                .messages(messages)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(request);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<SmsResponseDto> responseEntity = restTemplate.exchange(new URI("https://sens.apigw.ntruss.com/sms/v2/services/" + SERVICE_ID + "/messages"), HttpMethod.POST, httpEntity, SmsResponseDto.class);
        return responseEntity.getBody();
    }

    public String makeSignature(Long time) throws NoSuchAlgorithmException, InvalidKeyException {

        String method = "POST";
        String url = "/sms/v2/services/" + SERVICE_ID + "/messages";
        String timestamp = time.toString();

        String message = String.format("%s %s\n%s\n%s", method, url, timestamp, ACCESS_KEY);

        SecretKeySpec signingKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

        return Base64.encodeBase64String(rawHmac);
    }
}
