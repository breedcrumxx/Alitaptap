package com.example.demo.Models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Schedule {

    @Id        
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private int Id;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    @JsonProperty("instructor")
    private Account Instructor;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
    @ManyToOne
    @JoinColumn(name = "batch_id")
    @JsonProperty("batch")
    private Batch batch;

    @JsonProperty("start_at")
    private String StartAt;

    @JsonProperty("end_at")
    private String EndAt;

    @JsonProperty("weekslot")
    private String WeekSlot;

    @JsonProperty("subjectname")
    private String SubjectName;

    @JsonProperty("subjectcode")
    private String SubjectCode;

    @JsonProperty("subjectdescription")
    private String SubjectDescription;

    // @Transient
    // private List<Student> students;

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
            ", Instructor='" + getInstructor() + "'" +
            ", batch='" + getBatch() + "'" +
            ", StartAt='" + getStartAt() + "'" +
            ", EndAt='" + getEndAt() + "'" +
            ", WeekSlot='" + getWeekSlot() + "'" +
            ", SubjectName='" + getSubjectName() + "'" +
            ", SubjectCode='" + getSubjectCode() + "'" +
            ", SubjectDescription='" + getSubjectDescription() + "'" +
            ", Status='" + getStatus() + "'" +
            "}";
    }

    // public String toJson() {
    //     return "{"+
    //         "\"id\":\"" + getId() + "\"" +
    //         "\"instructor\":\"" + getInstructor() + "\"" + 
    //         "\"batch\":\"" + getBatch() + "\"" +
    //         "\"timeslot\":\"" + getTimeSlot() + "\"" + 
    //         "\"weekslot\":\"" + getWeekSlot() + "\"" +
    //         "\"subjectcode\":\"" + getSubjectCode() + "\"" +
            
    //     "}";
    // }

}
