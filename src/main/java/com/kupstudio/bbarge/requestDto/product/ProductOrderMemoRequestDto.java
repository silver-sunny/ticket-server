package com.kupstudio.bbarge.requestDto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class ProductOrderMemoRequestDto {

    @Schema(description = "메모", example = " ")
    @Size(min = 1, max = 100, message = "The character limit for the 'memo' is 1 to 100.")
    private String memo;

}
