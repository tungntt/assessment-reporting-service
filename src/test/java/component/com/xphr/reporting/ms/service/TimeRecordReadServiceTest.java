package component.com.xphr.reporting.ms.service;

import com.xphr.reporting.ms.controller.dto.PagingListResponseDto;
import com.xphr.reporting.ms.controller.dto.TimeRecordReportDto;
import com.xphr.reporting.ms.repository.entity.EmployeeEntity;
import com.xphr.reporting.ms.repository.entity.ProjectEntity;
import com.xphr.reporting.ms.repository.entity.TimeRecordEntity;
import com.xphr.reporting.ms.service.TimeRecordReadService;
import component.com.xphr.reporting.ms.ComponentTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ComponentTest
@DisplayName("TimeRecordReadService Component Tests")
class TimeRecordReadServiceTest {

    @Autowired
    private TimeRecordReadService service;

    @Autowired
    private com.xphr.reporting.ms.repository.TimeRecordRepository timeRecordRepository;

    @Autowired
    private jakarta.persistence.EntityManager entityManager;

    private EmployeeEntity employee1;
    private EmployeeEntity employee2;
    private ProjectEntity project1;
    private LocalDateTime baseDate;

    @BeforeEach
    void setUp() {
        baseDate = LocalDateTime.of(2024, 2, 1, 9, 0);

        // Create test employees
        employee1 = new EmployeeEntity(100L, "Alice Johnson");
        employee2 = new EmployeeEntity(101L, "Bob Wilson");
        entityManager.persist(employee1);
        entityManager.persist(employee2);

        // Create test projects
        project1 = new ProjectEntity(50L, "Website Redesign");
        ProjectEntity project2 = new ProjectEntity(51L, "Mobile App");
        entityManager.persist(project1);
        entityManager.persist(project2);
        entityManager.flush();

        // Create time records for Alice
        // Alice - Website Redesign: 8 hours
        TimeRecordEntity record1 = createTimeRecord(200L, employee1, project1,
                baseDate, baseDate.plusHours(8));

        // Alice - Website Redesign: 4 hours (same project, different day)
        TimeRecordEntity record2 = createTimeRecord(201L, employee1, project1,
                baseDate.plusDays(1), baseDate.plusDays(1).plusHours(4));

        // Bob - Website Redesign: 6 hours
        TimeRecordEntity record3 = createTimeRecord(202L, employee2, project1,
                baseDate, baseDate.plusHours(6));

        timeRecordRepository.saveAll(List.of(record1, record2, record3));
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Should get report for admin with correct pagination")
    void shouldGetReportForAdmin() {
        // Given
        LocalDateTime startDate = baseDate.minusDays(1);
        LocalDateTime endDate = baseDate.plusDays(3);
        int page = 0;
        int size = 20;

        // When
        PagingListResponseDto<TimeRecordReportDto> result = service.getReportForAdmin(
                page, size, startDate, endDate);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getData()).isNotEmpty();
        assertThat(result.getCurrentPage()).isEqualTo(0);
        assertThat(result.getPageSize()).isEqualTo(20);
        assertThat(result.getTotalRecord()).isGreaterThan(0);

        // Verify data structure
        TimeRecordReportDto aliceWebsite = findDto(result.getData(), "Alice Johnson", "Website Redesign");
        assertThat(aliceWebsite).isNotNull();
        assertThat(aliceWebsite.getTotalHours()).isEqualTo(12.0); // 8 + 4 hours

        TimeRecordReportDto bobWebsite = findDto(result.getData(), "Bob Wilson", "Website Redesign");
        assertThat(bobWebsite).isNotNull();
        assertThat(bobWebsite.getTotalHours()).isEqualTo(6.0);
    }

    @Test
    @DisplayName("Should get report for employee with correct filtering")
    void shouldGetReportForEmployee() {
        // Given
        LocalDateTime startDate = baseDate.minusDays(1);
        LocalDateTime endDate = baseDate.plusDays(3);
        int page = 0;
        int size = 20;
        String employeeName = "Alice Johnson";

        // When
        PagingListResponseDto<TimeRecordReportDto> result = service.getReportForEmployee(
                page, size, startDate, endDate, employeeName);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getData()).isNotEmpty();
        
        // Verify only Alice's records
        assertThat(result.getData()).allMatch(dto -> "Alice Johnson".equals(dto.getEmployeeName()));

