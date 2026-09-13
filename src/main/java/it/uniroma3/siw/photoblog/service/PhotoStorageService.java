package it.uniroma3.siw.photoblog.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.photoblog.exception.InvalidPhotoFileException;

@Service
public class PhotoStorageService {

    public static final String UPLOADS_URL = "/uploads/";

    private final Path uploadDir;

    public PhotoStorageService(@Value("${app.upload-dir}") String uploadDir) {
        this.uploadDir = Paths.get(uploadDir);
    }

    public String save(MultipartFile file) throws InvalidPhotoFileException {
        if (file == null || file.isEmpty()) {
            throw new InvalidPhotoFileException("Devi scegliere un file immagine");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidPhotoFileException("Il file deve essere un'immagine (jpg, png, webp...)");
        }
        String extension = "";
        String originalName = file.getOriginalFilename();
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf('.')).toLowerCase();
        }
        // nome generato
        String fileName = "foto-" + UUID.randomUUID() + extension;
        try {
            Files.createDirectories(uploadDir);
            Files.copy(file.getInputStream(), uploadDir.resolve(fileName));
        } catch (IOException e) {
            throw new InvalidPhotoFileException("Impossibile salvare il file: " + e.getMessage());
        }
        return UPLOADS_URL + fileName;
    }

    //cancella il file solo se e' uno di quelli caricati non demo
    public void delete(String imageUrl) {
        if (imageUrl == null || !imageUrl.startsWith(UPLOADS_URL)) {
            return;
        }
        try {
            Files.deleteIfExists(uploadDir.resolve(imageUrl.substring(UPLOADS_URL.length())));
        } catch (IOException e) {
        }
    }
}
