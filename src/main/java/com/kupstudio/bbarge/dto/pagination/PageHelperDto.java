package com.kupstudio.bbarge.dto.pagination;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PageHelperDto {


    @Schema(description = "페이지당 출력할 데이터 개수")
    private int countPerPage;

    @Schema(description = "현재 페이지 번호")
    private int page;

    @Schema(description = "전체 데이터 수")
    private int totalCount;

    @Schema(description = "쿼리를 출력한 시간")
    private LocalDateTime queryTime;

    @Schema(description = "페이지네이션 정보")
    private PaginationDto pagination;

    public PageHelperDto(int countPerPage, int page, int totalCount, LocalDateTime queryTime) {
        this.countPerPage = countPerPage;
        this.page = page;
        this.totalCount = totalCount;
        this.queryTime = queryTime;
        PaginationDto paginationDto = new PaginationDto(this);
        this.pagination = paginationDto;
    }

}