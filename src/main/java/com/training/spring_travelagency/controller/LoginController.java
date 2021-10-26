package com.training.spring_travelagency.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Log4j2
@RequestMapping("/")
public class LoginController implements ErrorController {
    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String getLoginPage(@RequestParam(value = "logout", required = false) String logout,
                               @RequestParam(value = "error", required = false) String error,
                               Model model) {
        model.addAttribute("logout", logout != null);
        model.addAttribute("error", error != null);
        return "login";
    }

    @GetMapping("/access-denied")
    public String getAccessDeniedPage() {
        return "/500";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage());
        return "/404";
    }

    @RequestMapping("/error")
    public String getErrorPath() {
        return "/403";
    }
}