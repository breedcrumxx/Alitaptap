package com.example.demo.Controllers;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.hibernate.type.descriptor.jdbc.TimestampJdbcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.Account;
import com.example.demo.Models.Attendance;
import com.example.demo.Models.Log;
import com.example.demo.Models.RFID;
import com.example.demo.Models.Schedule;
import com.example.demo.Models.Student;
import com.example.demo.Repositories.ScheduleRepository;
import com.example.demo.Services.AccountService;
import com.example.demo.Services.AttendanceService;
import com.example.demo.Services.LogService;
import com.example.demo.Services.RFIDService;
import com.example.demo.Services.ScheduleService;
import com.example.demo.Services.StudentService;

@RestController
@RequestMapping(path = "/api/rfid")
public class RFIDController {
    
    @Autowired
    RFIDService rfidService;

    @Autowired
    StudentService studentService;

    @Autowired
    AccountService accountService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    AttendanceService attendanceService;

    @Autowired
    LogService logService;

    @PostMapping(path = "/verify")
    private void verify(@RequestBody String rfvalue){
        rfvalue = rfvalue.substring(0, rfvalue.length() -1);
        RFID searched = rfidService.verify(rfvalue);

        if(searched == null){
            // not valid
            System.out.println("this rfid is not registered yet");

            return;
        }

        String dayNames[] = new DateFormatSymbols().getWeekdays();  
        Calendar date = Calendar.getInstance();  

        String today =  dayNames[date.get(Calendar.DAY_OF_WEEK)];
        System.out.println("Today is "+ dayNames[date.get(Calendar.DAY_OF_WEEK)]); 
        
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());
        String timeStamp = new SimpleDateFormat("HH:mm").format(date.getTime());
        System.out.println(timeStamp);

