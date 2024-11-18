package com.kupstudio.bbarge.dto.product.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCancelOrReturnStatsDto {

    @Schema(description = "취소/반품 요청 건수")
    private int requestTotalCount;

    @Schema(description = "취소/반품 완료 건수")
    private int doneTotalCount;

    public OrderCancelOrReturnStatsDto(int requestTotalCount, int doneTotalCount) {
        this.requestTotalCount = requestTotalCount;
        this.doneTotalCount = doneTotalCount;
    }
}
