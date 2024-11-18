package com.kupstudio.bbarge.dto.naver.request;

import com.kupstudio.bbarge.enumClass.product.naver.NaverReturnReasonEnum;
import lombok.Getter;

@Getter
public class NaverReturnRequestDto {

    private NaverReturnReasonEnum returnReason;

    private String collectDeliveryMethod;

    public NaverReturnRequestDto(NaverReturnReasonEnum returnReason) {
        this.returnReason = returnReason;
        this.collectDeliveryMethod = "NOTHING";
    }
}
