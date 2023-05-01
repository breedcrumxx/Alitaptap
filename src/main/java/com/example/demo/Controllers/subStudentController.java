package com.example.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.JsonResponse;
import com.example.demo.Models.SubStudent;
import com.example.demo.Services.SubStudentService;

@RestController
@RequestMapping(path = "/api/substudent")
public class subStudentController {

    @Autowired
    SubStudentService subStudentService;
    
    //create a method to transfer s student to other schedules
    // @PostMapping(path = "/transfer")
    // private JsonResponse setSubStudent(@RequestBody SubStudent sub){
    //     JsonResponse response = new JsonResponse();
    //     SubStudent substd = subStudentService.transferStudent(sub);

    //     if(substd == null){
    //         response.status = "Error";
    //         response.message = "Failed to transfer the student.";

    //         return response;
    //     }

    //     response.status = "Success";
    //     response.message = "Successfully transfered the student to other subject";

    //     return response;
    // }

    @PostMapping(path = "/transfer")
    private JsonResponse transferToSchedule(@RequestBody SubStudent substd){
        JsonResponse response = new JsonResponse();
        SubStudent transfered = subStudentService.transferStudent(substd);

        System.out.println(substd.toString());

        if(transfered == null){
            response.status = "Error";
            response.message = "Unable to process your request, internal server error.";

            return response;
        }

        response.status = "Success";
        response.message = "Successfully transfered the student.";

        return response;
    }

    @PostMapping(path = "/exclude")
    private JsonResponse excludeStudent(@RequestBody SubStudent sub){
        JsonResponse response = new JsonResponse();
        SubStudent excluded = subStudentService.exclude(sub);

        if(excluded == null){
            response.status = "Error";
            response.message = "Unable to process your request, internal server error.";

            return response;
        }

        response.status = "Error";
        response.message = "Successfully excluded the student.";

        return response;
    }

    //create a method to revert back a student
    @GetMapping("/revert/{stdid}/{schedid}")
    private JsonResponse revertSubStudent(@PathVariable("stdid") int stdid, @PathVariable("shedid") int schedid){
        JsonResponse response = new JsonResponse();

        subStudentService.revert(stdid, schedid);

        response.status = "Success";
        response.message = "Successfully transfered back the student to its previous subject";

        return response;
    }

}
