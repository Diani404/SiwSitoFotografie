package it.uniroma3.siw.photoblog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.photoblog.model.Event;
import it.uniroma3.siw.photoblog.model.EventType;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByOrderByDateDesc();

    @Query("select e from Event e left join fetch e.cover order by e.date desc")
    List<Event> findAllWithCover();

    @Query("select e from Event e left join fetch e.cover where e.type = :type order by e.date desc")
    List<Event> findByTypeWithCover(@Param("type") EventType type);

    @Query("select e from Event e left join fetch e.cover where e.id = :id")
    Optional<Event> findByIdWithCover(@Param("id") Long id);

    //pacchetti nel carrello
    @Query("select e from Event e left join fetch e.cover where e.id in :ids order by e.date desc")
    List<Event> findAllByIdWithCover(@Param("ids") Iterable<Long> ids);

    //stessa cosa ma copertina fuori
    @EntityGraph(attributePaths = "cover")
    @Query("select e from Event e order by e.date desc")
    List<Event> findAllWithEntityGraph();
}
