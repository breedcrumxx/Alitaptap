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

    @JsonProperty("firstname")
    private String Lastname;

    @JsonProperty("firstname")
    private String Middlename;

    @JsonProperty("firstname")
    private String Role;

    @JsonProperty("firstname")
    private String Datetime;

    @JsonProperty("type")
    private String Type;

    @JsonProperty("current")
    private String CurrentlyUsed;

    //middlename
    //lastname
    //role
    //date time
    //type visit on schedule
    //currently using by

}
