package com.kupstudio.bbarge.enumClass.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCodeEnum {

    // product 관련 error 44X
    NOT_IN_CHANNEL_PRODUCT(440, "채널에 등록되지 않은 주문번호(orderNo)입니다."),
    PRODUCT_IS_EXIST(441, "상품이 존재합니다."),

    ADMIN_STORE_IS_EXIST(450, "담당 매장이 있습니다."),
    ADMIN_STORE_NOT_EXIST(451, "담당 매장이 없습니다."),

    NAVER_FAIL_EXCEPTION(470, "NAVER FAIL EXCEPTION"),

    WRITER_NOT_MATCH(482, "작성자가 아닙니다."),
    FILE_RELATED_FAIL(484, "파일 업로드 실패"),
    CONDITION_FAIL(485, "조건 실패"),
    INSERT_FAIL(486, "저장 실패"),
    DELETE_FAIL(489, "삭제 실패"),


    POST_NOT_EXIST(490, "DB에 존재하지 않는 값을 요청하고 있습니다."),

    SMS_TO_NOT_EXIST(495, "문자 전송 대상이 유효하지 않음"),
    STORE_DELETE_CONDITION_FAIL(496, "매장 삭제 조건을 충족하지 않음"),

    //////////////////////////////// 네이버 오류 //////////////////////////
    NAVER_WRONG_PARAM(4000, "잘못된 요청 파라미터	오류 유형.객체명.필드명"),
    NAVER_NOT_EXIST_CODE(9999, "기타 정의되지 않은 오류 코드"),
    NAVER_NOT_EXIST_PRODUCT_ORDER(100001, "상품 주문을 찾을 수 없음"),
    NAVER_NOT_EXIST_ORDER(100003, "주문을 찾을 수 없음"),
    NAVER_NO_PERMISSION(101009, "처리 권한이 없는 상품 주문 번호를 요청"),
    NAVER_DELIVERY_DUE_DATE_RANGE_EXCEEDED(104105, "발송 기한 입력 범위 초과"),
    NAVER_NEED_CHANGE_SHIPPING_NOT_DELIVERY(104116, "배송 방법 변경 필요(배송 없음 주문)"),
    NAVER_NEED_CHANGE_SHIPPING(104117, "배송 방법 변경 필요"),
    NAVER_DELIVERY_COMPANY_NOT_ENTERED(104118, "택배사 미입력"),
    NAVER_CHECK_DELIVERY_COMPANY_CODE(104119, "택배사 코드 확인"),
    NAVER_NOT_ENTERED_INVOICE_NUMBER(104120, "송장 번호 미입력"),
    NAVER_DRIVER_DELIVERY_INVOICE_ERROR(104121, "배송 송장 오류(기사용 송장)	"),
    NAVER_INVALID_DELIVERY_INVOICE_ERROR(104122, "배송 송장 오류(비유효 송장)	"),
    NAVER_DUPLICATE_PRODUCT_ORDER(104131, "상품 주문 번호 중복"),
    NAVER_BAD_REQUEST(104133, "잘못된 요청	"),
    NAVER_CHECK_EXCHANGE_STATUS(104417, "교환 상태 확인 필요(재배송 처리 불가능 주문 상태)	"),
    NAVER_NOT_DELIVERED_DESIRED_DATE(104441, "희망일 배송 상품이 아님"),
    NAVER_CHECK_PRODUCT_ORDER_STATUS(104442, "상품 주문 상태 확인 필요"),
    NAVER_CHECK_ORDER_STATUS(104443, "발주 상태 확인 필요"),
    NAVER_INVALID_PREFERRED_DELIVERY_DATE(104444, "배송 희망일 날짜가 유효하지 않음"),
    NAVER_EXCEEDED_DELIVERY_DATE_OVER(104445, "배송 희망일 변경 가능 날짜 초과"),
    NAVER_DUPLICATE_DESIRED_DELIVERY_DATE(104446, "배송 희망일 변경 중복 요청");


    private int errorCode;
    private String errorMessage;

    ErrorCodeEnum(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }


}