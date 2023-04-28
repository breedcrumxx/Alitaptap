package com.example.demo.Services;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.Batch;
import com.example.demo.Models.Schedule;
import com.example.demo.Models.Student;
import com.example.demo.Repositories.BatchRepository;
import com.example.demo.Repositories.ScheduleRepository;
import com.example.demo.Repositories.StudentRepository;

@Service
public class ScheduleService {
    
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    BatchRepository batchRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    public Schedule create(Schedule sched) {
        return scheduleRepository.save(sched);
    }

    public Schedule getSchedule(int id) {
        return scheduleRepository.getScheduleById(id);
    }

    public List<Schedule> getSchedules(int id){
        return scheduleRepository.findAllByIntructorId(id);
    }

    public List<Schedule> getCurrentSchedule(int id,String day) {
        List<Schedule> schedules = scheduleRepository.getCurrentSchedule(id ,day);

        if(schedules.size() == 0){
            return schedules;
        }

        // List<Schedule> finSchedules = new ArrayList<>();

        // for(Schedule sched: schedules){
        //     int batchid = sched.getBatch().getId();
        //     Batch batch = batchRepository.getBatchById(batchid);

        //     System.out.println(sched.toString());

        //     sched.setBatch(batch);

        //     finSchedules.add(sched);
        // }

        return schedules;
    }

    public Schedule getScheduleByDayAndTime(String day, String time){
        return null;
    }

    public Schedule getStudentSchedule(int batchId, String today, String time) {
        return scheduleRepository.getBatchScheduleToday(batchId, today, time);
    }

    public Schedule getStudentSchedule(int batchId, String today, String time, int schedid){
        return null;
    }

    public Schedule getScheduleAsOfNow(String today, String timeStamp) { // to get the current schedule and to check if the student is in the include list

        return scheduleRepository.getScheduleTodayThisTime(today, timeStamp);
    }
    
}
