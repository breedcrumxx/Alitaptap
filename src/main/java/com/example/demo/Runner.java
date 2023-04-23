package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.Services.RFIDService;

@Component
public class Runner implements CommandLineRunner {
    
    @Autowired
    private RFIDService rfidService;

    @Override
    public void run(String... args) throws Exception {
        // rfidService.trySaveRFID();
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

}
