package com.kupstudio.bbarge.dto.admin;

import com.kupstudio.bbarge.enumClass.admin.AdminRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "운영자 정보 DTO")
public class AdminDto {

    @Schema(description = "운영자 번호")
    private int adminNo;

    @Schema(description = "운영자 로그인할 아이디")
    private String adminId;

    @Schema(description = "운영자 권한")
    private AdminRoleEnum role;

    @Schema(description = "운영자 삭제 여부")
    private boolean isDelete;


    public String getRoleMeaning() {
        return role.getMeaning();
    }


}
