package com.example.demo.Models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(
    name = "student_seq",
    sequenceName = "student_seq",
    initialValue = 1, allocationSize = 1
)
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_seq")
    private int Id;

    @JsonProperty("firstname")
    private String FirstName;

    @JsonProperty("lastname")
    private String LastName;

    @JsonProperty("middlename")
    private String MiddleName;

    @JsonProperty("studentid")
    private String StudentId;

    @JsonProperty("address")
    private String Address;

    @JsonProperty("batchid")
    @ManyToOne
    @JoinColumn(name = "batchId")
    @JsonBackReference
    private Batch BatchId;
    
    @JsonProperty("rfid")
    @OneToOne
    @JoinColumn(name = "rfid_id")
    private Rfid StudentRFID;
    
    public Student(){

    }

}
