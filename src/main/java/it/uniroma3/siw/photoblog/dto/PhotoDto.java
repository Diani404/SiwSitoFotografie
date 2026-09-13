package it.uniroma3.siw.photoblog.dto;

import it.uniroma3.siw.photoblog.model.Photo;

public record PhotoDto(Long id, String title, String description, String imageUrl, boolean cover) {

    public static PhotoDto from(Photo p, boolean cover) {
        return new PhotoDto(p.getId(), p.getTitle(), p.getDescription(), p.getImageUrl(), cover);
    }
}
