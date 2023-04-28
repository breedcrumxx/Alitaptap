package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.Log;

public interface LogRepository extends JpaRepository<Log, Integer> {

    @Query(value = "select * from log where rfid=:rfid ORDER by id DESC limit 1;", nativeQuery = true)
    Log getRecentLogByRfid(@Param("rfid") String rfid);
    
}
