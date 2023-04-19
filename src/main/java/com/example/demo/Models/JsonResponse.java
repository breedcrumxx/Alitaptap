package com.example.demo.Models;

public class JsonResponse {
    public String status;
    public String message;

    public JsonResponse(){
        
    }

    public JsonResponse(String stat, String msg){
        this.status = stat;
        this.message = msg;
    }
}
