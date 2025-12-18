package com.xphr.reporting.ms.service;

import com.xphr.reporting.ms.controller.dto.PagingListResponseDto;
import com.xphr.reporting.ms.controller.dto.TimeRecordReportDto;
import com.xphr.reporting.ms.mapper.ReportMapper;
import com.xphr.reporting.ms.repository.TimeRecordRepository;
import com.xphr.reporting.ms.repository.model.TimeRecordReportModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TimeRecordReadService {

    private final ReportMapper reportMapper;
    private final TimeRecordRepository repository;

    @Transactional(readOnly = true)
    public PagingListResponseDto<TimeRecordReportDto> getReportForAdmin(int page, int size,
                                                                        LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching report for ADMIN from {} to {}", startDate, endDate);
        long startTime = System.currentTimeMillis();

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<TimeRecordReportModel> pageResult = repository.getReportData(startDate, endDate, pageRequest);

        PagingListResponseDto<TimeRecordReportDto> result = populateTimeRecordReportDto(pageResult);

        long duration = System.currentTimeMillis() - startTime;
        log.info("Admin report query executed in {} ms, returned {} records", duration, result.getPageSize());

        return result;
    }

    @Transactional(readOnly = true)
    public PagingListResponseDto<TimeRecordReportDto> getReportForEmployee(int page, int size,
            LocalDateTime startDate, LocalDateTime endDate, String username) {
        log.info("Fetching report for EMPLOYEE '{}' from {} to {}", username, startDate, endDate);
        long startTime = System.currentTimeMillis();

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<TimeRecordReportModel> pageResult = repository.getReportDataByEmployee(startDate, endDate, username, pageRequest);
        PagingListResponseDto<TimeRecordReportDto> result = populateTimeRecordReportDto(pageResult);

        long duration = System.currentTimeMillis() - startTime;
        log.info("Employee report query executed in {} ms, returned {} records", duration, result.getPageSize());

        return result;
    }

    private PagingListResponseDto<TimeRecordReportDto> populateTimeRecordReportDto(Page<TimeRecordReportModel> pageResult) {
        List<TimeRecordReportDto> data = pageResult.getContent()
                .stream().map(reportMapper::toTimeRecordReportDto)
                .toList();
        return new PagingListResponseDto<>(pageResult.getNumber(),
                pageResult.getSize(), pageResult.getTotalPages(), pageResult.getTotalElements(), data);
    }
}
