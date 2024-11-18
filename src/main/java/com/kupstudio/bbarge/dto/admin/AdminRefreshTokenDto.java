package com.kupstudio.bbarge.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "운영자 refreshToken DTO")
public class AdminRefreshTokenDto {

    @Schema(description = "운영자 고유 번호")
    private int adminNo;

    @Schema(description = "운영자 아이디")
    private String adminId;

    @Schema(description = "리프레시 토큰")
    private String refreshToken;

}
