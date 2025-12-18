package com.xphr.reporting.ms.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project")
@Getter
@Setter
@NoArgsConstructor
public class ProjectEntity {

    @Id
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<TimeRecordEntity> timeRecords= new ArrayList<>();

    public ProjectEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
