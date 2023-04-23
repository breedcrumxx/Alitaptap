package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.RFID;

public interface RFIDRepository extends JpaRepository<RFID, Integer> {

    @Query(value = "select * from rfid where rfid=:id", nativeQuery = true)
    RFID getRFIDById(@Param("id") String id);

}
