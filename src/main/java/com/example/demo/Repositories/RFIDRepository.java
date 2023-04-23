package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.RFID;

public interface RFIDRepository extends JpaRepository<RFID, Integer> {

    @Query(value = "select rfid.id, rfid.rfid, rfid.used_by from rfid where rfid.rfid=:id", nativeQuery = true)
    RFID findRFIDByRfid(@Param("id") String rfid);

}
