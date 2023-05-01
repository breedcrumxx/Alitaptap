package com.example.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.Log;

public interface LogRepository extends JpaRepository<Log, Integer> {

    @Query(value = "select * from log where rfid=:rfid ORDER by id DESC limit 1;", nativeQuery = true)
    Log getRecentLogByRfid(@Param("rfid") String rfid);

    @Query(value = "select count(*) from log where date(datetime)=date(:currdate)", nativeQuery = true)
    int countLogsToday(@Param("currdate") String currentDate);

    @Query(value = "select * from log order by datetime desc limit 6", nativeQuery = true)
    List<Log> recentLogs();

    @Query(value = "select * from log", nativeQuery = true)
    List<Log> getLogs();
    
}
