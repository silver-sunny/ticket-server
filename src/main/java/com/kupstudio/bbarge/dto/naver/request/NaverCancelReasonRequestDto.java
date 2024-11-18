package com.kupstudio.bbarge.dto.naver.request;

import com.kupstudio.bbarge.enumClass.product.naver.NaverCancelReasonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NaverCancelReasonRequestDto {

    private NaverCancelReasonEnum cancelReason;

}
