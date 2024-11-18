package com.kupstudio.bbarge.dto.pagination;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PaginationDto {

    @Schema(description = "이전 페이지 존재 여부")
    private boolean existPrevPage;

    @Schema(description = "다음 페이지 존재 여부")
    private boolean existNextPage;

    @Schema(description = "전체 페이지 수")
    private int totalPageCount;

    public PaginationDto(PageHelperDto params) {
        if (params.getTotalCount() > 0) {
            calculatePagination(params.getTotalCount(), params.getPage(), params.getCountPerPage());
        }
    }

    private void calculatePagination(int totalCount, int currentPage, int countPerPage) {
        // 전체 페이지 수 계산
        totalPageCount = (totalCount - 1) / countPerPage + 1;

        // 현재 페이지 번호가 전체 페이지 수보다 큰 경우, 현재 페이지 번호에 전체 페이지 수 저장
        if (currentPage > totalPageCount) {
            currentPage = totalPageCount;
        }

        // 이전 페이지 존재 여부 확인
        existPrevPage = currentPage > 1;

        // 다음 페이지 존재 여부 확인
        existNextPage = currentPage < totalPageCount;
    }
}