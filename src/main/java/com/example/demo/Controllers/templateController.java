package com.example.demo.Controllers;

import java.io.IOException;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Others.RenderHTML;

@RestController
public class templateController {
    
    @RequestMapping(path = "/get-template/{template}", produces = org.springframework.http.MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    private String throwRegistrationTemplate(@PathVariable String template) throws IOException {
        String inject = "";
        RenderHTML html = new RenderHTML(System.getProperty("user.dir") + "/Components/templates/" + template + ".txt");
        inject = html.getHTML();

        return inject;
    }

}
