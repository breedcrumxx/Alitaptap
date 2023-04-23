package com.example.demo.Services;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.RFID;
import com.example.demo.Models.Schedule;
import com.example.demo.Models.Student;
import com.example.demo.Repositories.AccountRepository;
import com.example.demo.Repositories.RFIDRepository;

@Service
public class RFIDService {
    
    @Autowired
    RFIDRepository rfidRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ScheduleService scheduleService;

    public void trySaveRFID(){ //rename this to RFIDAPI
        Scanner input = new Scanner(System.in);
        RFID rfid = new RFID();

        String id = input.nextLine();
            
        rfid.setRFID(id);
        RFID isPresent = rfidRepository.getRFIDById(id);

        if(isPresent == null){
            System.out.println("System error, unidentified RFID.");

            trySaveRFID();
        }

        //in or out

        String dayNames[] = new DateFormatSymbols().getWeekdays();  
        Calendar date = Calendar.getInstance();  
        String today =  dayNames[date.get(Calendar.DAY_OF_WEEK)];
        System.out.println("Today is "+ dayNames[date.get(Calendar.DAY_OF_WEEK)]);  

        Student student = isPresent.getStudent();

        Schedule tempSched = scheduleService.getScheduleByDayAndTime(today, "10:01");

        if(tempSched == null){
            // intruder
            trySaveRFID();
        }

        List<Student> students = studentService.getStudents(tempSched.getBatch().getId(), tempSched.getId()); // get all the students in this batch
        List<Student> excludedStudents = studentService.getExcludedStudents(tempSched.getId());

        if(excludedStudents.size() != 0){
            students.removeAll(excludedStudents);
        }

        for(Student currStudent : students){
            if(student.equals(currStudent)){
                //exist in the schedule
            }
        }

        System.out.println(isPresent.toString());

        trySaveRFID();
    }

    private String getUser(String rfid){
        return "";
    }

    public RFID create(RFID rfid) {
        return rfidRepository.save(rfid);
    }
}
