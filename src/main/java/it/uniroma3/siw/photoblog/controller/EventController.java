package it.uniroma3.siw.photoblog.controller;

import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.photoblog.exception.EventNotDeletableException;
import it.uniroma3.siw.photoblog.model.Cart;
import it.uniroma3.siw.photoblog.model.Event;
import it.uniroma3.siw.photoblog.model.EventType;
import it.uniroma3.siw.photoblog.model.Photo;
import it.uniroma3.siw.photoblog.model.User;
import it.uniroma3.siw.photoblog.service.CredentialsService;
import it.uniroma3.siw.photoblog.service.EventService;
import it.uniroma3.siw.photoblog.service.PhotoService;
import it.uniroma3.siw.photoblog.service.PurchaseService;
import jakarta.validation.Valid;

@Controller
public class EventController {

    private final EventService eventService;
    private final PhotoService photoService;
    private final PurchaseService purchaseService;
    private final CredentialsService credentialsService;
    private final Cart cart;

    public EventController(EventService eventService, PhotoService photoService, PurchaseService purchaseService,
            CredentialsService credentialsService, Cart cart) {
        this.eventService = eventService;
        this.photoService = photoService;
        this.purchaseService = purchaseService;
        this.credentialsService = credentialsService;
        this.cart = cart;
    }

    @GetMapping("/events")
    public String list(@RequestParam(required = false) EventType type, Model model) {
        model.addAttribute("events", eventService.findByType(type));
        model.addAttribute("types", EventType.values());
        model.addAttribute("selectedType", type);
        return "events/list";
    }

    @GetMapping("/events/{id}")
    public String show(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        Optional<Event> optional = eventService.findById(id);
        if (optional.isEmpty()) {
            return "redirect:/events";
        }
        model.addAttribute("event", optional.get());
        model.addAttribute("photoCount", photoService.countByEventId(id));
        if (userDetails != null) {
            User user = credentialsService.getUser(userDetails.getUsername());
            model.addAttribute("photos", photoService.findByEventId(id));
            model.addAttribute("inCart", cart.contains(id));
            model.addAttribute("alreadyOrdered", purchaseService.alreadyBought(user.getId(), id));
        }
        return "events/show";
    }

    //versione React
    @GetMapping("/events/{id}/gallery")
    public String gallery(@PathVariable Long id, Model model) {
        Optional<Event> optional = eventService.findById(id);
        if (optional.isEmpty()) {
            return "redirect:/events";
        }
        model.addAttribute("event", optional.get());
        return "events/gallery";
    }

    @GetMapping("/admin/events")
    public String adminList(Model model) {
        model.addAttribute("events", eventService.findAll());
        return "admin/events/list";
    }

    @GetMapping("/admin/events/new")
    public String createForm(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("types", EventType.values());
        return "admin/events/form";
    }

    @GetMapping("/admin/events/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Optional<Event> optional = eventService.findById(id);
        if (optional.isEmpty()) {
            return "redirect:/admin/events";
        }
        model.addAttribute("event", optional.get());
        model.addAttribute("types", EventType.values());
        return "admin/events/form";
    }

    @PostMapping("/admin/events")
    public String save(@Valid @ModelAttribute("event") Event event, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("types", EventType.values());
            return "admin/events/form";
        }
        Event saved = eventService.save(event);
        return "redirect:/admin/events/" + saved.getId() + "/photos";
    }

    //gestione foto di un evento
    @GetMapping("/admin/events/{id}/photos")
    public String managePhotos(@PathVariable Long id, Model model) {
        Optional<Event> optional = eventService.findById(id);
        if (optional.isEmpty()) {
            return "redirect:/admin/events";
        }
        model.addAttribute("event", optional.get());
        model.addAttribute("photos", photoService.findByEventId(id));
        if (!model.containsAttribute("photo")) {
            model.addAttribute("photo", new Photo());
        }
        return "admin/photos/manage";
    }

    @PostMapping("/admin/events/{id}/cover")
    public String setCover(@PathVariable Long id, @RequestParam Long photoId) {
        eventService.setCover(id, photoId);
        return "redirect:/admin/events/" + id + "/photos";
    }

    @PostMapping("/admin/events/{id}/delete")
    public String delete(@PathVariable Long id, Model model) {
        try {
            eventService.delete(id);
            return "redirect:/admin/events";
        } catch (EventNotDeletableException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return adminList(model);
        }
    }
}
