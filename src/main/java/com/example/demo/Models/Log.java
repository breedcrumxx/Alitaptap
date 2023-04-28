package com.example.demo.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Log {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    //firstname
    @JsonProperty("firstname")
    private String Firstname;

    @JsonProperty("lastname")
    private String Lastname;

    @JsonProperty("middlename")
    private String Middlename;

    @JsonProperty("rfid")
    private String RFID;

    @JsonProperty("role")
    private String Role;

    @JsonProperty("datetime")
    private String Datetime;

    @JsonProperty("status")
    private String Status;

    @JsonProperty("type")
    private String Type;

    @JsonProperty("currclass")
    private String CurrentClass;

    @JsonProperty("currclass")
    private String CurrentInstructor;

    public Log(String Firstname, String Lastname, String Middlename, String RFID, String Role, String Datetime, String Status, String Type, String CurrentClass, String CurrentInstructor) {
        this.Firstname = Firstname;
        this.Lastname = Lastname;
        this.Middlename = Middlename;
        this.RFID = RFID;
        this.Role = Role;
        this.Datetime = Datetime;
        this.Status = Status;
        this.Type = Type;
        this.CurrentClass = CurrentClass;
        this.CurrentInstructor = CurrentInstructor;
    }

    public Log(){
        
    }

    @Override
    public String toString() {
        return "{" +
            " Id='" + getId() + "'" +
            ", Firstname='" + getFirstname() + "'" +
            ", Lastname='" + getLastname() + "'" +
            ", Middlename='" + getMiddlename() + "'" +
            ", RFID='" + getRFID() + "'" +
            ", Role='" + getRole() + "'" +
            ", Datetime='" + getDatetime() + "'" +
            ", Status='" + getStatus() + "'" +
            ", Type='" + getType() + "'" +
            ", CurrentClass='" + getCurrentClass() + "'" +
            ", CurrentInstructor='" + getCurrentInstructor() + "'" +
            "}";
    }




    //middlename
    //lastname
    //role
    //date time
    //type visit on schedule
    //currently using by

}
