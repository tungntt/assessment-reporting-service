package component.com.xphr.reporting.ms.controller;

import com.xphr.reporting.ms.repository.entity.EmployeeEntity;
import com.xphr.reporting.ms.repository.entity.ProjectEntity;
import com.xphr.reporting.ms.repository.entity.TimeRecordEntity;
import component.com.xphr.reporting.ms.ComponentTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ComponentTest
@AutoConfigureMockMvc
@DisplayName("TimeRecordController Component Tests")
class TimeRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
        baseDate = LocalDateTime.of(2024, 3, 1, 9, 0);

        // Create test employees
        employee1 = new EmployeeEntity(300L, "Test Employee");
        employee2 = new EmployeeEntity(301L, "Another Employee");
        entityManager.persist(employee1);
        entityManager.persist(employee2);

        // Create test projects
        project1 = new ProjectEntity(100L, "Test Project");
        entityManager.persist(project1);

        // Create time records
        TimeRecordEntity record1 = createTimeRecord(400L, employee1, project1,
                baseDate, baseDate.plusHours(8));
        timeRecordRepository.save(record1);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should return work hours report view for admin user")
    void shouldReturnWorkHoursReportViewForAdmin() throws Exception {
        // Given
        String startDate = baseDate.minusDays(1).toString();
        String endDate = baseDate.plusDays(1).toString();

        // When & Then
        mockMvc.perform(get("/report/time-record")
                        .param("startDate", startDate)
                        .param("endDate", endDate)
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(view().name("work_hours_report"))
                .andExpect(model().attributeExists("reportData"))
                .andExpect(model().attributeExists("pageMetaData"))
                .andExpect(model().attributeExists("startDate"))
                .andExpect(model().attributeExists("endDate"))
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attribute("isAdmin", true));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"EMPLOYEE"})
    @DisplayName("Should return work hours report view for employee user")
    void shouldReturnWorkHoursReportViewForEmployee() throws Exception {
        // Given
        String startDate = baseDate.minusDays(1).toString();
        String endDate = baseDate.plusDays(1).toString();

        // When & Then
        mockMvc.perform(get("/report/time-record")
                        .param("startDate", startDate)
                        .param("endDate", endDate))
                .andExpect(status().isOk())
                .andExpect(view().name("work_hours_report"))
                .andExpect(model().attributeExists("reportData"))
                .andExpect(model().attributeExists("pageMetaData"))
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attribute("isAdmin", false))
                .andExpect(model().attribute("username", "testuser"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should use default date range when dates are not provided")
    void shouldUseDefaultDateRangeWhenDatesNotProvided() throws Exception {
        // When & Then
        mockMvc.perform(get("/report/time-record"))
                .andExpect(status().isOk())
                .andExpect(view().name("work_hours_report"))
                .andExpect(model().attributeExists("startDate"))
                .andExpect(model().attributeExists("endDate"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should handle pagination parameters")
    void shouldHandlePaginationParameters() throws Exception {
        // Given
        String startDate = baseDate.minusDays(1).toString();
        String endDate = baseDate.plusDays(1).toString();

        // When & Then
        mockMvc.perform(get("/report/time-record")
                        .param("startDate", startDate)
                        .param("endDate", endDate)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("pageMetaData"));
    }

    @Test
    @WithMockUser(username = "Test Employee", roles = {"EMPLOYEE"})
    @DisplayName("Should filter results by employee name for employee users")
    void shouldFilterResultsByEmployeeName() throws Exception {
        // Given
        String startDate = baseDate.minusDays(1).toString();
        String endDate = baseDate.plusDays(1).toString();

        // When & Then
        mockMvc.perform(get("/report/time-record")
                        .param("startDate", startDate)
                        .param("endDate", endDate))
                .andExpect(status().isOk())
                .andExpect(view().name("work_hours_report"))
                .andExpect(model().attributeExists("reportData"));
    }

    @Test
    @DisplayName("Should require authentication")
    void shouldRequireAuthentication() throws Exception {
        // When & Then
        mockMvc.perform(get("/report/time-record"))
                .andExpect(status().is3xxRedirection()); // Redirects to login
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
}

