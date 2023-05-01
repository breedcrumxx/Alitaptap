package com.example.demo.Models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormattedAttendance {
    
    private int StudentId;
    private String FullName;
    private int Present = 0;
    private int Late = 0;

    public FormattedAttendance(){

    }

    public FormattedAttendance(int StudentId, String FullName, int Present, int Late) {
        this.StudentId = StudentId;
        this.FullName = FullName;
        this.Present = Present;
        this.Late = Late;
    }


}
