package it.uniroma3.siw.photoblog.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import it.uniroma3.siw.photoblog.model.Event;

public record EventDto(Long id, String title, String type, LocalDate date, String location, String description,
        BigDecimal price, String coverUrl) {

    // richiede un evento caricato con la copertina
    public static EventDto from(Event e) {
        String coverUrl = e.getCover() == null ? null : e.getCover().getImageUrl();
        return new EventDto(e.getId(), e.getTitle(), e.getType().name(), e.getDate(), e.getLocation(),
                e.getDescription(), e.getPrice(), coverUrl);
    }
}
