package com.example.demo.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
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
    private String RFID;

    @JsonProperty("type")
    private String UsedBy;

    @JsonProperty("student")
    @JsonManagedReference
    @OneToOne(mappedBy = "StudentRFID")
    private Student student;

    @JsonProperty("Personel")
    @JsonManagedReference
    @OneToOne(mappedBy = "AccountRFID")
    private Account personel;

    public RFID(){
        
    }

    public RFID(String rfid){
        this.RFID = rfid;
    }

    public String getRFID(){
        return this.RFID;
    }


    @Override
    public String toString() {
        return "{" +
            " Id='" + getId() + "'" +
            ", RFID='" + getRFID() + "'" +
            ", UsedBy='" + getUsedBy() + "'" +
            ", student='" + getStudent() + "'" +
            ", personel='" + getPersonel() + "'" +
            "}";
    }

}
