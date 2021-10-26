package com.training.spring_travelagency.controller;

import com.training.spring_travelagency.dto.OrderDTO;
import com.training.spring_travelagency.entity.Order;
import com.training.spring_travelagency.entity.User;
import com.training.spring_travelagency.entity.enums.Roles;
import com.training.spring_travelagency.service.OrderService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@Log4j2
public class OrderController {
    private final OrderService orderService;
    private static final int[] PAGE_SIZES = {5, 10, 50, 100};

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders/add/{id}")
    public String orderTour(@AuthenticationPrincipal User user,
                            @PathVariable("id") long tourId,
                            @RequestParam(value = "page", required = false) Long pageId,
                            @RequestParam(value = "size", required = false) Long size,
                            @RequestParam(value = "sort", required = false) String sort,
                            RedirectAttributes redirectAttributes) {
        orderService.orderTour(user.getId(), tourId);
        redirectAttributes.addAttribute("page", pageId);
        redirectAttributes.addAttribute("size", size);
        redirectAttributes.addAttribute("sort", sort);
        return "redirect:/user-cabinet";
    }

    @GetMapping("/orders")
    public String getOrdersPage(Model model,
                                @PageableDefault(sort = {"id"},
                                        direction = Sort.Direction.DESC,
                                        size = 10) Pageable pageable) {
        model.addAttribute("orders", orderService.findAllOrdersPageable(pageable));
        model.addAttribute("pageSizes", PAGE_SIZES);
        return "/orders";
    }

    @GetMapping("/orders/set-confirmed/{id}")
    public String setOrderAsConfirmed(@PathVariable("id") long orderId,
                                       @RequestParam(value = "page", required = false) Long pageId,
                                       @RequestParam(value = "size", required = false) Long size,
                                       @RequestParam(value = "sort", required = false) String sort,
                                       RedirectAttributes redirectAttributes) {
        orderService.markOrderAsConfirmed(orderId);
        redirectAttributes.addAttribute("page", pageId);
        redirectAttributes.addAttribute("size", size);
        redirectAttributes.addAttribute("sort", sort);
        return "redirect:/orders";
    }

    @GetMapping("/orders/set-rejected/{id}")
    public String setOrderAsRejected(@PathVariable("id") long orderId,
                                      @RequestParam(value = "page", required = false) Long pageId,
                                      @RequestParam(value = "size", required = false) Long size,
                                      @RequestParam(value = "sort", required = false) String sort,
                                      RedirectAttributes redirectAttributes) {
        orderService.markOrderAsRejected(orderId);
        redirectAttributes.addAttribute("page", pageId);
        redirectAttributes.addAttribute("size", size);
        redirectAttributes.addAttribute("sort", sort);
        return "redirect:/orders";
    }

    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable("id") long orderId,
                              @RequestParam(value = "page", required = false) Long pageId,
                              @RequestParam(value = "size", required = false) Long size,
                              @RequestParam(value = "sort", required = false) String sort,
                              RedirectAttributes redirectAttributes) {
        orderService.deleteOrder(orderId);
        redirectAttributes.addAttribute("page", pageId);
        redirectAttributes.addAttribute("size", size);
        redirectAttributes.addAttribute("sort", sort);
        return "redirect:/orders";
    }

    @GetMapping("/orders/set-discount/{id}")
    public String getSetDiscountPage(@PathVariable long id,
                                     Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "/discount-update";
    }

    @PostMapping("/orders/set-discount/{id}")
    public String setDiscount(@PathVariable long id,
                              @ModelAttribute("order") @Valid OrderDTO orderDTO,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/discount-update";
        }
        orderService.setDiscount(id, orderDTO);
        return "redirect:/orders";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage());
        return "/404";
    }
}