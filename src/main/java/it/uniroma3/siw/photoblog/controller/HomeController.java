package it.uniroma3.siw.photoblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.photoblog.service.EventService;

@Controller
public class HomeController {

    private final EventService eventService;

    public HomeController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping({ "/", "/index" })
    public String getHome(Model model) {
        model.addAttribute("events", eventService.findLatest(3));
        return "index";
    }
}
