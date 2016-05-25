package com.excilys.cdb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class SwaggerController {

    @RequestMapping("/")
    public String home() {
        return "redirect:swagger-ui.html";
    }

}