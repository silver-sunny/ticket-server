package com.kupstudio.bbarge.dto.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductChannelDto {
    private int storeNo;
    private int productNo;
    private Long naverProductNo;
    private Long coupangProductNo;
    private Long wemakepriceProductNo;
    private Long interparkProductNo;
    private Long tmonProductNo;

//    /**
//     * 디테일 담는 객체
//     */
//    public List<ChannelDetailDto> toChannelDetailList(String channelList) {
//
//        String channelFormat = channelList.replace("[", "").replace("]", "");
//        String[] list = channelFormat.split(",");
//
//        List<ChannelDetailDto> resultList = new ArrayList<ChannelDetailDto>();
//
//        for (String channel : list)
////        switch ()
//
//            resultList.add(new ChannelDetailDto(ChannelEnum.NEVERLAND_HC, ChannelEnum.NEVERLAND_HC.getMeaning(), "http://neverland/" + productNo));
//
//        if (naverProductNo != null) {
//            resultList.add(new ChannelDetailDto(ChannelEnum.NAVER, ChannelEnum.NAVER.getMeaning(), "https://shoppinglive.naver.com/home")); // TODO 네이버 넣어야 함
//        }
//        if (coupangProductNo != null) {
//            resultList.add(new ChannelDetailDto(ChannelEnum.COUPANG, ChannelEnum.COUPANG.getMeaning(), "https://pages.coupang.com/f/s17095?from=home_C2&traid=home_C2&trcid=11322364")); // TODO 네이버 넣어야 함
//        }
//        if (wemakepriceProductNo != null) {
//            resultList.add(new ChannelDetailDto(ChannelEnum.WEMAKEPRICE, ChannelEnum.WEMAKEPRICE.getMeaning(), "https://www.wemakeprice.com/main/100500?source=todaypick&no=1")); // TODO 네이버 넣어야 함
//        }
//        if (interparkProductNo != null) {
//            resultList.add(new ChannelDetailDto(ChannelEnum.INTERPARK, ChannelEnum.INTERPARK.getMeaning(), "https://events.interpark.com/exhibition?exhibitionCode=210226008")); // TODO 네이버 넣어야 함
//        }
//        if (tmonProductNo != null) {
//            resultList.add(new ChannelDetailDto(ChannelEnum.TMON, ChannelEnum.TMON.getMeaning(), "https://www.tmon.co.kr/deal/24694464322")); // TODO 네이버 넣어야 함
//        }
//
//        return resultList;
//    }

}

