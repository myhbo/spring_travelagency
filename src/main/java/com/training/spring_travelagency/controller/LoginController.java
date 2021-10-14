package com.training.spring_travelagency.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Log4j2
@RequestMapping("/")
public class LoginController {
    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String getLoginPage(@RequestParam(value = "logout", required = false) String logout,
                               Model model) {
        model.addAttribute("logout", logout != null);
        return "login";
    }
}