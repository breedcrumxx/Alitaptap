package com.example.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.Rfid;
import com.example.demo.Repositories.RFIDRepository;

@Service
public class RFIDService {
    
    @Autowired
    RFIDRepository rfidRepository;

    public Rfid create(Rfid accountRFID) {
        return rfidRepository.save(accountRFID);
    }

    
}
