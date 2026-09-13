package it.uniroma3.siw.photoblog.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.photoblog.exception.EventNotDeletableException;
import it.uniroma3.siw.photoblog.model.Event;
import it.uniroma3.siw.photoblog.model.EventType;
import it.uniroma3.siw.photoblog.model.Photo;
import it.uniroma3.siw.photoblog.repository.EventRepository;
import it.uniroma3.siw.photoblog.repository.PhotoRepository;
import it.uniroma3.siw.photoblog.repository.PurchaseRepository;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final PhotoRepository photoRepository;
    private final PurchaseRepository purchaseRepository;
    private final PhotoStorageService photoStorageService;

    public EventService(EventRepository eventRepository, PhotoRepository photoRepository,
            PurchaseRepository purchaseRepository, PhotoStorageService photoStorageService) {
        this.eventRepository = eventRepository;
        this.photoRepository = photoRepository;
        this.purchaseRepository = purchaseRepository;
        this.photoStorageService = photoStorageService;
    }

    @Transactional(readOnly = true)
    public List<Event> findAll() {
        return eventRepository.findAllWithCover();
    }

    @Transactional(readOnly = true)
    public List<Event> findByType(EventType type) {
        return type == null ? eventRepository.findAllWithCover() : eventRepository.findByTypeWithCover(type);
    }

    @Transactional(readOnly = true)
    public List<Event> findLatest(int howMany) {
        List<Event> events = eventRepository.findAllWithCover();
        return events.size() > howMany ? events.subList(0, howMany) : events;
    }

    @Transactional(readOnly = true)
    public Optional<Event> findById(Long id) {
        return eventRepository.findByIdWithCover(id);
    }

    @Transactional(readOnly = true)
    public List<Event> findAllById(Collection<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        return eventRepository.findAllByIdWithCover(ids);
    }

    @Transactional
    public Event save(Event event) {
        if (event.getId() == null) {
            return eventRepository.save(event);
        }
        //l'oggetto che arriva dal form non ha cover e foto
        Event existing = eventRepository.findById(event.getId()).orElseThrow();
        existing.setTitle(event.getTitle());
        existing.setType(event.getType());
        existing.setDate(event.getDate());
        existing.setLocation(event.getLocation());
        existing.setDescription(event.getDescription());
        existing.setPrice(event.getPrice());
        return existing;
    }

    @Transactional
    public void setCover(Long eventId, Long photoId) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        Photo photo = photoRepository.findByIdWithEvent(photoId).orElseThrow();
        if (photo.getEvent().getId().equals(event.getId())) {
            event.setCover(photo);
        }
    }

    //cancella l'evento con tutte le sue foto (anche i file su disco);
    //bloccato se il pacchetto e' gia' stato acquistato da qualcuno
    @Transactional
    public void delete(Long id) throws EventNotDeletableException {
        Event event = eventRepository.findByIdWithCover(id).orElseThrow();
        if (purchaseRepository.existsByEvents_Id(id)) {
            throw new EventNotDeletableException(event.getTitle());
        }
        List<Photo> photos = photoRepository.findByEvent_IdOrderByIdAsc(id);
        // la copertina punta a una foto: va staccata prima di cancellare le foto (chiave esterna)
        event.setCover(null);
        eventRepository.flush();
        photoRepository.deleteAll(photos);
        eventRepository.delete(event);
        for (Photo photo : photos) {
            photoStorageService.delete(photo.getImageUrl());
        }
    }
}
