package com.training.spring_travelagency.controller;

import com.training.spring_travelagency.dto.UserDTO;
import com.training.spring_travelagency.entity.User;
import com.training.spring_travelagency.entity.enums.Roles;
import com.training.spring_travelagency.exception.EmailNotUniqueException;
import com.training.spring_travelagency.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@Log4j2
@RequestMapping
public class UserController {

    private final UserService userService;
    private static final int[] PAGE_SIZES = {5, 10, 50, 100};

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/users")
    public String getUsersPage(Model model,
                               @PageableDefault(sort = {"id"},
                                       direction = Sort.Direction.ASC,
                                       size = 10) Pageable pageable) {
        model.addAttribute("users", userService.findAllUsersPageable(pageable));
        model.addAttribute("pageSizes", PAGE_SIZES);
        return "/users";
    }

    @GetMapping("/users/update/{id}")
    public String getUserUpdatePage(@PathVariable long id, Model model) {
        User user = userService.getUserById(id);

        model.addAttribute("user", user);
        model.addAttribute("roles", Roles.values());
        return "/user-update";
    }

    @PostMapping("/users/update/{id}")
    public String updateUser(@PathVariable long id,
                             @ModelAttribute("user") @Valid UserDTO userDTO,
                             BindingResult bindingResult,
                             Model model) throws EmailNotUniqueException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("role", Roles.values());
            return "/user-update";
        }
        userService.updateUser(id, userDTO);
        return "redirect:/users";
    }

    @GetMapping("/users/ban/{id}")
    public String banUser(@PathVariable long id) {
        userService.banUser(id);
        return "redirect:/users";
    }

    @GetMapping("/users/unban/{id}")
    public String unbanUser(@PathVariable long id) {
        userService.unbanUser(id);
        return "redirect:/users";
    }

    @GetMapping("/user-cabinet")
    public String getUserCabinetPage(@AuthenticationPrincipal User user,
                                     Model model) {
        model.addAttribute("user", userService.getUserById(user.getId()));
        return "/user-cabinet";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage());
        return "/404";
    }
}

