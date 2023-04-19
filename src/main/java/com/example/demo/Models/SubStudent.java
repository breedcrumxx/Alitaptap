package com.example.demo.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class SubStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @JsonProperty("id")
    private int Id;

    @JsonProperty("student")
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "student_id")
    private Student student;

    @JsonProperty("include")
    @ManyToOne
    @JoinColumn(name = "include_to")
    private Schedule includeSchedule;

    @JsonProperty("exclude")
    @ManyToOne
    @JoinColumn(name = "exclude_to")
    private Schedule excludeSchedule;

    public SubStudent(){

    }
}
