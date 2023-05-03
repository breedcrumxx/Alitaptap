package com.example.demo.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.JsonResponse;
import com.example.demo.Models.Student;
import com.example.demo.Models.StudentSearch;
import com.example.demo.Repositories.StudentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@RestController
@RequestMapping(path = "/api/search")
public class SearchController {

    @Autowired
    StudentRepository studentRepository;
    
    @PostMapping(path = "/student")
    private JsonResponse searchStudent(@RequestBody StudentSearch stdSrc){
        JsonResponse response = new JsonResponse();
        List<Student> students = new ArrayList<>();
        String search = stdSrc.getSearch();
        String constraint = stdSrc.getConstraint();
        String filter = stdSrc.getFilter();

        // System.out.println(search);
        System.out.println(constraint);
        // System.out.println(filter);

        if(constraint.equals("default")){
            
        }
        if(constraint.equals("first_name")){
            students = studentRepository.getStudentsByFirstname(search);
        }
        if(constraint.equals("last_name")){
            students = studentRepository.getStudentsByLastname(search);
        }
        if(constraint.equals("middle_name")){
            students = studentRepository.getStudentsByMiddlename(search);
        }
        if(constraint.equals("student_id")){
            students = studentRepository.getStudentsByStudentId(search);
        }
        if(constraint.equals("batch")){
            students = studentRepository.getStudentsByClass(search);
        }
        if(constraint.equals("address")){
            students = studentRepository.getStudentsByAddress(search);
        }

        if(students.size() == 0){
            response.status = "Empty";
            response.message = "No results found.";

            return response;
        }

        ObjectMapper map = new ObjectMapper();
        String json = "";
        map.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        try {
            json = map.writeValueAsString(students);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response.status = "Error";
            response.message = "Error";

            return response;
        }
        // for(Student student: students){
        //     System.out.println(student.toString());
        // }

        response.status = "Success";
        response.message = json;

        return response;
    }
}