        //check if student
        if(searched.getUsedBy().equals("Student")){
            // then check his/her schedule
            Student currStd = studentService.getStudentByRfid(searched.getId());
            Schedule schedule = scheduleService.getStudentSchedule(currStd.getBatchId().getId(), today, timeStamp);
            //check other schedule if the student is included

            System.out.println(currStd.toString());

            String firstname = currStd.getFirstName();
            String lastname = currStd.getLastName();
            String middlename = currStd.getMiddleName();
            String atDateTime = currentDateTime;

            //verify if the log record to see if in or out
            Log verify = logService.getRecentLogByRfid(currStd.getStudentRFID().getRfid(), currentDateTime);
                if(verify!=null){System.out.print(verify.toString());}

            // System.out.println(verify.toString());
            String logStatus = "Out";

            if(schedule != null){ // if there is schedule today for the student
                List<Student> students = studentService.getStudents(schedule.getBatch().getId(), schedule.getId());
                List<Student> excluded = studentService.getExcludedStudents(schedule.getId());
                Account instructor = accountService.getAccountById(schedule.getInstructor());

                students.removeAll(excluded);

                for(Student student : students){
                    if(currStd.equals(student)){  // is naturally or included in the schedule

                        String status = "On time"; // craete a method to identify if late or not

                        Attendance attendance = new Attendance(firstname, lastname, middlename, atDateTime, status, schedule.getBatch().getCourse() + "-" + schedule.getBatch().getSection(), schedule.getId(), schedule.getSubjectCode(), instructor.getLastName() + ", " + instructor.getLastName());
                        attendanceService.create(attendance);

                        // new Attendance(firstname, lastname, middlename, currentDateTime, status, atDateTime, 0, logStatus, status);
                        
                        // condition to determin the log status
                        if(verify == null){
                            logStatus = "In";
                            System.out.println("in");
                        } else {
                            if(verify.getStatus().equals("In")){
                                logStatus = "Out";
                            } else {
                                logStatus = "In";
                            }
                        }

                        System.out.println(logStatus);

                        //create log
                        Log log = new Log(firstname, lastname, middlename, currStd.getStudentRFID().getRfid(), "Student", currentDateTime, logStatus, "Scheduled", schedule.getSubjectCode(), instructor.getLastName() + ", " + instructor.getLastName());
                        logService.create(log);

                        System.out.println("Scheduled");
                        
                        return;
                    }
                }

                // is excluded in the schedule
                // condition to determin the log status
                if(verify == null){
                    logStatus = "In";
                    System.out.println("in");
                } else {
                    if(verify.getStatus().equals("In")){
                        logStatus = "Out";
                    } else {
                        logStatus = "In";
                    }
                }

                Log log = new Log(firstname, lastname, middlename, currStd.getStudentRFID().getRfid(), "Student", currentDateTime, logStatus, "Unscheduled", schedule.getSubjectCode(), instructor.getLastName() + ", " + instructor.getLastName());
                logService.create(log);
                System.out.println("excluded");
                return;
            }

            if(schedule == null){ // if not scheduled for today then check other schedule
                Schedule currentSchedule = scheduleService.getScheduleAsOfNow(today, timeStamp); // to get the current schedule

                // update to filter the incactive schedules
                if(currentSchedule == null){
                    System.out.println("no current schedule");
                }
       
                if(currentSchedule != null){ // to check if there is a schedule for today
                    List<Student> students = studentService.getStudents(currentSchedule.getBatch().getId(), currentSchedule.getId()); // get all students form schedule today
                    List<Student> excluded = studentService.getExcludedStudents(currentSchedule.getId()); // all students from schedule today
                    Account instructor = accountService.getAccountById(currentSchedule.getInstructor());
                    System.out.println(instructor.toString());

                    // check get all the students in todays schedule

                    int currentSchedId = currentSchedule.getId();
                    String currentInstructor = instructor.getLastName() + ", " + instructor.getFirstName(); 

                    /*
                     * create a method to identify if the student is late or not
                     * update schedule, put initial date created attribute
                     * 
                     * 
                     * 
                     */

                    if(excluded.size() != 0){
                        students.removeAll(excluded);
                    }
                    
                    for(Student student : students){
                        if(currStd.equals(student)){ // checking if student is included in the schedule

                            String status = "On time"; //  create method to identify if the student is late or not
                            String batch = currStd.getBatchId().getCourse() + "-" + currStd.getBatchId().getSection();

                            // create attendance 
                            Attendance attendance = new Attendance(firstname, lastname, middlename, atDateTime, status, batch, currentSchedId, currentSchedule.getSubjectCode(), currentInstructor);
                            attendanceService.create(attendance);
                            
                            // condition to determin the log status
                            if(verify == null){
                                logStatus = "In";
                                System.out.println("in");
                            } else {
                                if(verify.getStatus().equals("In")){
                                    logStatus = "Out";
                                } else {
                                    logStatus = "In";
                                }
                            }

                            //create log
                            Log log = new Log(firstname, lastname, middlename, currStd.getStudentRFID().getRfid(), "Student", currentDateTime, logStatus, "Scheduled", currentSchedule.getSubjectCode(), currentInstructor);
                            logService.create(log);
                            
                            System.out.println("Included");
                            return;
                        }
                    }

                    //there is schedule but does not belong to

                    if(verify == null){
                        logStatus = "In";
                    } else {
                        if(verify.getStatus().equals("In")){
                            logStatus = "Out";
                        } else {
                            logStatus = "In";
                        }
                    }

                    Log log = new Log(firstname, lastname, middlename, currStd.getStudentRFID().getRfid(), "Student", currentDateTime, logStatus, "Unscheduled", currentSchedule.getSubjectCode(), currentInstructor);

                    logService.create(log);

                    System.out.println("Unscheduled, either excluded or naturally not in the schedule");

                    return;
                }

                // no schedule
                if(verify == null){
                    logStatus = "In";
                } else {
                    if(verify.getStatus().equals("In")){
                        logStatus = "Out";
                    } else {
                        logStatus = "In";
                    }
                }

                Log log = new Log(
                    currStd.getFirstName(),
                    currStd.getLastName(),
                    currStd.getMiddleName(),
                    currStd.getStudentRFID().getRfid(),
                    "Student",
                    currentDateTime,
                    logStatus,
                    "Unscheduled",
                    "None",
                    "None"
                );

                logService.create(log);
                System.out.println("No schedule");
                return;
            }
        }

        //check if teacher
        if(searched.getUsedBy().equals("Instructor")){
            // then create log.
            Account instructor = accountService.getAccountByRFID(searched.getId());


            System.out.println(instructor.toString());
            return;
        }

    }
}
