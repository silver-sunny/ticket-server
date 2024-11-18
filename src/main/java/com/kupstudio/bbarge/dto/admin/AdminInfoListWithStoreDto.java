package com.kupstudio.bbarge.dto.admin;

import com.kupstudio.bbarge.enumClass.admin.AdminRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "운영자 정보랑 매장 출력 DTO")
public class AdminInfoListWithStoreDto {

    @Schema(description = "운영자 번호")
    private int adminNo;

    @Schema(description = "운영자 로그인할 아이디")
    private String adminId;

    @Schema(description = "운영자 권한")
    private AdminRoleEnum role;

    @Schema(description = "삭제된 운영자인지 여부")
    private boolean isDelete;

    @Schema(description = "운영자 관리 매장")
    private List<AdminStoreInfoDto> storeList;

    public String getRoleMeaning() {
        return role.getMeaning();
    }


}


