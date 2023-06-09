package com.example.demo.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.Account;
import com.example.demo.Models.JsonResponse;
import com.example.demo.Models.RFID;
import com.example.demo.Models.Schedule;
import com.example.demo.Models.Student;
import com.example.demo.Repositories.AccountRepository;
import com.example.demo.Repositories.ScheduleRepository;
import com.example.demo.Services.AccountService;
import com.example.demo.Services.RFIDService;
import com.example.demo.Services.ScheduleService;
import com.example.demo.Services.StudentService;
import com.example.demo.Services.SubStudentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(path = "/api/account")
public class accountController {

    ObjectMapper map = new ObjectMapper();

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    SubStudentService subStudentService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    RFIDService rfidService;

    // create account method
    @PostMapping(path = "/create-account", consumes = "application/json", produces = "application/json")
    public JsonResponse createAccount(@RequestBody Account create){
        JsonResponse response = new JsonResponse();

        Account match = accountRepository.findAccount(create.getUsername());
        if(match != null){
            response.status = "Used";
            response.message = "Username is already in used.";

            return response;
        }

        //verify first
        RFID isUsed = rfidService.verify(create.getAccountRFID().getRfid());

        if(isUsed != null){
            response.status = "Error";
            response.message = "RFID is already in used by someone.";

            return response;
        }

        //save rfid
        RFID createdRfid = rfidService.create(create.getAccountRFID());

        if(createdRfid == null){
            response.status = "Error";
            response.message = "Error processing you request.";

            return response;
        }

        //insert to account and then save
        create.setAccountRFID(createdRfid);
        Account account = accountRepository.save(create);

        if(account == null){
            response.status = "Error";
            response.message = "Error processing you request.";

            return response;
        }

        response.status = "Success";
        response.message = "Successfully created your account!";

        return response;
    }

    @GetMapping(path = "/verify/{username}")
    private JsonResponse verifyUsername(@PathVariable String username){

        System.out.println(username);
        JsonResponse response = new JsonResponse();
        Account match = accountRepository.findAccount(username);

        if(match != null){
            response.status = "Used";
            response.message = "Username is already in used.";

            return response;
        }

        response.status = "Success";
        response.message = "Unique username.";

        return response;
    }

    //login account method
    @PostMapping(path = "/login-account", consumes = {"application/json"}, produces = "application/json")
    public JsonResponse loginAccount(@RequestBody Account login){
        JsonResponse response = new JsonResponse();
        Account account = accountRepository.findAccount(login.getUsername());

        if(account == null) {
            response.status = "No match";
            response.message = "The username you entered didn't match in any of our records. Please try again.";
            return response;
        } 

        String password = account.getPassword();
        
        if(!password.equals(login.getPassword())){
            
            response.status = "Error";
            response.message = "Incorrect password!";

            return response;
        }

        ObjectMapper map = new ObjectMapper();
        String json = "";

        try {
            json = map.writeValueAsString(account);
        } catch (JsonProcessingException e) {
            response.status = "Error";
            response.message = "Error processing your request, internal server error.";
            System.out.println(e.toString());
            return response;
        }

        response.status = "Success";
        response.message = json;
        return response;
    }

    @PostMapping(path = "/get-user-data/{id}", produces = "application/json")
    private JsonResponse throwUserData(@PathVariable int id){
        Account account = accountRepository.findAccountById(id);
        JsonResponse response = new JsonResponse();

        if(account == null){
            response.status = "Error";
            response.message = "No resources found in the server.";

            return response;
        }

        ObjectMapper map = new ObjectMapper();

        String json = "";
        try {
            json = map.writeValueAsString(account);
        } catch (JsonProcessingException e) {
            response.status = "Error";
            response.message = "Error processing your request, internal server error.";

            return response;
        }

        response.status = "success";
        response.message = json;
        return response;
    }

    @GetMapping(path = "/{id}/schedules")
    public JsonResponse getSchedule(@PathVariable("id") int id){
        JsonResponse response = new JsonResponse();
        Optional<Account> optAcc = accountRepository.findById(id); // change this

        if(!optAcc.isPresent()){
            response.status = "Error";
            response.message = "Suspicious account detected.";

            return response;
        }

        Account curr_acc = (Account) optAcc.get();
        
        List<Schedule> tempSched = scheduleService.getSchedules(id); // all of the schedules without students
        List<Schedule> finSchedules = new ArrayList<>(); // empty schedule container

        if(tempSched.size() == 0){
            response.status = "Success";
            response.message = "No schedules yet, create one now!";

            return response;
        }

        // for(int i = 0; i < tempSched.size(); i++){
        //     Schedule temp = tempSched.get(i); // current sched working on
        //     // System.out.println("here" + temp.getBatch().getId());
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

        try {
            String json = map.writeValueAsString(tempSched);

            response.status = "Success";
            response.message = json;

        }catch (JsonProcessingException e){
            response.status = "Error";
            response.message = "Internal server error. (Account controller)";

            System.out.println(e.toString());
        }

        return response;
    }

    @GetMapping(path = "/get-all-instructors")
    private JsonResponse getAllInstructors(){
        JsonResponse response = new JsonResponse();
        List<Account> instructors = accountRepository.getAllInstructors();

        if(instructors == null || instructors.size() == 0){
            response.status = "Empty";
            response.message = "No instructors yet.";

            return response;
        }

        ObjectMapper map = new ObjectMapper();
        String json = "";

        try {
			json = map.writeValueAsString(instructors);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            response.status = "Error";
            response.message = "Unable to process your request, internal server error.";

            return response;
		}

        response.status = "Success";
        response.message = json;

        return response;
    }
}
