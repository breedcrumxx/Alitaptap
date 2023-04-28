package com.example.demo.Controllers;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.Account;
import com.example.demo.Models.JsonResponse;
import com.example.demo.Models.Schedule;
import com.example.demo.Models.Student;
import com.example.demo.Repositories.AccountRepository;
import com.example.demo.Services.ScheduleService;
import com.example.demo.Services.StudentService;
import com.example.demo.Services.SubStudentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(path ="/api/schedule")
public class scheduleController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    StudentService studentService;

    @Autowired
    SubStudentService subStudentService;

    @PostMapping(path = "/create-schedule")
    private JsonResponse createSchedule(@RequestBody Schedule sched){
        JsonResponse response = new JsonResponse();

        Account Instructor = accountRepository.findAccountById(sched.getInstructor());
        sched.setInstructor(Instructor);

        Schedule create = scheduleService.create(sched);

        if(create == null){
            response.status = "Error";
            response.message = "Internal server error.";

            return response;
        }

        response.status = "Success";
        response.message = "Successfully created.";

        return response;
    }

    @GetMapping(path = "/{id}/schedules")
    public JsonResponse getSchedule(@PathVariable("id") int id){
        JsonResponse response = new JsonResponse();
        Optional<Account> optAcc = accountRepository.findById(id);

        if(!optAcc.isPresent()){
            response.status = "Error";
            response.message = "Suspicious account detected.";

            return response;
        }
        
        List<Schedule> tempSched = scheduleService.getSchedules(id);
        List<Schedule> finSchedules = new ArrayList<>(); // empty schedule container

        if(tempSched.size() == 0){
            response.status = "Empty";
            response.message = "No schedules yet, create one now!";

            return response;
        }

        // THIS FUNCTION IS USED TO LOAD ALL STUDENTS IN THE SCHEDULE
        // for(int i = 0; i < tempSched.size(); i++){
        //     Schedule temp = tempSched.get(i); // current sched working on

        //     if(temp.getStatus() == 1){ // check if it's an active schedule
        //         List<Student> students = studentService.getStudents(temp.getBatch().getId(), temp.getId()); // get all the students in this batch
        //         List<Student> excludedStudents = studentService.getExcludedStudents(temp.getId());

        //         if(excludedStudents.size() == 0){
        //             temp.setStudents(students);
        //             finSchedules.add(temp);

        //             continue;
        //         }

        //         students.removeAll(excludedStudents);

        //         temp.setStudents(students);
        //         finSchedules.add(temp);
        //     }
        // }

        ObjectMapper map = new ObjectMapper();
        try {
            String json = map.writeValueAsString(tempSched);

            response.status = "Success";
            response.message = json;

        }catch (JsonProcessingException e){
            response.status = "Error";
            response.message = "Internal server error. (Account controller)";
        }

        return response;
    }

    @PutMapping(path = "/update-schedule")
    private JsonResponse updateSchedule(@RequestBody Schedule sched){
        JsonResponse response = new JsonResponse();
        Schedule updatedSched = scheduleService.create(sched);

        if(updatedSched == null){
            response.status = "Error";
            response.message = "Error updating your schedule, internal server error.";

            return response;
        }

        response.status = "Success";
        response.message = "Successfully updated your schedule, check it now!";

        return response;
    }

    @PutMapping(path = "/set-active-schedule/{id}")
    private JsonResponse activateSchedule(@PathVariable int id){
        JsonResponse response = new JsonResponse();
        Schedule tempSched = scheduleService.getSchedule(id);

        if(tempSched == null){
            response.status = "Error";
            response.message = "Cannot activate your schedule.";

            return response;
        }

        tempSched.setStatus(1);
        Schedule removed = scheduleService.create(tempSched);

        if(removed == null){
            response.status = "Error";
            response.message = "Internal server error. (Schedule controller)";

            return response;
        }

        response.status = "Success";
        response.message = "Your schedule has been sent back to your schedules.";

        return response;
    }

    @GetMapping(path = "/{id}/current-schedule")
    private JsonResponse currentSchedule(@PathVariable int id){
        JsonResponse response = new JsonResponse();

        String dayNames[] = new DateFormatSymbols().getWeekdays();  
        Calendar date = Calendar.getInstance();  
        String today =  dayNames[date.get(Calendar.DAY_OF_WEEK)];
        System.out.println("Today is "+ dayNames[date.get(Calendar.DAY_OF_WEEK)]);  

        List<Schedule> schedules = scheduleService.getCurrentSchedule(id, today);

        if(schedules.size() == 0){
            response.status = "Empty";
            response.message = "You have no class for today.";

            return response;
        }

        ObjectMapper map = new ObjectMapper();

        String json;
        try {
            json = map.writeValueAsString(schedules);
        } catch (JsonProcessingException e) {
            System.out.println(e.toString());
            response.status = "Error";
            response.message = "Error processing your request.";

            return response;
        }

        response.status = "Success";
        response.message = json;

        return response;
    }

    @PutMapping(path = "/remove-schedule/{id}")
    private JsonResponse removeSchedule(@PathVariable int id){
        JsonResponse response = new JsonResponse();
        Schedule tempSched = scheduleService.getSchedule(id);

        if(tempSched == null){
            response.status = "Error";
            response.message = "Cannot remove your schedule.";

            return response;
        }

        tempSched.setStatus(0);
        Schedule removed = scheduleService.create(tempSched);

        if(removed == null){
            response.status = "Error";
            response.message = "Internal server error. (Schedule controller)";

            return response;
        }

        response.status = "Success";
        response.message = "Your schedule has been sent to your archive.";

        return response;
    }

    @DeleteMapping(path = "/delete-schedule/{id}")
    private JsonResponse deleteSchedule(@PathVariable int id){
        JsonResponse response = new JsonResponse();

        response.status = "Success";
        response.status = "Successfully deleted a schedule";

        return response;
    }

}

