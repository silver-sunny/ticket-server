package com.kupstudio.bbarge.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Schema(description = "매장 운영자 DTO")
public class AdminStoreDto {

    @Schema(description = "운영자 고유 번호")
    private int adminNo;

    @Schema(description = "운영자 ID")
    private String adminId;

    @Schema(description = "매장번호")
    private List<Integer> storeNoList;

}
