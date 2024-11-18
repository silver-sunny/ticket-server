package com.kupstudio.bbarge.dto.product.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderNonIssuedTicketStatsDto {

    @Schema(description = "티켓미발급 건수")
    private int nonIssuedTicketTotalCount;

    @Schema(description = "티켓발급마감 d-day")
    private int ticketDeadlineTotalCount;

    public OrderNonIssuedTicketStatsDto(int nonIssuedTicketTotalCount, int ticketDeadlineTotalCount) {
        this.nonIssuedTicketTotalCount = nonIssuedTicketTotalCount;
        this.ticketDeadlineTotalCount = ticketDeadlineTotalCount;
    }
}
