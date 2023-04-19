package com.example.demo.Models;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;
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
    private Batch BatchId;

    @JsonProperty("rfid")
    private String RFID;

    @JsonProperty("status")
    private String status;

    @Transient
    @OneToMany(mappedBy = "students")
    @JsonManagedReference
    private List<SubStudent> subStudent;
    
    public Student(){

    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Student)) {
            return false;
        }
        Student student = (Student) o;
        return Id == student.Id && Objects.equals(FirstName, student.FirstName) && Objects.equals(LastName, student.LastName) && Objects.equals(MiddleName, student.MiddleName) && Objects.equals(StudentId, student.StudentId) && Objects.equals(BatchId, student.BatchId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, FirstName, LastName, MiddleName, StudentId, BatchId);
    }

    @Override
    public String toString() {
        return "{" +
            " Id='" + getId() + "'" +
            ", FirstName='" + getFirstName() + "'" +
            ", LastName='" + getLastName() + "'" +
            ", MiddleName='" + getMiddleName() + "'" +
            ", StudentId='" + getStudentId() + "'" +
            ", GroupId='" + getBatchId() + "'" +
            "}";
    }


}
