package com.example.demo.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class mainController {

    @GetMapping(path = "/")
    private ModelAndView throwHome(){
        ModelAndView page = new ModelAndView();
        page.setViewName("/host");

        return page;
    }

    // @PostMapping(path = "/test", produces = org.springframework.http.MediaType.TEXT_HTML_VALUE)
    // @ResponseBody
    // private String welcomeAsHTML() throws IOException {
    //     String inject = "";
    //     // System.out.println("Present Project Directory : "+ System.getProperty("user.dir"));
    //     RenderHTML html = new RenderHTML(System.getProperty("user.dir") + "/Components/table.txt");
    //     inject = html.getHTML();

    //     return inject;
    // }

}
