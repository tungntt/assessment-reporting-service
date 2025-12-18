package com.xphr.reporting.ms.controller;

import com.xphr.reporting.ms.controller.dto.PagingListResponseDto;
import com.xphr.reporting.ms.controller.dto.TimeRecordReportDto;
import com.xphr.reporting.ms.service.TimeRecordReadService;
import com.xphr.reporting.ms.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TimeRecordController {

    private static final String ADMIN_ROLE = "ROLE_ADMIN";

    private final TimeRecordReadService timeRecordReadService;

    @GetMapping("/report/time-record")
    public String getTimeRecordReport( @RequestParam(defaultValue = "0", name = "page") int page,
                                       @RequestParam(defaultValue = "20", name = "size") int size,
                                       @RequestParam(required = false, name = "startDate")
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                       @RequestParam(required = false, name = "endDate")
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                       Authentication authentication,
                                       Model model) {

        if (startDate == null) {
            startDate = LocalDateTime.now().minusMonths(1);
        }
        if (endDate == null) {
            endDate = LocalDateTime.now();
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(ADMIN_ROLE::equals);

        PagingListResponseDto<TimeRecordReportDto> pageResult;
        if (isAdmin) {
            log.info("Admin user '{}' accessing full report", authentication.getName());
            pageResult = timeRecordReadService.getReportForAdmin(page, size, startDate, endDate);
        } else {
            log.info("Employee user '{}' accessing personal report", authentication.getName());
            pageResult = timeRecordReadService.getReportForEmployee(page, size,
                    startDate, endDate, StringUtils.capitalize(authentication.getName()));
        }

        model.addAttribute("reportData", pageResult.getData());
        model.addAttribute("pageMetaData", pageResult);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("startDateValue", DateTimeUtils.toString(startDate));
        model.addAttribute("endDateValue", DateTimeUtils.toString(endDate));
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("username", authentication.getName());

        return "work_hours_report";
    }
}
