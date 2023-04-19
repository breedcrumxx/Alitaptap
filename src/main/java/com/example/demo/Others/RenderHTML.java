package com.example.demo.Others;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class RenderHTML {
    private String Filename;

    public RenderHTML(){

    }

    public RenderHTML(String filename){
        this.Filename = filename;
    }

    public String getHTML() throws IOException{
        String response = "", container;
        File file = new File(Filename);

        BufferedReader reader = new BufferedReader(new FileReader(file));
        while((container = reader.readLine()) != null){
            response += container;
        }
        reader.close();

        return response;
    }
}
