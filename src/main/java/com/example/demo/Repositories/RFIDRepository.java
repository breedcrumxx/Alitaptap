package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.Rfid;

public interface RFIDRepository extends JpaRepository<Rfid, Integer> {
    
}
