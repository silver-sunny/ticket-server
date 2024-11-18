package com.kupstudio.bbarge.dto.naver.product;

import lombok.Getter;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class NaverDispatchProductOrderDto {

    private String productOrderId;

    private String deliveryMethod;

    private String dispatchDate;

    public NaverDispatchProductOrderDto(String channelProductOrderId) {
        this.productOrderId = channelProductOrderId;
        this.deliveryMethod = "NOTHING";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        this.dispatchDate = OffsetDateTime.now().format(formatter);

    }

}


