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

    @Query(value = "select * from schedule where id=:id", nativeQuery = true)
    Schedule getScheduleById(@Param("id") int id);

    @Query(value = "select * from schedule where week_slot=:day AND instructor_id=:id", nativeQuery = true)
    List<Schedule> getCurrentSchedule(@Param("id") int id, @Param("day") String day);

    @Query(value = "select * from schedule where batch_id=:batchid AND (week_slot=:today AND (start_at<=:hour AND end_at>=:hour))", nativeQuery = true)
    Schedule getBatchScheduleToday(@Param("batchid") int batchId, @Param("today") String today, @Param("hour") String hour);

    @Query(value = "select * from schedule where week_slot=:today AND (time(start_at)<=time(:hour) AND time(end_at)>=time(:hour))", nativeQuery = true)
    Schedule getScheduleTodayThisTime(@Param("today") String today, @Param("hour") String timeStamp);


}
