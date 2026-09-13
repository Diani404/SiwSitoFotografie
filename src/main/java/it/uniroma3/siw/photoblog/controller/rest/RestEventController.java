package it.uniroma3.siw.photoblog.controller.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.photoblog.dto.EventDto;
import it.uniroma3.siw.photoblog.dto.PhotoDto;
import it.uniroma3.siw.photoblog.model.Event;
import it.uniroma3.siw.photoblog.model.Photo;
import it.uniroma3.siw.photoblog.service.EventService;
import it.uniroma3.siw.photoblog.service.PhotoService;

@RestController
@RequestMapping("/api/events")
public class RestEventController {

    private final EventService eventService;
    private final PhotoService photoService;

    public RestEventController(EventService eventService, PhotoService photoService) {
        this.eventService = eventService;
        this.photoService = photoService;
    }

    @GetMapping
    public List<EventDto> list() {
        List<EventDto> result = new ArrayList<>();
        for (Event event : eventService.findAll()) {
            result.add(EventDto.from(event));
        }
        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> show(@PathVariable Long id) {
        Optional<Event> optional = eventService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(EventDto.from(optional.get()));
    }

    //tutte le foto dell'evento
    @GetMapping("/{id}/photos")
    public ResponseEntity<List<PhotoDto>> photos(@PathVariable Long id) {
        Optional<Event> optional = eventService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Photo cover = optional.get().getCover();
        Long coverId = cover == null ? null : cover.getId();
        List<PhotoDto> result = new ArrayList<>();
        for (Photo photo : photoService.findByEventId(id)) {
            result.add(PhotoDto.from(photo, photo.getId().equals(coverId)));
        }
        return ResponseEntity.ok(result);
    }
}
