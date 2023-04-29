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

    private String FirstName;
    private String LastName;
    private String MiddleName;
    private String DateTime;
    private String Status;
    private String Batch;
    private String rfid;
    private int Schedule;
    private String CurrentClass;
    private String CurrentInstructor;

    public Attendance(String FirstName, String LastName, String MiddleName, String DateTime, String Status, String Batch, String rfid, int Schedule, String CurrentClass, String CurrentInstructor) {
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.MiddleName = MiddleName;
        this.DateTime = DateTime;
        this.Status = Status;
        this.Batch = Batch;
        this.rfid = rfid;
        this.Schedule = Schedule;
        this.CurrentClass = CurrentClass;
        this.CurrentInstructor = CurrentInstructor;
    }

    public Attendance(){
        
    }
  
}
