package com.xphr.reporting.ms.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity {

    @Id
    private Long id;

    @Column(length = 60)
    private String name;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<TimeRecordEntity> timeRecords = new ArrayList<>();

    public EmployeeEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
