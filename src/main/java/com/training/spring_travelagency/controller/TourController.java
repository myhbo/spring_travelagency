package com.training.spring_travelagency.controller;

import com.training.spring_travelagency.dto.TourDTO;
import com.training.spring_travelagency.entity.Tour;
import com.training.spring_travelagency.entity.enums.HotelType;
import com.training.spring_travelagency.entity.enums.TourType;
import com.training.spring_travelagency.service.TourService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@Log4j2
public class TourController {
    private final TourService tourService;
    private static final int[] PAGE_SIZES = {5, 10, 50, 100};

    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping("/tours")
    public String getToursPage(Model model,
                               @PageableDefault(sort = {"isHot"},
                                       direction = Sort.Direction.DESC,
                                       size = 10) Pageable pageable) {
        model.addAttribute("tours", tourService.findAllToursPageable(pageable));
        model.addAttribute("pageSizes", PAGE_SIZES);
        return "/tours";
    }

    @GetMapping("/tours/add")
    public String getAddTourPage(Model model,
                                 @ModelAttribute("tour") TourDTO tourDTO) {
        model.addAttribute("tourTypes", TourType.values());
        model.addAttribute("hotelTypes", HotelType.values());
        return "/tour-add";
    }

    @PostMapping("/tours/add")
    public String addTour(@ModelAttribute("tour") @Valid TourDTO tourDTO,
                          BindingResult bindingResult,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("tourTypes", TourType.values());
            model.addAttribute("hotelTypes", HotelType.values());
            return "/tour-add";
        }
        tourService.createTour(tourDTO);
        return "redirect:/tours";
    }

    @GetMapping("/tours/update/{id}")
    public String getTourUpdatePage(@PathVariable("id") long id,
                                    Model model) {
        Tour tour = tourService.getTourById(id);
        model.addAttribute("tour", tour);
        model.addAttribute("tourTypes", TourType.values());
        model.addAttribute("hotelTypes", HotelType.values());
        return "/tour-update";
    }

    @PostMapping("/tours/update/{id}")
    public String updateTour(@PathVariable("id") long id,
                             @ModelAttribute("tour") @Valid TourDTO tourDTO,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("tourTypes", TourType.values());
            model.addAttribute("hotelTypes", HotelType.values());
            return "/tour-update";
        }
        tourService.updateTour(id, tourDTO);
        return "redirect:/tours";
    }

    @GetMapping("/tours/delete/{id}")
    public String deleteTour(@PathVariable long id,
                             @RequestParam(value = "page", required = false) Long pageId,
                             @RequestParam(value = "size", required = false) Long size,
                             @RequestParam(value = "sort", required = false) String sort,
                             RedirectAttributes redirectAttributes) {
        tourService.deleteTour(id);
        redirectAttributes.addAttribute("page", pageId);
        redirectAttributes.addAttribute("size", size);
        redirectAttributes.addAttribute("sort", sort);
        return "redirect:/tours";
    }

    @GetMapping("/tours/toggle-hot/{id}")
    public String toggleHot(@PathVariable long id,
                            @RequestParam(value = "page", required = false) Long pageId,
                            @RequestParam(value = "size", required = false) Long size,
                            @RequestParam(value = "sort", required = false) String sort,
                            RedirectAttributes redirectAttributes) {
        tourService.toggleHot(id);
        redirectAttributes.addAttribute("page", pageId);
        redirectAttributes.addAttribute("size", size);
        redirectAttributes.addAttribute("sort", sort);
        return "redirect:/tours";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage());
        return "/404";
    }
}
