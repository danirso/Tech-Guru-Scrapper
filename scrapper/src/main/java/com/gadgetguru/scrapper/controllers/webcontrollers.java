package com.gadgetguru.scrapper.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

public class webcontrollers {

    @Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index"; // Retorna index.html dentro de /templates
    }
}
    @Controller
public class SearchController{

    @GetMapping("/search")
    public String search(){
        return "result";
    }
}

}
