package com.kupstudio.bbarge.dao.naver.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kupstudio.bbarge.dao.naver.oauth.OAuthTokenDao;
import com.kupstudio.bbarge.dto.naver.images.NaverImagesResponseDto;
import com.kupstudio.bbarge.dto.naver.product.NaverCommonResponseDto;
import com.kupstudio.bbarge.dto.naver.product.NaverProductDto;
import com.kupstudio.bbarge.dto.naver.product.NaverProductResponseDto;
import com.kupstudio.bbarge.exception.product.NaverFailException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.util.List;

import static com.kupstudio.bbarge.constant.common.CommonConstant.MEDIA_TYPE_APPLICATION_JSON;
import static com.kupstudio.bbarge.constant.product.naver.NaverProductUrlConstant.*;


@Service
@RequiredArgsConstructor
public class NaverProductDao {

    private final OAuthTokenDao oAuthTokenDao;

    private final ObjectMapper objectMapper;

    private final OkHttpClient client;


    /**
     * 네이버 API 상품 추가
     *
     * @param naverProductDto
     * @return
     */
    @SneakyThrows
    public NaverProductResponseDto insertNaverProduct(NaverProductDto naverProductDto) {

        String returnReasonAsString = objectMapper.writeValueAsString(naverProductDto);

        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_APPLICATION_JSON, returnReasonAsString);


        Request request = oAuthTokenDao.naverPostBuildRequest(NAVER_INSERT_PRODUCT_URL, requestBody);


        Response response = client.newCall(request).execute();


        if (!response.isSuccessful()) {
            NaverCommonResponseDto naverFailCommonRes = objectMapper.readValue(response.body().string(), NaverCommonResponseDto.class);

            // 네이버 코드있으면 throw
            throw new NaverFailException(naverFailCommonRes.getMessage());
        }

        return objectMapper.readValue(response.body().string(), NaverProductResponseDto.class);

    }

    /**
     * 파일 업로드
     *
     * @param imageFiles
     * @return
     */
    @SneakyThrows
    public NaverImagesResponseDto uploadNaverProductImages(List<MultipartFile> imageFiles) {

        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        for (MultipartFile file : imageFiles) {
            RequestBody fileBody = RequestBody.create(MediaType.parse(file.getContentType()), file.getBytes());
            requestBodyBuilder.addFormDataPart("imageFiles", URLEncoder.encode(file.getOriginalFilename(), "UTF-8"), fileBody);
        }


        RequestBody requestBody = requestBodyBuilder.build();


        Request request = oAuthTokenDao.naverPostBuildRequest(NAVER_INSERT_PRODUCT_IMAGES_URL, requestBody, "multipart/form-data");

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            NaverCommonResponseDto naverFailCommonRes = objectMapper.readValue(response.body().string(), NaverCommonResponseDto.class);

            // 네이버 코드있으면 throw
            throw new NaverFailException(naverFailCommonRes.getMessage());
        }

        return objectMapper.readValue(response.body().string(), NaverImagesResponseDto.class);


    }

    /**
     * 네이버에서 상품 하나 가져오기
     *
     * @param channelProductNo
     * @return
     */
    @SneakyThrows
    public NaverProductDto getNaverProduct(String channelProductNo) {


        Request request = oAuthTokenDao.naverGetBuildRequest(NAVER_GET_PRODUCT_URL + channelProductNo);


        Response response = client.newCall(request).execute();


        if (!response.isSuccessful()) {
            NaverCommonResponseDto naverFailCommonRes = objectMapper.readValue(response.body().string(), NaverCommonResponseDto.class);

            // 네이버 코드있으면 throw
            throw new NaverFailException(naverFailCommonRes.getMessage());
        }

        return objectMapper.readValue(response.body().string(), NaverProductDto.class);


    }


    /**
     * 네이버 API에서 상품 수정
     *
     * @param naverProductDto
     * @param channelProductNo
     * @return
     */
    @SneakyThrows
    public NaverProductResponseDto updateNaverProduct(NaverProductDto naverProductDto, String channelProductNo) {


        String returnReasonAsString = objectMapper.writeValueAsString(naverProductDto);

        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_APPLICATION_JSON, returnReasonAsString);


        Request request = oAuthTokenDao.naverPutBuildRequest(NAVER_UPDATE_PRODUCT_URL + channelProductNo, requestBody);


        Response response = client.newCall(request).execute();


        if (!response.isSuccessful()) {
            NaverCommonResponseDto naverFailCommonRes = objectMapper.readValue(response.body().string(), NaverCommonResponseDto.class);

            // 네이버 코드있으면 throw
            throw new NaverFailException(naverFailCommonRes.getMessage());
        }

        return objectMapper.readValue(response.body().string(), NaverProductResponseDto.class);

    }

    /**
     * 네이버 API에서 상품 삭제
     *
     * @param channelProductNo
     */

    @SneakyThrows
    public void deleteNaverProduct(String channelProductNo) {

        Request request = oAuthTokenDao.naverDeleteBuildRequest(NAVER_DELETE_PRODUCT_URL + channelProductNo);


        Response response = client.newCall(request).execute();


        if (!response.isSuccessful()) {
            NaverCommonResponseDto naverFailCommonRes = objectMapper.readValue(response.body().string(), NaverCommonResponseDto.class);

            // 네이버 코드있으면 throw
            throw new NaverFailException(naverFailCommonRes.getMessage());
        }


    }


}
