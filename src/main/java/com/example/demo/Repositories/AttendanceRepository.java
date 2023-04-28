package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer>{
    
}
