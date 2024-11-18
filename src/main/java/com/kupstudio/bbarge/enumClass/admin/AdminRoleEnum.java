package com.kupstudio.bbarge.enumClass.admin;

import lombok.Getter;

@Getter
public enum AdminRoleEnum {

    ROLE_ROOT("최상위운영자"),
    ROLE_MIDDLE("중간운영자"),
    ROLE_STORE("하위운영자");


    private final String meaning;

    AdminRoleEnum(String meaning) {
        this.meaning = meaning;

    }
}
