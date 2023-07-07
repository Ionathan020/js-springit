package com.jstronkhorst.springit.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model, HttpServletRequest request){
        model.addAttribute("message", "Hello World!");
        return "index";
    }
}
