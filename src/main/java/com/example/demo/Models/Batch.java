package com.example.demo.Models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Data
@Table( 
    name = "batch",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "unique_section",
            columnNames = "section"
        )
    }
)
@SequenceGenerator(
    name = "batch_seq",
    sequenceName = "batch_seq",
    initialValue = 1, allocationSize = 1
)
public class Batch {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "batch_seq")
    @JsonProperty("id")
    @JsonManagedReference
    private int Id;

    @JsonProperty("course")
    private String Course;

    @JsonProperty("section")
    private String Section;

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Batch)) {
            return false;
        }
        Batch group = (Batch) o;
        return Id == group.Id && Objects.equals(Course, group.Course) && Objects.equals(Section, group.Section);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, Course, Section);
    }

    @Override
    public String toString() {
        return "{" +
            " Id='" + getId() + "'" +
            ", Course='" + getCourse() + "'" +
            ", Section='" + getSection() + "'" +
            "}";
    }

}
