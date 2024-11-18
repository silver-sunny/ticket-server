package com.kupstudio.bbarge.enumClass.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.kupstudio.bbarge.enumClass.product.naver.NaverProductStatusTypeEnum;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Getter
public enum ProductStateEnum {

    ETC(0, ""),
    SALE(10, "판매 중"),
    SUSPENSION(20, "판매 대기(중지)"),
    OUTOFSTOCK(30, "품절"),
    PROHIBITION(40, "판매 금지"),

    // 나올수도있고 안나올수도있음
    CLOSE(50, "판매 종료"),
    WAIT(60, "판매 대기"),
    UNADMISSION(70, "승인 대기"),
    REJECTION(80, "승인 거부"),
    DELETE(90, "삭제");

    public final Integer stateNo;
    public final String meaning;

    ProductStateEnum(int stateNo, String meaning) {
        this.stateNo = stateNo;
        this.meaning = meaning;
    }

    // ProductStateEnum 역직렬화 수동으로 처리 (ProductRequestDto에서 유효한 enum을 입력받기 위해)
    @JsonCreator
    public static ProductStateEnum parsing(String inputValue) {
        // 입력값이 ETC라면 null을 리턴하여 valid에 걸리게 하여 에러 메시지 출력
        if (ETC.name().equalsIgnoreCase(inputValue)) {
            return null;
        }

        // 입력값이 유효하지 않다면 null을 리턴하여 valid에 걸리게 하여 에러 메시지 출력
        return Stream.of(ProductStateEnum.values())
                .filter(productStateEnum -> productStateEnum.toString().equals(inputValue.toUpperCase()))
                .findFirst()
                .orElse(null);
    }

    public static List<Map<Object, Object>> getProductStateEnumListForInsertOrUpdate() {
        List<ProductStateEnum> filterEnumList = new ArrayList<>();
        filterEnumList.add(SALE);
        filterEnumList.add(SUSPENSION);

        List<Map<Object, Object>> list = new ArrayList<>();
        for (ProductStateEnum value : filterEnumList) {

            Map<Object, Object> stateMap = new HashMap<>();

            stateMap.put("stateNo", value.stateNo);
            stateMap.put("meaning", value.meaning);
            stateMap.put("enum", value);

            list.add(stateMap);
        }
        return list;
    }

    public static ProductStateEnum getEnumOfStateNo(int stateNo) {
        for (ProductStateEnum thisEnum : ProductStateEnum.values()) {
            if (thisEnum.stateNo == stateNo) return thisEnum;
        }
        return ETC;
    }

    public static ProductStateEnum getProductStateByNaverProductStatusEnum(NaverProductStatusTypeEnum naverProductStatusTypeEnum) {

        for (ProductStateEnum value : ProductStateEnum.values()) {
            if(naverProductStatusTypeEnum.name().equals(value.name())){
                return value;
            }
        }

        return ETC;
    }

    // 수정 가능한 상품상태인지 확인
    public static boolean isValidProductStateForModify(ProductStateEnum productStateEnum) {
        switch (productStateEnum){
            case SALE:
            case OUTOFSTOCK:
            case UNADMISSION:
            case SUSPENSION:
                return true;
            default:
                return false;
        }
    }
}
