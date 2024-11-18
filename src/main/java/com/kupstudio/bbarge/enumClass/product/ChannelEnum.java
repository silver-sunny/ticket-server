package com.kupstudio.bbarge.enumClass.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.kupstudio.bbarge.dto.product.ChannelDetailDto;
import com.kupstudio.bbarge.dto.product.ProductDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Getter
public enum ChannelEnum {
    ETC(0, "", "", "", ""),
    NEVERLAND_HC(1, "네버랜드HC", "NEVERLAND_HC", "http://neverland/", "https://devbbarge.kr.object.ncloudstorage.com/storage/common/neverland_hc.svg"),
    NAVER(2, "네이버", "NAVER", "https://smartstore.naver.com/ticketdd22/products/", "https://devbbarge.kr.object.ncloudstorage.com/storage/common/naver.svg"),
    COUPANG(3, "쿠팡", "COUPANG", "https://pages.coupang.com/f/s17095?from=home_C2&traid=home_C2&trcid=11322364", "https://devbbarge.kr.object.ncloudstorage.com/storage/common/coupang.svg"),
    WEMAKEPRICE(4, "위마켓", "WEMAKEPRICE", "https://www.wemakeprice.com/main/100500?source=todaypick&no=1", "https://devbbarge.kr.object.ncloudstorage.com/storage/common/wemakeprice.svg"),
    INTERPARK(5, "인터파크", "INTERPARK", "https://events.interpark.com/exhibition?exhibitionCode=210226008", "https://devbbarge.kr.object.ncloudstorage.com/storage/common/interpark.svg"),
    TMON(6, "티몬", "TMON", "https://www.tmon.co.kr/deal/24694464322", "https://devbbarge.kr.object.ncloudstorage.com/storage/common/tmon.svg");

    public final int index;
    public final String meaning;
    public final String channel;
    public final String url;
    public final String icon;

    ChannelEnum(int index, String meaning, String channel, String url, String icon) {
        this.index = index;
        this.meaning = meaning;
        this.channel = channel;
        this.url = url;
        this.icon = icon;
    }

    // ChannelEnum 역직렬화 수동으로 처리 (ProductOrderRequestDto에서 유효한 enum을 입력받기 위해)
    @JsonCreator
    public static ChannelEnum parsing(String inputValue) {
        // 입력값이 ETC라면 null을 리턴하여 valid에 걸리게 하여 에러 메시지 출력
        if (ETC.name().equalsIgnoreCase(inputValue)) {
            return null;
        }

        // 입력값이 유효하지 않다면 null을 리턴하여 valid에 걸리게 하여 에러 메시지 출력
        return Stream.of(ChannelEnum.values())
                .filter(channelEnum -> channelEnum.toString().equals(inputValue.toUpperCase()))
                .findFirst()
                .orElse(null);
    }

    public static List<Map<Object, Object>> getSalesChannelEnumList() {
        List<Map<Object, Object>> list = new ArrayList<>();
        for (ChannelEnum value : ChannelEnum.values()) {

            if (value == ChannelEnum.ETC) {
                continue;
            }

            Map<Object, Object> channelMap = new HashMap<>();

            channelMap.put("index", value.index);
            channelMap.put("meaning", value.meaning);
            channelMap.put("channel", value.channel);
            channelMap.put("icon", value.icon);
            channelMap.put("enum", value);

            list.add(channelMap);
        }
        return list;
    }

    public static ChannelEnum getEnumOfChannel(String channel) {
        for (ChannelEnum thisEnum : ChannelEnum.values()) {
            if (thisEnum.channel.equals(channel)) return thisEnum;
        }
        return ETC;
    }

    public static ChannelEnum getEnumOfIndex(int index) {
        for (ChannelEnum thisEnum : ChannelEnum.values()) {
            if (thisEnum.index == index) return thisEnum;
        }

        return ETC;
    }

    public static ChannelDetailDto getChannelDetail(ProductDto productDto) {

        ChannelDetailDto channelDetailDto = null;

        ChannelEnum thisEnum = productDto.getChannel();
        switch (thisEnum) {
            case NEVERLAND_HC:
                // 자사는 앞에서 상품 등록이 완료된 상태여서 상품번호를 셋팅합니다.
                channelDetailDto = createChannelDetail(NEVERLAND_HC, productDto.getProductNo());
                break;
            case NAVER:
                channelDetailDto = createChannelDetail(NAVER, productDto.getChannelProductId());
                break;
            //TODO 나머지 소셜 추가
//            case COUPANG:
//                break;
//            case WEMAKEPRICE:
//                break;
//            case INTERPARK:
//                break;
//            case TMON:
//                break;
        }

        return channelDetailDto;
    }

    private static ChannelDetailDto createChannelDetail(ChannelEnum channelEnum, String channelProductId) {
        ChannelDetailDto channelDetailDto = new ChannelDetailDto();
        channelDetailDto.setChannelEnum(channelEnum);
        channelDetailDto.setName(channelEnum.meaning);
        channelDetailDto.setDetailUrl((channelProductId != null) ? channelEnum.url + channelProductId : null);
        channelDetailDto.setIcon(channelEnum.icon);
        return channelDetailDto;
    }

    private static ChannelDetailDto createChannelDetail(ChannelEnum channelEnum, int productNo) {
        ChannelDetailDto channelDetailDto = new ChannelDetailDto();
        channelDetailDto.setChannelEnum(channelEnum);
        channelDetailDto.setName(channelEnum.meaning);
        channelDetailDto.setDetailUrl(channelEnum.url + productNo);
        channelDetailDto.setIcon(channelEnum.icon);
        return channelDetailDto;
    }
}
