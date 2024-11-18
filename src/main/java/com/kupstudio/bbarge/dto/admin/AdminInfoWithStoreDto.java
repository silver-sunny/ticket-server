package com.kupstudio.bbarge.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "운영자 정보랑 매장 출력 DTO")
public class AdminInfoWithStoreDto {

    @Schema(description = "운영자 정보")
    private AdminDto adminDto;


    @Schema(description = "운영자 관리 매장")
    private List<AdminStoreInfoDto> storeList;


}


