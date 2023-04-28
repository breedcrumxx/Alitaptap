package com.example.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.RFID;
import com.example.demo.Repositories.RFIDRepository;

@Service
public class RFIDService {
    
    @Autowired
    RFIDRepository rfidRepository;

    public RFID verify(String rfidValue){
        return rfidRepository.getByRFIDValue(rfidValue);
    }

    public RFID create(RFID accountRFID) {
        return rfidRepository.save(accountRFID);
    }

    public RFID verifyAndCreate(RFID rfid) {
        RFID isUsed = verify(rfid.getRfid());

        if(isUsed != null){
            return null;
        }

        return create(rfid);
    }

    
}
