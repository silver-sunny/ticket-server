package com.kupstudio.bbarge.requestDto.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kupstudio.bbarge.enumClass.admin.AdminRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "운영자 계정 추가 DTO")
public class AdminRequestDto {

    @Schema(description = "운영자 고유 번호")
    @JsonIgnore
    private Integer adminNo;

    @Schema(description = "운영자 로그인할 아이디")
    private String adminId;

    @Schema(description = "운영자 로그인할 비밀번호 | 안넣을 경우 default 1111 ")
    @JsonIgnore
    private String password;

    @Schema(description = "운영자 권한")
    @JsonIgnore
    private AdminRoleEnum role;

    @Schema(description = "등록할 매장")
    private List<Integer> storeNoList;
}
