package com.example.demo.Controllers;

import java.util.ArrayList;
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
import com.example.demo.Repositories.AccountRepository;
import com.example.demo.Repositories.ScheduleRepository;
import com.example.demo.Services.StudentService;
import com.example.demo.Services.SubStudentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(path ="/api/schedule")
public class scheduleController {
    
    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    SubStudentService subStudentService;

    @PostMapping(path = "/create-schedule")
    private JsonResponse createSchedule(@RequestBody Schedule sched){
        JsonResponse response = new JsonResponse();

        Account Instructor = accountRepository.findAccountById(sched.getInstructor());
        sched.setInstructor(Instructor);

        Schedule create = scheduleRepository.save(sched);

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

        Account curr_acc = (Account) optAcc.get();
        
        List<Schedule> tempSched = curr_acc.getSchedules();
        List<Schedule> finSchedules = new ArrayList<>();

        if(tempSched.size() == 0){
            response.status = "Empty";
            response.message = "No schedules yet, create one now!";

            return response;
        }

        // for(int i = 0; i < tempSched.size(); i++){
        //     Schedule temp = tempSched.get(i);
        //     Batch currBatch = temp.getBatch();
        //     List<Student> students = studentService.getFromBatchId(currBatch.getId());

        //     if(temp.getStatus() == 1){
        //         temp.setStudents(students);

        //         finSchedules.add(temp);
        //     }
        // }

        // curr_acc.setSchedules(finSchedules);

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
        Schedule updatedSched = scheduleRepository.save(sched);

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
        Optional<Schedule> tempSched = scheduleRepository.findById(id);

        if(!tempSched.isPresent()){
            response.status = "Error";
            response.message = "Cannot activate your schedule.";

            return response;
        }

        Schedule sched = (Schedule) tempSched.get();
        sched.setStatus(1);
        Schedule removed = scheduleRepository.save(sched);

        if(removed == null){
            response.status = "Error";
            response.message = "Internal server error. (Schedule controller)";

            return response;
        }

        response.status = "Success";
        response.message = "Your schedule has been sent back to your schedules.";

        return response;
    }

    @PutMapping(path = "/remove-schedule/{id}")
    private JsonResponse removeSchedule(@PathVariable int id){
        JsonResponse response = new JsonResponse();
        Optional<Schedule> tempSched = scheduleRepository.findById(id);

        if(!tempSched.isPresent()){
            response.status = "Error";
            response.message = "Cannot remove your schedule.";

            return response;
        }

        Schedule sched = (Schedule) tempSched.get();
        sched.setStatus(0);
        Schedule removed = scheduleRepository.save(sched);

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

