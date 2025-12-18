package com.xphr.reporting.ms.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TimeRecordReportDto {

    private String employeeName;
    private String projectName;
    private Double totalHours;

    public TimeRecordReportDto(String employeeName, String projectName, Double totalHours) {
        this.employeeName = employeeName;
        this.projectName = projectName;
        this.totalHours = totalHours;
    }

    public String getFormattedHours() {
        if (totalHours == null) return "0.00";
        return String.format("%.2f", totalHours);
    }
}
