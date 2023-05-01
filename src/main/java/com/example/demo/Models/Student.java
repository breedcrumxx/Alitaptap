package com.example.demo.Models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @JsonProperty("id")
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
    @OneToOne
    @JoinColumn(name = "rfid_id")
    @JsonBackReference
    private RFID StudentRFID;
    
    public Student(){

    }

    @Override
    public String toString() {
        return "{" +
            " Id='" + getId() + "'" +
            ", FirstName='" + getFirstName() + "'" +
            ", LastName='" + getLastName() + "'" +
            ", MiddleName='" + getMiddleName() + "'" +
            ", StudentId='" + getStudentId() + "'" +
            ", Address='" + getAddress() + "'" +
            ", BatchId='" + getBatchId() + "'" +
            ", StudentRFID='" + getStudentRFID() + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Student)) {
            return false;
        }
        Student student = (Student) o;
        return Id == student.Id && Objects.equals(FirstName, student.FirstName) && Objects.equals(LastName, student.LastName) && Objects.equals(MiddleName, student.MiddleName) && Objects.equals(StudentId, student.StudentId) && Objects.equals(Address, student.Address) && Objects.equals(BatchId, student.BatchId) && Objects.equals(StudentRFID, student.StudentRFID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, FirstName, LastName, MiddleName, StudentId, Address, BatchId, StudentRFID);
    }

}
