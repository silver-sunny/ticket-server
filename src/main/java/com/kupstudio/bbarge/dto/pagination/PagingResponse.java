package com.kupstudio.bbarge.dto.pagination;

import lombok.Getter;

import java.util.List;

@Getter
public class PagingResponse<T> {

    private List<T> content;
    private T pageHelperDto;

    public PagingResponse(List<T> content, T pageHelperDto) {
        this.content = content;
        this.pageHelperDto = pageHelperDto;
    }


}