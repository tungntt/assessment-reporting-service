package com.xphr.reporting.ms.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeRecordReportDto {

    private String employeeName;
    private String projectName;
    private Double totalHours;

    public String getFormattedHours() {
        if (totalHours == null) return "0.00";
        return String.format("%.2f", totalHours);
    }
}
