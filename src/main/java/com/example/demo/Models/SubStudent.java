package com.example.demo.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private int StudentId;

    @JsonProperty("stat")
    private String Status;

    @JsonProperty("schedule")
    private int Schedule;

    public SubStudent(){

    }

    @Override
    public String toString() {
        return "{" +
            " Id='" + getId() + "'" +
            ", StudentId='" + getStudentId() + "'" +
            ", Status='" + getStatus() + "'" +
            ", Schedule='" + getSchedule() + "'" +
            "}";
    }

}
