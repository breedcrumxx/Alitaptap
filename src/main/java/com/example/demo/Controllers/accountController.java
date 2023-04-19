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
import com.example.demo.Models.Batch;
import com.example.demo.Models.JsonResponse;
import com.example.demo.Models.Schedule;
import com.example.demo.Models.Student;
import com.example.demo.Repositories.AccountRepository;
import com.example.demo.Repositories.ScheduleRepository;
import com.example.demo.Services.StudentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(path = "/api/account")
public class accountController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    StudentService studentService;

    //create account method
    @PostMapping(path = "/create-account", consumes = "application/json", produces = "application/json")
    public JsonResponse createAccount(@RequestBody Account create){

        System.out.println(create.getFullName());
        JsonResponse response = new JsonResponse();

        Account match = accountRepository.findAccount(create.getUsername());
        if(match != null){
            response.status = "Used";
            response.message = "Username is already in used.";

            return response;
        }

        Account account = accountRepository.save(create);
        if(account == null) {
            response.status = "Error";
            response.message = "Failed to create your account. Internal server error!";
        }

        response.status = "Success";
        response.message = "Successfully created your account!";

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

        response.status = "Success";
        response.message = "{ \"id\":\"" + account.getId() + "\", \"name\":\"" + account.getFullName() + "\", \"role\":\"" + account.getRole() + "\", \"sex\":\"" + account.getSex() + "\"}";

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

        response.status = "success";
        response.message = "{ \"id\":\"" + account.getId() + "\", \"name\":\"" + account.getFullName() + "\", \"role\":\"" + account.getRole() + "\", \"sex\":\"" + account.getSex() + "\"}";

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
            response.status = "Success";
            response.message = "No schedules yet, create one now!";

            return response;
        }

        for(int i = 0; i < tempSched.size(); i++){
            Schedule temp = tempSched.get(i);
            Batch currBatch = temp.getBatch();
            List<Student> students = studentService.getFromBatchId(currBatch.getId());

            if(temp.getStatus() == 1){
                temp.setStudents(students);
                finSchedules.add(temp);
            }
        }

        curr_acc.setSchedules(finSchedules);

        ObjectMapper map = new ObjectMapper();
        try {
            String json = map.writeValueAsString(curr_acc);

            response.status = "Success";
            response.message = json;

        }catch (JsonProcessingException e){
            response.status = "Error";
            response.message = "Internal server error. (Account controller)";
        }

        return response;
    }

}
