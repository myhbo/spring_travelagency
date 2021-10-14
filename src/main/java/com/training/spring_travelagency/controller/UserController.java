package com.training.spring_travelagency.controller;

import com.training.spring_travelagency.exception.EmailNotUniqueException;
import com.training.spring_travelagency.dto.UserDTO;
import com.training.spring_travelagency.entity.User;
import com.training.spring_travelagency.entity.enums.Roles;
import com.training.spring_travelagency.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@Log4j2
@RequestMapping
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/users")
    public String getUsersPage(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "users";
    }

    @GetMapping("/users/update/{id}")
    public String getUserUpdatePage(@PathVariable("id") long id, Model model) {
        User user = userService.getUserById(id);

        model.addAttribute("user", user);
        model.addAttribute("roles", Roles.values());
        return "user-update";
    }

    @PostMapping("/users/update/{id}")
    public String updateUser(@PathVariable("id") long id,
                             @ModelAttribute("user") UserDTO userDTO,
                             BindingResult bindingResult,
                             Model model) throws EmailNotUniqueException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("role", Roles.values());
            return "user-update";
        }
        userService.updateUser(id, userDTO);
        return "redirect:/users";      }

    @GetMapping("/users/ban/{id}")
    public String banUser(@PathVariable("id") long id) {
        userService.banUser(id);
        return "redirect:/users";
    }

    @GetMapping("/users/unban/{id}")
    public String unbanUser(@PathVariable("id") long id) {
        userService.unbanUser(id);
        return "redirect:/users";
    }
}

