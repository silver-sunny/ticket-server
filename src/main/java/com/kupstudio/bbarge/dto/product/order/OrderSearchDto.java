package com.kupstudio.bbarge.dto.product.order;

import com.kupstudio.bbarge.enumClass.product.CancelOrReturnFilterEnum;
import com.kupstudio.bbarge.enumClass.product.DetailedConditionEnum;
import com.kupstudio.bbarge.enumClass.product.InquiryPeriodEnum;
import com.kupstudio.bbarge.enumClass.product.ProductOrderFilterEnum;
import com.kupstudio.bbarge.utils.EmojiRemoveUtil;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OrderSearchDto {

    // 판매매장
    private Integer storeNo;

    // 조회기간
    private InquiryPeriodEnum inquiryPeriodEnum;

    // 조회 기간 시작 날자
    private String startDate;

    // 조회 기간 마지막 날짜
    private String endDate;

    // 주문상태
    private Integer orderStateNo;

    // 티켓 발급 여부
    private Boolean isIssuance;

    // 취소/반품 상태
    private Integer cancelOrReturnStateNo;

    // 상세조건
    private DetailedConditionEnum detailedConditionEnum;

    // 검색어
    private String searchWord;

    // 관리자 권한별 매장 리스트
    private Set<Integer> adminStoreNos;

    // 티켓 발급/취소 관리 리스트 조회용
    private List<Integer> orderStateNos;

    // 취소/반품 [요청/처리완료] 리스트 조회용
    private List<Integer> cancelOrReturnStateNos;

    // 취소/반품 [요청/처리완료] 리스트 정렬용
    private Integer cancelRequestStateNo;
    private Integer returnRequestStateNo;
    private Integer cancelDoneStateNo;
    private Integer returnDoneStateNo;

    // 페이지당 데이터 개수
    private int countPerPage;

    // 현재 페이지
    private int page;

    // 오프셋
    private int offSet;


    public OrderSearchDto paramsToOrderSearchDto(Integer storeNo, InquiryPeriodEnum inquiryPeriodEnum, LocalDate startDate, LocalDate endDate, ProductOrderFilterEnum orderFilterEnum, Boolean isIssuance, CancelOrReturnFilterEnum cancelOrReturnFilterEnum, DetailedConditionEnum detailedConditionEnum, String searchWord, int countPerPage, int page) {

        String startDateString = null;
        String endDateString = null;

        // 조회기간 카테고리가 선택되어 있으면서
        if (inquiryPeriodEnum != null) {
            LocalDateTime startDateTime = null;
            LocalDateTime endDateTime = null;

            // 입력된 시작 날짜 값이 있다면 startDateTime 생성
            if (startDate != null) {
                startDateTime = startDate.atStartOfDay();
                // 입력된 마지막 날짜 값이 있다면 endDate 값으로 endDateTime 생성
                // 입력된 마직막 날짜 값이 없다면 startDate 값으로 endDateTime 생성
                endDateTime = Objects.requireNonNullElse(endDate, startDate).atTime(LocalTime.MAX);
            } else {
                // 시작 날짜가 없다면 검색 필터에서 제외
                inquiryPeriodEnum = null;
            }

            startDateString = String.valueOf(startDateTime);
            endDateString = String.valueOf(endDateTime);
        }

        // 주문상태 번호 셋팅
        Integer orderStateNo = null;
        if (orderFilterEnum != null && orderFilterEnum != ProductOrderFilterEnum.ALL) {
            orderStateNo = orderFilterEnum.getIndex();
        }

        // 취소/반품상태 번호 셋팅
        Integer cancelOrReturnStateNo = null;
        if (cancelOrReturnFilterEnum != null && cancelOrReturnFilterEnum != CancelOrReturnFilterEnum.ALL) {
            cancelOrReturnStateNo = cancelOrReturnFilterEnum.getStatusNo();
        }

        String validSearchWord = EmojiRemoveUtil.removeEmojiForFilter(searchWord);
        if (validSearchWord == null) {
            detailedConditionEnum = null;
        }

        return OrderSearchDto.builder()
                .storeNo(storeNo)
                .inquiryPeriodEnum(inquiryPeriodEnum)
                .startDate(startDateString)
                .endDate(endDateString)
                .orderStateNo(orderStateNo)
                .isIssuance(isIssuance)
                .cancelOrReturnStateNo(cancelOrReturnStateNo)
                .detailedConditionEnum(detailedConditionEnum)
                .searchWord(validSearchWord)
                .countPerPage(countPerPage)
                .page(page)
                .build();
    }

}
