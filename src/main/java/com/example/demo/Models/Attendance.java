package com.example.demo.Models;

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
public class Attendance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonProperty("student")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    @JsonProperty("batch")
    private Batch batch;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    @JsonProperty("schedule")
    private Schedule schedule;

    public Attendance(){
        
    }
}
