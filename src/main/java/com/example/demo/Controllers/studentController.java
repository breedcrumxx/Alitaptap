package com.example.demo.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.JsonResponse;
import com.example.demo.Models.Student;
import com.example.demo.Services.StudentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(path = "/api/student")
public class studentController {
    
    @Autowired
    StudentService studentService;

    @PostMapping(path = "/create-student")
    private JsonResponse insertStudent(@RequestBody Student student){
        JsonResponse response = new JsonResponse();
        boolean studentExist = studentService.exist(student.getStudentId());

        if(studentExist == true){
            response.status = "Error";
            response.message = "Student already exist.";

            return response;
        }
        
        Student createdStudent = studentService.create(student);

        if(createdStudent == null){
            response.status = "Error";
            response.message = "Error creating a student record, internal server error.";

            return response;
        }

        response.status = "Success";
        response.message = "Successfully added a student to the record.";

        return response;
    }

    @GetMapping(path = "/get-student/{id}")
    private JsonResponse getStudent(@PathVariable int id){
        JsonResponse response = new JsonResponse();
        Optional<Student> student = studentService.getStudent(id);
        
        if(!student.isPresent()){
            response.status = "Error";
            response.message = "Server error, missing student record.";

            return response;
        }

        Student finStudent = (Student) student.get();
        ObjectMapper map = new ObjectMapper();

        String json = "";
        try {
            json = map.writeValueAsString(finStudent);
        } catch (JsonProcessingException e) {
            response.status = "Error";
            response.message = "Error processing your request, internal server error. (Student controller)";

            return response;
        }
        
        response.status = "Success";
        response.message = json;

        return response;
    }

    @PostMapping(path = "/get-students")
    private JsonResponse getStudents(){
        JsonResponse response = new JsonResponse();
        List<Student> students = studentService.getStudents();

        if(students.size() == 0){
            response.status = "Success";
            response.message = "No records found.";

            return response;
        }

        ObjectMapper map = new ObjectMapper();
        String json = "";
        try {
            json = map.writeValueAsString(students);
        } catch (JsonProcessingException e) {
            response.status = "Error";
            response.message = "Error processing your request, internal server error. (Student controller)";

            return response;
        }

        response.status = "Success";
        response.message = json;

        return response;
    }

    // @GetMapping(path = "/get-mystudents/{id}")
    // private JsonResponse getMyStudents(@PathVariable int id){
    //     JsonResponse response = new JsonResponse();
    //     List<Student> students = studentService.getMyStudents(id);

    //     if(students.size() == 0){
    //         response.status = "Error";
    //         response.message = "You don't have any students.";

    //         return response;
    //     }

    //     ObjectMapper map = new ObjectMapper();
    //     String json = "";
    //     try {
    //         json = map.writeValueAsString(students);
    //     } catch (JsonProcessingException e) {
    //         response.status = "Error";
    //         response.message = "Error processing your request, internal server error. (Student controller)";

    //         return response;
    //     }

    //     response.status = "Success";
    //     response.message = json;

    //     return response;
    // }


}
