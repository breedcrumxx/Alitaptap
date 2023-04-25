package com.example.demo.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RFID {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @JsonProperty("rfid")
    private String rfid;

    @JsonProperty("used_by")
    private String UsedBy;


    @Override
    public String toString() {
        return "{" +
            " Id='" + getId() + "'" +
            ", rfid='" + getRfid() + "'" +
            ", UsedBy='" + getUsedBy() + "'" +
            "}";
    }

}
