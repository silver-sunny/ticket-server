package com.kupstudio.bbarge.enumClass.product.naver;

import com.kupstudio.bbarge.enumClass.product.ProductStateEnum;
import lombok.Getter;

@Getter
public enum NaverMethodStateEnum {

    INSERT, INSERT_AND_UPDATE;

    public static NaverMethodStateEnum getNaverState(ProductStateEnum productStateEnum) {

        // 자체 등록이 판매중이면 추가
        if (ProductStateEnum.SALE.equals(productStateEnum)) {
            return NaverMethodStateEnum.INSERT;
        } else {
            // 대기중이면 insert 후 update
            return NaverMethodStateEnum.INSERT_AND_UPDATE;
        }
    }
}
