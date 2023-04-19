package com.example.demo.Repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    @Query(value = "SELECT * from schedule where id=?", nativeQuery = true)
    ArrayList<Schedule> findByInstructorId(int id);

    @Query(value = "select * from schedule where instructor_id=:id", nativeQuery = true)
    List<Schedule> findAllByIntructorId(@Param("id") int id);

}
