package com.example.demo.Controllers;

import java.io.IOException;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Others.RenderHTML;

@RestController
public class styleController {

    @RequestMapping(path = "/get-style/{style}", produces = org.springframework.http.MediaType.ALL_VALUE)
    @ResponseBody
    private String throwRegistrationStyle(@PathVariable String style) throws IOException {
        String inject = "";
        RenderHTML html = new RenderHTML(System.getProperty("user.dir") + "/Components/styles/" + style + ".txt");
        inject = html.getHTML();

        return inject;
    }

}
