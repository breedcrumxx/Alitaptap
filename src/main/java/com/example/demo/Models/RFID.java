package com.example.demo.Models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
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


    public int getId() {
        return this.Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getRfid() {
        return this.rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getUsedBy() {
        return this.UsedBy;
    }

    public void setUsedBy(String UsedBy) {
        this.UsedBy = UsedBy;
    }


}
