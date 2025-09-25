package com.glebzapara.test_project.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "subjects")
@Data
public class Subject {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name_subject")
    private String name_subject;
}
