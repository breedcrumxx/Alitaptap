package com.example.demo.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.Attendance;
import com.example.demo.Repositories.AttendanceRepository;

@Service
public class AttendanceService {
    
    @Autowired
    AttendanceRepository attendanceRepository;

    public Attendance create(Attendance attendance){
        return attendanceRepository.save(attendance);
    }

    public Attendance verify(String rfid, String currentClass){
        return attendanceRepository.getRecentAttendance(rfid, currentClass);
    }

    public List<Attendance> getDetailedAttendance(int schedid) {
        return attendanceRepository.getAttendance(schedid);
    }
}
