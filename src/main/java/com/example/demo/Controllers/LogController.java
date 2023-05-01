package com.example.demo.Controllers;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.JsonResponse;
import com.example.demo.Models.Log;
import com.example.demo.Services.LogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(path = "/api/log")
public class LogController {

    @Autowired
    LogService logService;
    
    @GetMapping(path = "/count")
    private JsonResponse getLogCount(){
        JsonResponse response = new JsonResponse();

        String dayNames[] = new DateFormatSymbols().getWeekdays();  
        Calendar date = Calendar.getInstance();  
        String today =  dayNames[date.get(Calendar.DAY_OF_WEEK)];
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        int count = logService.getLogCountToday(currentDate);

        String json = "" + count;

        response.status = "Success";
        response.message = json;

        return response;
    }

    @GetMapping(path = "/get-recent")
    private JsonResponse getRecentLogs(){
        JsonResponse response = new JsonResponse();
        List<Log> logs = logService.getRecentLogs();

        if(logs == null || logs.size() == 0){
            response.status = "Success";
            response.message = "No Taps have made yet.";

            return response;
        }

        ObjectMapper map = new ObjectMapper();
        String json = "";

        try {
            json = map.writeValueAsString(logs);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            System.out.println(e.toString());
            response.status = "Error";
            response.message = "Unable to process your request, internal server error.";

            return response;
        }

        response.status = "Success";
        response.message = json;

        return response;
    }

    @GetMapping(path = "/get-logs")
    private JsonResponse getLogs(){
        JsonResponse response = new JsonResponse();
        List<Log> logs = logService.getAllLogs();

        if(logs == null || logs.size() == 0){
            response.status = "Success";
            response.message = "No log taps made yet.";

            return response;
        }

        ObjectMapper map = new ObjectMapper();
        String json = "";

        try {
            json = map.writeValueAsString(logs);
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
