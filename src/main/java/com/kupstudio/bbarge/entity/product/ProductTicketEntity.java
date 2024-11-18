package com.kupstudio.bbarge.entity.product;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProductTicketEntity {
    private int ticketNo;

    private String ticketKey;

    private int storeNo;

    private int productNo;

    private String phoneNumber;

    private int channelNo;

    private LocalDateTime purchaseAt;

    private LocalDateTime createAt;

    private boolean isUsed;

    private LocalDateTime usedAt;

    private String purchaseUserId;

    private long price;

    private int purchaseQuantity;

    private String purchaseUserName;

    private String directOption;

    private int orderNo;

    public static ProductTicketEntity ticketEntityByProductOrder(ProductOrderEntity productOrder, String ticketKey) {
        LocalDateTime currentDate = LocalDateTime.now().withNano(0);

        return ProductTicketEntity.builder()
                .productNo(productOrder.getProductNo())
                .phoneNumber(productOrder.getPhoneNumber())
                .channelNo(productOrder.getChannelNo())
                .purchaseAt(productOrder.getPurchaseAt())
                .storeNo(productOrder.getStoreNo())
                .createAt(currentDate)
                .ticketKey(ticketKey)
                .purchaseUserId(productOrder.getPurchaseUserId())
                .purchaseUserName(productOrder.getPurchaseUserName())
                .price(productOrder.getPurchasePrice())
                .purchaseQuantity(productOrder.getPurchaseQuantity())
                .directOption(productOrder.getDirectOption())
                .orderNo(productOrder.getOrderNo())
                .build();
    }
}
