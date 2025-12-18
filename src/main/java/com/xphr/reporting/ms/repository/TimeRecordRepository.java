package com.xphr.reporting.ms.repository;

import com.xphr.reporting.ms.repository.entity.TimeRecordEntity;
import com.xphr.reporting.ms.repository.model.TimeRecordReportModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TimeRecordRepository extends JpaRepository<TimeRecordEntity, Long> {

    /**
     * Get report data for all employees (ADMIN view)
     */
    @Query("""
            SELECT new com.xphr.reporting.ms.repository.model.TimeRecordReportModel(e.name, p.name,
                   CAST(SUM((EXTRACT(EPOCH FROM tr.timeTo) - EXTRACT(EPOCH FROM tr.timeFrom)) / 3600.0) AS double))
            FROM TimeRecordEntity tr
            JOIN tr.employee e
            JOIN tr.project p
            WHERE tr.timeFrom >= :startDate AND tr.timeTo <= :endDate
            GROUP BY e.name, p.name
            ORDER BY e.name, p.name
            """)
    Page<TimeRecordReportModel> getReportData(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate,
                                              Pageable pageable);

    /**
     * Get report data for a specific employee (EMPLOYEE view)
     */
    @Query("""
            SELECT new com.xphr.reporting.ms.repository.model.TimeRecordReportModel(e.name, p.name,
                   CAST(SUM((EXTRACT(EPOCH FROM tr.timeTo) - EXTRACT(EPOCH FROM tr.timeFrom)) / 3600.0) AS double))
            FROM TimeRecordEntity tr
            JOIN tr.employee e
            JOIN tr.project p
            WHERE tr.timeFrom >= :startDate AND tr.timeTo <= :endDate
              AND e.name = :name
            GROUP BY e.name, p.name
            ORDER BY e.name, p.name
            """)
    Page<TimeRecordReportModel> getReportDataByEmployee(@Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate,
                                            @Param("name") String name, Pageable pageable);
}

