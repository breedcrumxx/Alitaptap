package com.example.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.RFID;
import com.example.demo.Services.RFIDService;

@RestController
@RequestMapping(path = "/api/rfid")
public class RFIDController {
    
    @Autowired
    RFIDService rfidService;

    @PostMapping(path = "/verify")
    private void verify(@RequestBody String rfvalue){
        rfvalue = rfvalue.substring(0, rfvalue.length() -1);
        RFID searched = rfidService.verify(rfvalue);

        if(searched == null){
            // not valid
            System.out.println("this rfid is not registered yet");

            return;
        }

        //check if student
        if(searched.getUsedBy().equals("Student")){
            // then check his/her schedule
            // if no current schedule
            // then create log
            System.out.println("Student");
            

            return;
        }

        //check if teacher
        if(searched.getUsedBy().equals("Instructor")){
            // then create log
            System.out.println("Instructor");
            return;
        }

    }
}
