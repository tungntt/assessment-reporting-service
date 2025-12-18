package component.com.xphr.reporting.ms.mapper;

import com.xphr.reporting.ms.controller.dto.TimeRecordReportDto;
import com.xphr.reporting.ms.mapper.ReportMapper;
import com.xphr.reporting.ms.repository.model.TimeRecordReportModel;
import component.com.xphr.reporting.ms.ComponentTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@ComponentTest
@DisplayName("ReportMapper Component Tests")
class ReportMapperTest {

    @Autowired
    private ReportMapper mapper;

    @Test
    @DisplayName("Should map TimeRecordReportModel to TimeRecordReportDto correctly")
    void shouldMapModelToDto() {
        // Given
        TimeRecordReportModel model = new TimeRecordReportModel(
                "John Doe",
                "Project Alpha",
                8.5
        );

        // When
        TimeRecordReportDto dto = mapper.toTimeRecordReportDto(model);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getEmployeeName()).isEqualTo("John Doe");
        assertThat(dto.getProjectName()).isEqualTo("Project Alpha");
        assertThat(dto.getTotalHours()).isEqualTo(8.5);
    }

    @Test
    @DisplayName("Should map model with null values")
    void shouldMapModelWithNullValues() {
        // Given
        TimeRecordReportModel model = new TimeRecordReportModel(
                "Jane Smith",
                "Project Beta",
                null
        );

        // When
        TimeRecordReportDto dto = mapper.toTimeRecordReportDto(model);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getEmployeeName()).isEqualTo("Jane Smith");
        assertThat(dto.getProjectName()).isEqualTo("Project Beta");
        assertThat(dto.getTotalHours()).isNull();
    }

    @Test
    @DisplayName("Should map model with zero hours")
    void shouldMapModelWithZeroHours() {
        // Given
        TimeRecordReportModel model = new TimeRecordReportModel(
                "Bob Wilson",
                "Project Gamma",
                0.0
        );

        // When
        TimeRecordReportDto dto = mapper.toTimeRecordReportDto(model);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getTotalHours()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Should map model with large hours value")
    void shouldMapModelWithLargeHoursValue() {
        // Given
        TimeRecordReportModel model = new TimeRecordReportModel(
                "Alice Johnson",
                "Project Delta",
                999.99
        );

        // When
        TimeRecordReportDto dto = mapper.toTimeRecordReportDto(model);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getTotalHours()).isEqualTo(999.99);
    }

    @Test
    @DisplayName("Should preserve formatted hours when mapped")
    void shouldPreserveFormattedHours() {
        // Given
        TimeRecordReportModel model = new TimeRecordReportModel(
                "Charlie Brown",
                "Project Epsilon",
                12.75
        );

        // When
        TimeRecordReportDto dto = mapper.toTimeRecordReportDto(model);

        // Then
        assertThat(dto.getFormattedHours()).isEqualTo("12.75");
    }

    @Test
    @DisplayName("Should map multiple models correctly")
    void shouldMapMultipleModels() {
        // Given
        TimeRecordReportModel model1 = new TimeRecordReportModel("Employee 1", "Project 1", 5.5);
        TimeRecordReportModel model2 = new TimeRecordReportModel("Employee 2", "Project 2", 10.25);

        // When
        TimeRecordReportDto dto1 = mapper.toTimeRecordReportDto(model1);
        TimeRecordReportDto dto2 = mapper.toTimeRecordReportDto(model2);

        // Then
        assertThat(dto1.getEmployeeName()).isEqualTo("Employee 1");
        assertThat(dto1.getTotalHours()).isEqualTo(5.5);
        
        assertThat(dto2.getEmployeeName()).isEqualTo("Employee 2");
        assertThat(dto2.getTotalHours()).isEqualTo(10.25);
        
        // Verify they are different instances
        assertThat(dto1).isNotSameAs(dto2);
    }
}