        TimeRecordReportDto aliceWebsite = findDto(result.getData(), "Alice Johnson", "Website Redesign");
        assertThat(aliceWebsite).isNotNull();
        assertThat(aliceWebsite.getTotalHours()).isEqualTo(12.0);
    }

    @Test
    @DisplayName("Should handle pagination correctly for admin report")
    void shouldHandlePaginationForAdmin() {
        // Given
        LocalDateTime startDate = baseDate.minusDays(1);
        LocalDateTime endDate = baseDate.plusDays(3);
        int size = 1; // Small page size to test pagination

        // When
        PagingListResponseDto<TimeRecordReportDto> firstPage = service.getReportForAdmin(0, size, startDate, endDate);
        PagingListResponseDto<TimeRecordReportDto> secondPage = service.getReportForAdmin(1, size, startDate, endDate);

        // Then
        assertThat(firstPage.getData()).hasSize(1);
        assertThat(firstPage.getCurrentPage()).isEqualTo(0);
        assertThat(firstPage.getPageSize()).isEqualTo(1);
        assertThat(firstPage.getTotalPage()).isGreaterThanOrEqualTo(1);

        assertThat(secondPage.getData()).hasSize(1);
        assertThat(secondPage.getCurrentPage()).isEqualTo(1);
        assertThat(secondPage.getPageSize()).isEqualTo(1);

        // Verify different records on different pages
        assertThat(firstPage.getData().get(0)).isNotEqualTo(secondPage.getData().get(0));
    }

    @Test
    @DisplayName("Should return empty result when no records in date range")
    void shouldReturnEmptyResultWhenNoRecordsInRange() {
        // Given
        LocalDateTime startDate = baseDate.plusDays(100);
        LocalDateTime endDate = baseDate.plusDays(101);
        int page = 0;
        int size = 20;

        // When
        PagingListResponseDto<TimeRecordReportDto> result = service.getReportForAdmin(
                page, size, startDate, endDate);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getData()).isEmpty();
        assertThat(result.getTotalRecord()).isEqualTo(0);
        assertThat(result.getTotalPage()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should return empty result when employee has no records")
    void shouldReturnEmptyResultWhenEmployeeHasNoRecords() {
        // Given
        LocalDateTime startDate = baseDate.minusDays(1);
        LocalDateTime endDate = baseDate.plusDays(3);
        int page = 0;
        int size = 20;
        String employeeName = "NonExistent Employee";

        // When
        PagingListResponseDto<TimeRecordReportDto> result = service.getReportForEmployee(
                page, size, startDate, endDate, employeeName);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getData()).isEmpty();
        assertThat(result.getTotalRecord()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should aggregate hours correctly for same employee-project combination")
    void shouldAggregateHoursCorrectly() {
        // Given
        LocalDateTime startDate = baseDate.minusDays(1);
        LocalDateTime endDate = baseDate.plusDays(3);
        int page = 0;
        int size = 20;

        // When
        PagingListResponseDto<TimeRecordReportDto> result = service.getReportForAdmin(
                page, size, startDate, endDate);

        // Then
        TimeRecordReportDto aliceWebsite = findDto(result.getData(), "Alice Johnson", "Website Redesign");
        assertThat(aliceWebsite).isNotNull();
        // Should aggregate: 8 hours + 4 hours = 12 hours
        assertThat(aliceWebsite.getTotalHours()).isEqualTo(12.0);
    }

    @Test
    @DisplayName("Should calculate formatted hours correctly")
    void shouldCalculateFormattedHoursCorrectly() {
        // Given
        LocalDateTime startDate = baseDate.minusDays(1);
        LocalDateTime endDate = baseDate.plusDays(3);
        int page = 0;
        int size = 20;

        // When
        PagingListResponseDto<TimeRecordReportDto> result = service.getReportForAdmin(
                page, size, startDate, endDate);

        // Then
        TimeRecordReportDto aliceWebsite = findDto(result.getData(), "Alice Johnson", "Website Redesign");
        assertThat(aliceWebsite.getFormattedHours()).isEqualTo("12.00");
    }

    private TimeRecordEntity createTimeRecord(Long id, EmployeeEntity employee, ProjectEntity project,
                                             LocalDateTime timeFrom, LocalDateTime timeTo) {
        TimeRecordEntity record = new TimeRecordEntity();
        record.setId(id);
        record.setEmployee(employee);
        record.setProject(project);
        record.setTimeFrom(timeFrom);
        record.setTimeTo(timeTo);
        return record;
    }

    private TimeRecordReportDto findDto(List<TimeRecordReportDto> dtos, String employeeName, String projectName) {
        return dtos.stream()
                .filter(dto -> employeeName.equals(dto.getEmployeeName()) 
                        && projectName.equals(dto.getProjectName()))
                .findFirst()
                .orElse(null);
    }
}

