package com.example.demo.Models;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    @OneToMany(mappedBy = "Instructor")
    private List<Schedule> schedules;

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

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Account)) {
            return false;
        }
        Account account = (Account) o;
        return Id == account.Id && Objects.equals(Username, account.Username) && Objects.equals(Password, account.Password) && Objects.equals(FullName, account.FullName) && Objects.equals(Sex, account.Sex) && Objects.equals(Role, account.Role) && Objects.equals(schedules, account.schedules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, Username, Password, FullName, Sex, Role, schedules);
    }


    @Override
    public String toString() {
        return "{" +
            " Id='" + getId() + "'" +
            ", Username='" + getUsername() + "'" +
            ", Password='" + getPassword() + "'" +
            ", FullName='" + getFullName() + "'" +
            ", Sex='" + getSex() + "'" +
            ", Role='" + getRole() + "'" +
            ", schedules='" + getSchedules() + "'" +
            "}";
    }


}
