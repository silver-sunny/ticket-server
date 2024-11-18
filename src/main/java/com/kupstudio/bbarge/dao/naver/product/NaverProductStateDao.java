package com.kupstudio.bbarge.dao.naver.product;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kupstudio.bbarge.constant.product.ProductConstant;
import com.kupstudio.bbarge.dao.naver.oauth.OAuthTokenDao;
import com.kupstudio.bbarge.dto.naver.product.NaverCommonResponseDto;
import com.kupstudio.bbarge.dto.naver.product.NaverDispatchProductOrderDto;
import com.kupstudio.bbarge.dto.naver.product.NaverDispatchProductOrdersDto;
import com.kupstudio.bbarge.dto.naver.product.NaverFailProductOrderInfosDto;
import com.kupstudio.bbarge.dto.naver.request.NaverCancelReasonRequestDto;
import com.kupstudio.bbarge.dto.naver.request.NaverReturnRequestDto;
import com.kupstudio.bbarge.dto.product.order.OrderDetailDto;
import com.kupstudio.bbarge.enumClass.product.ChannelEnum;
import com.kupstudio.bbarge.enumClass.product.naver.NaverCancelReasonEnum;
import com.kupstudio.bbarge.enumClass.product.naver.NaverReturnReasonEnum;
import com.kupstudio.bbarge.exception.common.ConditionFailException;
import com.kupstudio.bbarge.exception.common.PostNotExistException;
import com.kupstudio.bbarge.exception.product.NaverFailException;
import com.kupstudio.bbarge.exception.product.NotInChannelProductException;
import com.kupstudio.bbarge.service.product.ProductTicketService;
import com.kupstudio.bbarge.service.product.order.ProductOrderService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.kupstudio.bbarge.constant.common.CommonConstant.MEDIA_TYPE_APPLICATION_JSON;
import static com.kupstudio.bbarge.constant.product.ProductTicketConstant.EXIST_TICKET_USED;
import static com.kupstudio.bbarge.constant.product.naver.NaverProductUrlConstant.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class NaverProductStateDao {

    private final OAuthTokenDao oAuthTokenDao;

    private final ObjectMapper objectMapper;


    private final ProductOrderService productOrderService;

    private final OkHttpClient client;

    private final ProductTicketService productTicketService;

    /**
     * 네이버 주문 반품 요청 -> 승인 동시에 되게
     * 사용한 티켓이 있으면 안됨
     *
     * @param orderNo
     * @param returnReason
     */
    public void naverReturnRequestAndApprove(int orderNo, NaverReturnReasonEnum returnReason) {

        // 사용한 티켓이 있으면 에러
        if (productTicketService.isProductTicketUsed(orderNo)) {
            throw new ConditionFailException(EXIST_TICKET_USED);
        }
        String channelProductOrderId = this.getChannelProductOrderIdAndCheckValid(orderNo);

        this.naverReturnRequest(channelProductOrderId, returnReason);
        this.naverReturnApprove(channelProductOrderId);
    }


    /**
     * 주문번호로 반품 승인
     * 사용한 티켓이 있으면 안됨
     *
     * @param orderNo
     */

    public void naverReturnApprove(int orderNo) {

        // 사용한 티켓이 있으면 에러
        if (productTicketService.isProductTicketUsed(orderNo)) {
            throw new ConditionFailException(EXIST_TICKET_USED);
        }
        String channelProductOrderId = this.getChannelProductOrderIdAndCheckValid(orderNo);

        this.naverReturnApprove(channelProductOrderId);

    }


    /**
     * 상품 주문 번호로 반품 승인
     *
     * @param channelProductOrderId
     */
    @SneakyThrows
    public void naverReturnApprove(String channelProductOrderId) {

        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_APPLICATION_JSON, "");


        Request request = oAuthTokenDao.naverPostBuildRequest(String.format(NAVER_RETURN_APPROVE_URL, channelProductOrderId), requestBody);


        checkProductOrderValidAndRequestUpdateOrderState(request, channelProductOrderId);


    }


    /**
     * 주문번호로 반품 요청
     *
     * @param orderNo
     * @param returnReason
     */
    public void naverReturnRequest(int orderNo, NaverReturnReasonEnum returnReason) {

        // 사용한 티켓이 있으면 에러
        if (productTicketService.isProductTicketUsed(orderNo)) {
            throw new ConditionFailException(EXIST_TICKET_USED);
        }

        String channelProductOrderId = this.getChannelProductOrderIdAndCheckValid(orderNo);

        this.naverReturnRequest(channelProductOrderId, returnReason);

    }


    /**
     * 상품 주문번호로 반품 요청
     *
     * @param channelProductOrderId 채널 상품 주문 번호
     * @param returnReason          반품 이유
     */
    @SneakyThrows
    public void naverReturnRequest(String channelProductOrderId, NaverReturnReasonEnum returnReason) {


        String returnReasonAsString = objectMapper.writeValueAsString(new NaverReturnRequestDto(returnReason));

        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_APPLICATION_JSON, returnReasonAsString);


        Request request = oAuthTokenDao.naverPostBuildRequest(String.format(NAVER_RETURN_REQUEST_URL, channelProductOrderId), requestBody);
        checkProductOrderValidAndRequestUpdateOrderState(request, channelProductOrderId);

    }


    /**
     * 반품 철회(거부)
     *
     * @param orderNo
     */
    @SneakyThrows
    public void naverReturnReject(int orderNo, String rejectReturnReason) {


        String channelProductOrderId = this.getChannelProductOrderIdAndCheckValid(orderNo);


        JSONObject jsonBody = new JSONObject();
        jsonBody.put("rejectReturnReason", rejectReturnReason);


        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_APPLICATION_JSON, jsonBody.toString());


        Request request = oAuthTokenDao.naverPostBuildRequest(String.format(NAVER_RETURN_REJECT_URL, channelProductOrderId), requestBody);
        checkProductOrderValidAndRequestUpdateOrderState(request, channelProductOrderId);


    }

    /**
     * 주문번호로 취소 승인
     *
     * @param orderNo
     */

    public void naverCancelApprove(int orderNo) {

        String channelProductOrderId = this.getChannelProductOrderIdAndCheckValid(orderNo);

        this.naverCancelApprove(channelProductOrderId);

    }


    /**
     * 상품 주문 번호로 취소 승인
     *
     * @param channelProductOrderId 소셜 상품 주문 번호
     */
    @SneakyThrows
    public void naverCancelApprove(String channelProductOrderId) {

        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_APPLICATION_JSON, "");

        Request request = oAuthTokenDao.naverPostBuildRequest(String.format(NAVER_CANCEL_APPROVE_URL, channelProductOrderId), requestBody);
        checkProductOrderValidAndRequestUpdateOrderState(request, channelProductOrderId);


    }


    /**
     * 주문번호로 취소 요청
     *
     * @param orderNo
     * @param cancelReason
     */
    public void naverCancelRequest(int orderNo, NaverCancelReasonEnum cancelReason) {


        String channelProductOrderId = this.getChannelProductOrderIdAndCheckValid(orderNo);

        this.naverCancelRequest(channelProductOrderId, cancelReason);

    }


    /**
     * 상품 주문번호로 취소 요청
     *
     * @param channelProductOrderId
     * @param cancelReason
     */
    @SneakyThrows
    public void naverCancelRequest(String channelProductOrderId, NaverCancelReasonEnum cancelReason) {


        String cancelReasonAsString = objectMapper.writeValueAsString(new NaverCancelReasonRequestDto(cancelReason));


        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_APPLICATION_JSON, cancelReasonAsString);


        Request request = oAuthTokenDao.naverPostBuildRequest(String.format(NAVER_CANCEL_REQUEST_URL, channelProductOrderId), requestBody);
        checkProductOrderValidAndRequestUpdateOrderState(request, channelProductOrderId);


    }

    /**
     * 발송 완료 처리
     *
     * @param orderNo
     */
    @SneakyThrows
    public void dispatchNaverProduct(int orderNo) {

        String channelProductOrderId = this.getChannelProductOrderIdAndCheckValid(orderNo);


        NaverDispatchProductOrderDto dispatchProductOrderDto = new NaverDispatchProductOrderDto(channelProductOrderId);

        List<NaverDispatchProductOrderDto> dispatchProductOrderDtoList = new ArrayList<>();
        dispatchProductOrderDtoList.add(dispatchProductOrderDto);

        NaverDispatchProductOrdersDto dispatchProductOrdersDto = new NaverDispatchProductOrdersDto(dispatchProductOrderDtoList);


        String dispatchProductOrderDtoListAsString = objectMapper.writeValueAsString(dispatchProductOrdersDto);

        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_APPLICATION_JSON, dispatchProductOrderDtoListAsString);

        Request request = oAuthTokenDao.naverPostBuildRequest(NAVER_DISPATCH_URL, requestBody);


        checkProductOrderValidAndRequestUpdateOrderState(request, channelProductOrderId);
    }


    /**
     * 주문 정보 출력 및 채널 네이버 맞는지 확인
     *
     * @param orderNo
     * @return
     */
    private String getChannelProductOrderIdAndCheckValid(int orderNo) {

        OrderDetailDto orderDetailDto = productOrderService.getOrderDetail(orderNo);

        if (orderDetailDto == null) {

            // 등록이 안된 주문번호 에러처리
            throw new PostNotExistException(ProductConstant.PRODUCT_IS_NOT_EXIST);

        }

        String channelProductOrderId = orderDetailDto.getChannelProductOrderId();


        if (StringUtils.isEmpty(channelProductOrderId) ||
                !orderDetailDto.getChannel().equals(ChannelEnum.NAVER)) {

            // channelProductOrderId 없을경우 , 네이버가 아닐경우 에러
            throw new NotInChannelProductException();
        }

        return channelProductOrderId;
    }

    /**
     * 주문 상태 변경
     * 요청이 완료되었는지,
     * 에러가 났으면 모델 변경후 response
     * naver쪽에서 res는 1개밖에 없지만 통합을 위해 리스트로 옴
     *
     * @param request
     * @param channelProductOrderId
     */
    @SneakyThrows
    private void checkProductOrderValidAndRequestUpdateOrderState(Request request, String channelProductOrderId) {


        Response response = client.newCall(request).execute();

        // dto에 res값 일부분 없어도 에러 안나게
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String body = response.body().string();

        log.info(body);

        NaverCommonResponseDto naverCommonRes = objectMapper.readValue(body, NaverCommonResponseDto.class);


        if (!StringUtils.isEmpty(naverCommonRes.getCode())) {
            // 네이버 코드있으면 throw
            throw new NaverFailException(naverCommonRes.getMessage());
        }

        List<NaverFailProductOrderInfosDto> failProductOrderInfos = naverCommonRes.getData().getFailProductOrderInfos();

        if (!failProductOrderInfos.isEmpty()) {

            String message = (failProductOrderInfos.get(0) == null)
                    ? ""
                    : failProductOrderInfos.get(0).getMessage();

            throw new NaverFailException(message);
        }


        productOrderService.updateProductOrderIsProgressing(channelProductOrderId, ChannelEnum.NAVER);
    }


}
