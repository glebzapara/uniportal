package com.glebzapara.test_project.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "subjects")
@Data
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subjects_id_seq")
    @Column(name = "id")
    private Integer id;

    @Column(name = "name_subject")
    private String name_subject;

    @Column(name = "course")
    private short course;
}
