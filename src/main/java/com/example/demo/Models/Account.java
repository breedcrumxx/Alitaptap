package com.example.demo.Models;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table( 
    uniqueConstraints = {
        @UniqueConstraint(
            name = "unique_username",
            columnNames = "Username"
        )
    }
)
@SequenceGenerator(
    name = "account_seq", 
    sequenceName = "account_seq", 
    initialValue = 1, allocationSize = 1
)  

public class Account {

    @Id 
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
    @JsonProperty("id")
    @JsonAlias("instructorId")
    private int Id;

    @JsonProperty("username")
    private String Username;

    @JsonProperty("password")
    private String Password;

    @JsonProperty("fullname")
    private String FullName;

    @JsonProperty("sex")
    private String Sex;

    @JsonProperty("role")
    private String Role = "";

    // @OneToMany(mappedBy = "Instructor")
    // private List<Schedule> schedules;

    @OneToOne
    @JoinColumn(name = "rfid_id")
    @JsonProperty("rfid")
    @JsonBackReference
    private RFID AccountRFID;

    public Account() {
    }

    public Account(int id) {
        this.Id = id;
    }

    public String toJson() {
        return "{" +
            "\"Id\":\"" + getId() + "\"" +
            ",\"Username\":\"" + getUsername() + "\"" +
            ",\"Password\":\"" + getPassword() + "\"" +
            ",\"Fullname\":\"" + getFullName() + "\"" +
            ",\"Sex\":\"" + getSex() + "\"" +
            ",\"Role\":\"" + getRole() + "\"" +
            "}";
    }

    // @Override
    // public int hashCode() {
    //     return Objects.hash(Id, Username, Password, FullName, Sex, Role, schedules);
    // }

    @Override
    public String toString() {
        return "{" +
            " Id='" + getId() + "'" +
            ", Username='" + getUsername() + "'" +
            ", Password='" + getPassword() + "'" +
            ", FullName='" + getFullName() + "'" +
            ", Sex='" + getSex() + "'" +
            ", Role='" + getRole() + "'" +
            "}";
    }


}
