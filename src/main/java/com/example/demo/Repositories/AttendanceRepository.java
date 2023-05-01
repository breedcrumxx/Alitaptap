package com.example.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer>{
    
    @Query(value = "select * from attendance where rfid=:rfid AND current_class=:class", nativeQuery = true)
    Attendance getRecentAttendance(@Param("rfid") String rfid, @Param("class") String currentClass);

    @Query(value = "select * from attendance where schedule=:schedid order by last_name asc", nativeQuery = true)
    List<Attendance> getAttendance(@Param("schedid") int schedid);

}
