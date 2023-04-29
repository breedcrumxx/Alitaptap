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

    @Query(value = "select * from schedule where instructor_id=:id AND status=1", nativeQuery = true)
    List<Schedule> findAllByIntructorId(@Param("id") int id);

    @Query(value = "select * from schedule where id=:id", nativeQuery = true)
    Schedule getScheduleById(@Param("id") int id);

    @Query(value = "select * from schedule where week_slot=:day AND instructor_id=:id AND status=1", nativeQuery = true)
    List<Schedule> getCurrentSchedule(@Param("id") int id, @Param("day") String day);

    @Query(value = "select * from schedule where batch_id=:batchid AND (week_slot=:today AND (start_at<=:hour AND end_at>=:hour)) AND status=1", nativeQuery = true)
    Schedule getBatchScheduleToday(@Param("batchid") int batchId, @Param("today") String today, @Param("hour") String hour);

    @Query(value = "select * from schedule where week_slot=:today AND (time(start_at)<=time(:hour) AND time(end_at)>=time(:hour)) AND status=1", nativeQuery = true)
    Schedule getScheduleTodayThisTime(@Param("today") String today, @Param("hour") String timeStamp);

    @Query(value = "select * from schedule where id=:schedid AND time(ADDTIME(start_at, '00:10'))>=time(:time) AND status=1", nativeQuery = true)
    Schedule verifyLate(@Param("schedid") int currentSchedId, @Param("time") String timeStamp);

    @Query(value = "select * from schedule where (start_at<=:startat AND end_at>=:startat) OR (start_at<=:endat AND end_at>=:endat)", nativeQuery = true)
    Schedule checkScheduleTime(@Param("startat") String startAt, @Param("endat") String endAt);
}
