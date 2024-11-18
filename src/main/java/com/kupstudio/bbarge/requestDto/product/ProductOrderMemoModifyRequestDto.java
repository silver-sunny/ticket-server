package com.kupstudio.bbarge.requestDto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ProductOrderMemoModifyRequestDto {

    @Schema(description = "메모번호")
    @Min(value = 1, message = "The minimum value of a memoNo is 1")
    private int memoNo;

    @Schema(description = "메모", example = " ")
    @Size(min = 1, max = 100, message = "The character limit for the 'memo' is 1 to 100.")
    private String memo;

}
