package it.uniroma3.siw.photoblog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.photoblog.exception.InvalidPhotoFileException;
import it.uniroma3.siw.photoblog.model.Event;
import it.uniroma3.siw.photoblog.model.Photo;
import it.uniroma3.siw.photoblog.repository.EventRepository;
import it.uniroma3.siw.photoblog.repository.PhotoRepository;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final EventRepository eventRepository;
    private final PhotoStorageService photoStorageService;

    public PhotoService(PhotoRepository photoRepository, EventRepository eventRepository,
            PhotoStorageService photoStorageService) {
        this.photoRepository = photoRepository;
        this.eventRepository = eventRepository;
        this.photoStorageService = photoStorageService;
    }

    @Transactional(readOnly = true)
    public List<Photo> findByEventId(Long eventId) {
        return photoRepository.findByEvent_IdOrderByIdAsc(eventId);
    }

    @Transactional(readOnly = true)
    public long countByEventId(Long eventId) {
        return photoRepository.countByEvent_Id(eventId);
    }

    @Transactional(readOnly = true)
    public Optional<Photo> findById(Long id) {
        return photoRepository.findByIdWithEvent(id);
    }

    //il file viene salvato su disco e la foto agganciata all'evento
    //se l'evento non ha ancora una copertina, la prima foto caricata lo diventa
    @Transactional
    public Photo save(Photo photo, Long eventId, MultipartFile file) throws InvalidPhotoFileException {
        Event event = eventRepository.findById(eventId).orElseThrow();
        photo.setImageUrl(photoStorageService.save(file));
        photo.setEvent(event);
        Photo saved = photoRepository.save(photo);
        if (event.getCover() == null) {
            event.setCover(saved);
        }
        return saved;
    }

    @Transactional
    public Photo update(Long id, Photo data) {
        Photo existing = photoRepository.findByIdWithEvent(id).orElseThrow();
        existing.setTitle(data.getTitle());
        existing.setDescription(data.getDescription());
        return existing;
    }

    @Transactional
    public Long delete(Long id) {
        Photo photo = photoRepository.findByIdWithEvent(id).orElseThrow();
        Event event = photo.getEvent();
        // confronto per id: la copertina di un evento caricato con join fetch e' un proxy, equals() non basta
        if (event.getCover() != null && event.getCover().getId().equals(photo.getId())) {
            event.setCover(null);
            eventRepository.flush();
        }
        photoRepository.delete(photo);
        photoStorageService.delete(photo.getImageUrl());
        return event.getId();
    }
}
