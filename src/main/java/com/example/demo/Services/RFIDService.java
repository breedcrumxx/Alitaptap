package com.example.demo.Services;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.Account;
import com.example.demo.Models.RFID;
import com.example.demo.Models.Schedule;
import com.example.demo.Models.Student;
import com.example.demo.Repositories.AccountRepository;
import com.example.demo.Repositories.RFIDRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RFIDService {
    
    @Autowired
    RFIDRepository rfidRepository;

    public RFID create(RFID create){
        return rfidRepository.save(create);
    }

    public boolean RFIDExist(String rfid){
        RFID existingRfid = rfidRepository.findRFIDByRfid(rfid);

        if(existingRfid == null){
            return false;
        }

        return true;
    }
}
