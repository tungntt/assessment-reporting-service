package com.xphr.reporting.ms.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class TimeRecordController {

    @GetMapping("/report/time-record")
    public String getTimeRecordReport(Authentication authentication) {
        log.info("User {} accessed into Time Record Report with roles {} ", authentication.getName(),
                authentication.getAuthorities());
        return "work_hours_report";
    }
}
