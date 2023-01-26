package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Controller
public class MainController {
    @GetMapping("/")
    public String homePage(){
        return "home";
    }

    @GetMapping("/user")
    public String pageForAuthUsers(){
        //вместо передачи principal в метод некоторые делают
        //Authentication a = SecurityContextHolder.getContext().getAuthentication();
        //эти способы взаимозаменяемые, но с principal просто короче
        return "user";
    }

    @GetMapping("/admin")
    public String pageForAdmins(){
        return "index";
    }
}
