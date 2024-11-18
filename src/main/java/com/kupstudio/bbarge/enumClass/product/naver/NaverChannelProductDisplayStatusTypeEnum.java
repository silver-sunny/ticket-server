package com.kupstudio.bbarge.enumClass.product.naver;

import com.kupstudio.bbarge.enumClass.product.ProductStateEnum;
import lombok.Getter;

@Getter
public enum NaverChannelProductDisplayStatusTypeEnum {

    ON, // 전시중

    SUSPENSION; // 전시 중지


    public static NaverChannelProductDisplayStatusTypeEnum getNaverChannelProductDisplayStatusTypeEnum(ProductStateEnum productStateEnum) {

        switch (productStateEnum) {
            case SALE:
                return NaverChannelProductDisplayStatusTypeEnum.ON;
            case SUSPENSION:
                return NaverChannelProductDisplayStatusTypeEnum.SUSPENSION;
        }

        return null;
    }


}
