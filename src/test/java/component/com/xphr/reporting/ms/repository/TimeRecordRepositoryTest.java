package component.com.xphr.reporting.ms.repository;

import com.xphr.reporting.ms.repository.TimeRecordRepository;
import com.xphr.reporting.ms.repository.entity.EmployeeEntity;
import com.xphr.reporting.ms.repository.entity.ProjectEntity;
import com.xphr.reporting.ms.repository.entity.TimeRecordEntity;
import com.xphr.reporting.ms.repository.model.TimeRecordReportModel;
import component.com.xphr.reporting.ms.ComponentTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ComponentTest
@DisplayName("TimeRecordRepository Component Tests")
class TimeRecordRepositoryTest {

    @Autowired
    private TimeRecordRepository repository;

    @Autowired
    private jakarta.persistence.EntityManager entityManager;

    private EmployeeEntity employee1;
    private EmployeeEntity employee2;
    private ProjectEntity project1;
    private ProjectEntity project2;
    private LocalDateTime baseDate;

    @BeforeEach
    void setUp() {
        baseDate = LocalDateTime.of(2024, 1, 15, 10, 0);

        // Create test employees
        employee1 = new EmployeeEntity(1L, "John Doe");
        employee2 = new EmployeeEntity(2L, "Jane Smith");
        entityManager.persist(employee1);
        entityManager.persist(employee2);

        // Create test projects
        project1 = new ProjectEntity(1L, "Project Alpha");
        project2 = new ProjectEntity(2L, "Project Beta");
        entityManager.persist(project1);
        entityManager.persist(project2);
        entityManager.flush();

        // Create test time records
        // Employee 1 - Project 1: 8 hours on Jan 15
        TimeRecordEntity record1 = createTimeRecord(1L, employee1, project1,
                baseDate, baseDate.plusHours(8));

        // Employee 1 - Project 2: 4 hours on Jan 15
        TimeRecordEntity record2 = createTimeRecord(2L, employee1, project2,
                baseDate, baseDate.plusHours(4));

        // Employee 2 - Project 1: 6 hours on Jan 16
        TimeRecordEntity record3 = createTimeRecord(3L, employee2, project1,
                baseDate.plusDays(1), baseDate.plusDays(1).plusHours(6));

        // Employee 2 - Project 2: 10 hours on Jan 17
        TimeRecordEntity record4 = createTimeRecord(4L, employee2, project2,
                baseDate.plusDays(2), baseDate.plusDays(2).plusHours(10));

        // Out of range record (should not appear in results)
        TimeRecordEntity record5 = createTimeRecord(5L, employee1, project1,
                baseDate.minusDays(10), baseDate.minusDays(10).plusHours(5));

        repository.saveAll(List.of(record1, record2, record3, record4, record5));
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Should retrieve all report data for admin view with date range")
    void shouldRetrieveAllReportDataForAdmin() {
        // Given
        LocalDateTime startDate = baseDate.minusDays(1);
        LocalDateTime endDate = baseDate.plusDays(3);
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<TimeRecordReportModel> result = repository.getReportData(startDate, endDate, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(4); // 4 unique employee-project combinations
        assertThat(result.getTotalElements()).isEqualTo(4);
        assertThat(result.getTotalPages()).isEqualTo(1);

        // Verify grouping and aggregation
        TimeRecordReportModel johnProject1 = findRecord(result.getContent(), "John Doe", "Project Alpha");
        assertThat(johnProject1).isNotNull();
        assertThat(johnProject1.getTotalHours()).isEqualTo(8.0);

        TimeRecordReportModel johnProject2 = findRecord(result.getContent(), "John Doe", "Project Beta");
        assertThat(johnProject2).isNotNull();
        assertThat(johnProject2.getTotalHours()).isEqualTo(4.0);

        TimeRecordReportModel janeProject1 = findRecord(result.getContent(), "Jane Smith", "Project Alpha");
        assertThat(janeProject1).isNotNull();
        assertThat(janeProject1.getTotalHours()).isEqualTo(6.0);

        TimeRecordReportModel janeProject2 = findRecord(result.getContent(), "Jane Smith", "Project Beta");
        assertThat(janeProject2).isNotNull();
        assertThat(janeProject2.getTotalHours()).isEqualTo(10.0);
    }

    @Test
    @DisplayName("Should retrieve report data for specific employee")
    void shouldRetrieveReportDataForSpecificEmployee() {
        // Given
        LocalDateTime startDate = baseDate.minusDays(1);
        LocalDateTime endDate = baseDate.plusDays(3);
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<TimeRecordReportModel> result = repository.getReportDataByEmployee(
                startDate, endDate, "John Doe", pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2); // Only John's records
        assertThat(result.getTotalElements()).isEqualTo(2);

        // Verify only John's records are returned
        assertThat(result.getContent()).allMatch(record -> "John Doe".equals(record.getEmployeeName()));

        TimeRecordReportModel johnProject1 = findRecord(result.getContent(), "John Doe", "Project Alpha");
        assertThat(johnProject1.getTotalHours()).isEqualTo(8.0);

        TimeRecordReportModel johnProject2 = findRecord(result.getContent(), "John Doe", "Project Beta");
        assertThat(johnProject2.getTotalHours()).isEqualTo(4.0);
    }

    @Test
    @DisplayName("Should support pagination")
    void shouldSupportPagination() {
        // Given
        LocalDateTime startDate = baseDate.minusDays(1);
        LocalDateTime endDate = baseDate.plusDays(3);
        Pageable pageable = PageRequest.of(0, 2); // Page size of 2

        // When
        Page<TimeRecordReportModel> firstPage = repository.getReportData(startDate, endDate, pageable);
        Page<TimeRecordReportModel> secondPage = repository.getReportData(startDate, endDate, 
                PageRequest.of(1, 2));

        // Then
        assertThat(firstPage.getContent()).hasSize(2);
        assertThat(firstPage.getTotalPages()).isEqualTo(2);
        assertThat(firstPage.getTotalElements()).isEqualTo(4);
        assertThat(firstPage.isFirst()).isTrue();
        assertThat(firstPage.hasNext()).isTrue();

        assertThat(secondPage.getContent()).hasSize(2);
        assertThat(secondPage.isLast()).isTrue();
        assertThat(secondPage.hasPrevious()).isTrue();
    }

    @Test
    @DisplayName("Should exclude records outside date range")
    void shouldExcludeRecordsOutsideDateRange() {
        // Given - only records from baseDate onwards
        LocalDateTime startDate = baseDate;
        LocalDateTime endDate = baseDate.plusDays(3);
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<TimeRecordReportModel> result = repository.getReportData(startDate, endDate, pageable);

        // Then
        // Should still get 4 records (the one before baseDate is excluded)
        assertThat(result.getContent()).hasSize(4);
    }

    @Test
    @DisplayName("Should return empty page when no records match date range")
    void shouldReturnEmptyPageWhenNoRecordsMatch() {
        // Given
        LocalDateTime startDate = baseDate.plusDays(100);
        LocalDateTime endDate = baseDate.plusDays(101);
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<TimeRecordReportModel> result = repository.getReportData(startDate, endDate, pageable);

        // Then
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);
        assertThat(result.getTotalPages()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should return empty page when employee not found")
    void shouldReturnEmptyPageWhenEmployeeNotFound() {
        // Given
        LocalDateTime startDate = baseDate.minusDays(1);
        LocalDateTime endDate = baseDate.plusDays(3);
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<TimeRecordReportModel> result = repository.getReportDataByEmployee(
                startDate, endDate, "NonExistent Employee", pageable);

        // Then
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);
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

    private TimeRecordReportModel findRecord(List<TimeRecordReportModel> records, 
                                            String employeeName, String projectName) {
        return records.stream()
                .filter(r -> employeeName.equals(r.getEmployeeName()) 
                        && projectName.equals(r.getProjectName()))
                .findFirst()
                .orElse(null);
    }
}

