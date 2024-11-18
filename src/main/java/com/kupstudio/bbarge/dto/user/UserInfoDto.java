package com.kupstudio.bbarge.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDto {

    @Schema(description = "유저 번호")
    private int userNo;

    @Schema(description = "유저 아이디(이메일)")
    private String userId;

    @Schema(description = "유저 이름")
    private String name;

    @Schema(description = "유저 닉네임")
    private String nickName;

    @Schema(description = "유저 전화번호")
    private String phoneNumber;
}
