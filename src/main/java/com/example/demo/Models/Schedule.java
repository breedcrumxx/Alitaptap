package com.example.demo.Models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(
    name = "schedule_seq",
    sequenceName = "schedule_seq",
    initialValue = 1, allocationSize = 1
)
public class Schedule {

    @Id        
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schedule_seq")
    @JsonProperty("id")
    private int Id;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    @JsonProperty("instructor")
    private Account Instructor;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    @JsonProperty("batch")
    private Batch batch;

    @JsonProperty("timeslot")
    private String TimeSlot;

    @JsonProperty("weekslot")
    private String WeekSlot;

    @JsonProperty("subjectcode")
    private String SubjectCode;

    @JsonProperty("subjectdescription")
    private String SubjectDescription;

    @Transient
    private List<Student> students;

    @Transient
    private List<Student> excluded;

    @Transient
    private List<Student> included;

    private int Status = 1;

    public Schedule(){

    }

    public int getInstructor(){
        return Instructor.getId();
    }


    @Override
    public String toString() {
        return "{" +
            " Id='" + getId() + "'" +
            ", BatchId='" + getBatch() + "'" +
            ", TimeSlot='" + getTimeSlot() + "'" +
            ", WeekSlot='" + getWeekSlot() + "'" +
            ", SubjectCode='" + getSubjectCode() + "'" +
            ", SubjectDescription='" + getSubjectDescription() + "'" +
            "}";
    }

    public String toJson() {
        return "{"+
            "\"id\":\"" + getId() + "\"" +
            "\"instructor\":\"" + getInstructor() + "\"" + 
            "\"batch\":\"" + getBatch() + "\"" +
            "\"timeslot\":\"" + getTimeSlot() + "\"" + 
            "\"weekslot\":\"" + getWeekSlot() + "\"" +
            "\"subjectcode\":\"" + getSubjectCode() + "\"" +
            
        "}";
    }

}
