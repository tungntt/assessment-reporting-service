package com.xphr.reporting.ms.repository.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TimeRecordReportModel {

    private String employeeName;
    private String projectName;
    private Double totalHours;

    public TimeRecordReportModel(String employeeName, String projectName, Double totalHours) {
        this.employeeName = employeeName;
        this.projectName = projectName;
        this.totalHours = totalHours;
    }

    public String getFormattedHours() {
        if (totalHours == null) return "0.00";
        return String.format("%.2f", totalHours);
    }
}
