package com.kupstudio.bbarge.enumClass.file;

import lombok.Getter;

@Getter
public enum FilePathTypeEnum {

    ETC(0, "", ""),
    PRI(1, "상품 대표 이미지", "pri"),
    PDI(2, "상품 상세 이미지", "pdi");

    public final int index;
    public final String meaning;
    public final String path;

    FilePathTypeEnum(int index, String meaning, String path) {
        this.index = index;
        this.meaning = meaning;
        this.path = path;
    }

    /**
     * meaning 과 일치하는 enum 반환
     *
     * @param meaning
     * @return
     */
    public static FilePathTypeEnum getEnumOfMeaning(String meaning) {
        for (FilePathTypeEnum thisEnum : FilePathTypeEnum.values()) {
            if (thisEnum.meaning.equals(meaning)) return thisEnum;
        }
        return ETC;
    }

    /**
     * 입력받은 path 를 포함 하고 있는 enum 반환
     *
     * @param path
     * @return
     */
    public static FilePathTypeEnum getEnumOfPath(String path) {

        if (path == null || path.isEmpty()) {
            return ETC;
        }

        for (FilePathTypeEnum thisEnum : FilePathTypeEnum.values()) {
            if (thisEnum == ETC) continue;
            if (thisEnum.path.contains(path) || path.contains(thisEnum.path)) return thisEnum;
        }
        return ETC;
    }

}