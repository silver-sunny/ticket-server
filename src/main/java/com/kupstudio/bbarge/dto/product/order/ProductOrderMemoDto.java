package com.kupstudio.bbarge.dto.product.order;

import com.kupstudio.bbarge.requestDto.product.ProductOrderMemoModifyRequestDto;
import com.kupstudio.bbarge.requestDto.product.ProductOrderMemoRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProductOrderMemoDto {

    @Schema(description = "주문번호")
    private int orderNo;

    @Schema(description = "메모번호")
    private int memoNo;

    @Schema(description = "메모")
    private String memo;

    @Schema(description = "작성자ID")
    private String adminID;

    @Schema(description = "메모 등록 시간")
    private LocalDateTime createAt;

    @Schema(description = "메모 수정 시간")
    private LocalDateTime modifyAt;

    public ProductOrderMemoDto ProductOrderMemoRequestDtoToProductOrderMemoDto(int orderNo, ProductOrderMemoRequestDto requestDto, String adminId) {
        return ProductOrderMemoDto.builder()
                .orderNo(orderNo)
                .memo(requestDto.getMemo())
                .adminID(adminId)
                .build();
    }

    public ProductOrderMemoDto ProductOrderMemoModifyRequestDtoToProductOrderMemoDto(int orderNo, ProductOrderMemoModifyRequestDto requestDto) {
        return ProductOrderMemoDto.builder()
                .orderNo(orderNo)
                .memoNo(requestDto.getMemoNo())
                .memo(requestDto.getMemo())
                .build();
    }
}
