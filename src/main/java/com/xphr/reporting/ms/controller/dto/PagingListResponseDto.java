package com.xphr.reporting.ms.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PagingListResponseDto<T> {

    private Integer currentPage;

    private Integer pageSize;

    private Integer totalPage;

    private Long totalRecord;

    private List<T> data;
}
