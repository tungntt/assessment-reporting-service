package com.xphr.reporting.ms.mapper;

import com.xphr.reporting.ms.controller.dto.TimeRecordReportDto;
import com.xphr.reporting.ms.repository.model.TimeRecordReportModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    TimeRecordReportDto toTimeRecordReportDto(TimeRecordReportModel source);
}
