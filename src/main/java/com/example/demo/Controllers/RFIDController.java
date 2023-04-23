package com.example.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Services.RFIDService;

@RestController
@RequestMapping(path = "/api/rfid")
public class RFIDController {
    
    @Autowired
    RFIDService rfidService;

    @PostMapping(path = "/verify")
    private void verify(@RequestBody String rfid){
        boolean exist = rfidService.RFIDExist(rfid);

        if(exist == false){
            System.out.println("Unindentified rfid");

            return;
        }

    }

    


}
