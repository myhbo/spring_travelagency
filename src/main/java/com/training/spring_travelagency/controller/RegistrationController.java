package com.training.spring_travelagency.controller;

import com.training.spring_travelagency.dto.NewUserDTO;
import com.training.spring_travelagency.exception.EmailNotUniqueException;
import com.training.spring_travelagency.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getRegistrationPage(@ModelAttribute("user") NewUserDTO registerUserDTO) {
        return "registration";
    }

    @PostMapping
    public String registerNewUser(@ModelAttribute("user") NewUserDTO userDTO,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        userService.createUser(userDTO);
        return "redirect:/login";
    }

    @ExceptionHandler(EmailNotUniqueException.class)
    public String handleRuntimeException(EmailNotUniqueException e,
                                         Model model) {
        model.addAttribute("user", new NewUserDTO());
        model.addAttribute("emailErrorMessage", e.getMessage());
        return "registration";
    }
}