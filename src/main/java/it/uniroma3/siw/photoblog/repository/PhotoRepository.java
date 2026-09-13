package it.uniroma3.siw.photoblog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.photoblog.model.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    List<Photo> findByEvent_IdOrderByIdAsc(Long eventId);

    long countByEvent_Id(Long eventId);

    @Query("select p from Photo p join fetch p.event where p.id = :id")
    Optional<Photo> findByIdWithEvent(@Param("id") Long id);
}
