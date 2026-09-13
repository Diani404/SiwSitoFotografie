package it.uniroma3.siw.photoblog.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.photoblog.exception.InvalidPhotoFileException;
import it.uniroma3.siw.photoblog.model.Event;
import it.uniroma3.siw.photoblog.model.Photo;
import it.uniroma3.siw.photoblog.service.EventService;
import it.uniroma3.siw.photoblog.service.PhotoService;
import jakarta.validation.Valid;

@Controller
public class PhotoController {

    private final PhotoService photoService;
    private final EventService eventService;

    public PhotoController(PhotoService photoService, EventService eventService) {
        this.photoService = photoService;
        this.eventService = eventService;
    }

    @GetMapping("/photos/{id}")
    public String show(@PathVariable Long id, Model model) {
        Optional<Photo> optional = photoService.findById(id);
        if (optional.isEmpty()) {
            return "redirect:/events";
        }
        model.addAttribute("photo", optional.get());
        return "photos/show";
    }

    //caricamento di una nuova foto in un evento (il file non fa parte dell'entita')
    @PostMapping("/admin/events/{eventId}/photos")
    public String save(@PathVariable Long eventId, @Valid @ModelAttribute("photo") Photo photo,
            BindingResult bindingResult, @RequestParam("file") MultipartFile file, Model model) {
        Optional<Event> optional = eventService.findById(eventId);
        if (optional.isEmpty()) {
            return "redirect:/admin/events";
        }
        if (!bindingResult.hasErrors()) {
            try {
                photoService.save(photo, eventId, file);
                return "redirect:/admin/events/" + eventId + "/photos";
            } catch (InvalidPhotoFileException e) {
                bindingResult.reject("photo.file", e.getMessage());
            }
        }
        model.addAttribute("event", optional.get());
        model.addAttribute("photos", photoService.findByEventId(eventId));
        return "admin/photos/manage";
    }

    @GetMapping("/admin/photos/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Optional<Photo> optional = photoService.findById(id);
        if (optional.isEmpty()) {
            return "redirect:/admin/events";
        }
        model.addAttribute("photo", optional.get());
        model.addAttribute("event", optional.get().getEvent());
        return "admin/photos/form";
    }

    @PostMapping("/admin/photos/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("photo") Photo photo,
            BindingResult bindingResult, Model model) {
        Optional<Photo> optional = photoService.findById(id);
        if (optional.isEmpty()) {
            return "redirect:/admin/events";
        }
        Event event = optional.get().getEvent();
        if (bindingResult.hasErrors()) {
            photo.setId(id);
            photo.setImageUrl(optional.get().getImageUrl());
            model.addAttribute("event", event);
            return "admin/photos/form";
        }
        photoService.update(id, photo);
        return "redirect:/admin/events/" + event.getId() + "/photos";
    }

    @PostMapping("/admin/photos/{id}/delete")
    public String delete(@PathVariable Long id) {
        Optional<Photo> optional = photoService.findById(id);
        if (optional.isEmpty()) {
            return "redirect:/admin/events";
        }
        Long eventId = photoService.delete(id);
        return "redirect:/admin/events/" + eventId + "/photos";
    }
}
