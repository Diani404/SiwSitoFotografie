package it.uniroma3.siw.photoblog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.photoblog.model.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findByUser_IdOrderByDateDesc(Long userId);

    //un evento gia' venduto non si puo' cancellare
    boolean existsByEvents_Id(Long eventId);

    //ha gia' comprato questo pacchetto? (ordini non annullati)
    @Query("select count(p) > 0 from Purchase p join p.events e "
            + "where p.user.id = :userId and e.id = :eventId and p.status <> it.uniroma3.siw.photoblog.model.PurchaseStatus.CANCELLED")
    boolean alreadyBought(@Param("userId") Long userId, @Param("eventId") Long eventId);

    @Query("select distinct p from Purchase p left join fetch p.events where p.user.id = :userId order by p.date desc")
    List<Purchase> findByUserIdWithEvents(@Param("userId") Long userId);

    @Query("select distinct p from Purchase p join fetch p.user left join fetch p.events order by p.date desc")
    List<Purchase> findAllWithUserAndEvents();

    @Query("select distinct p from Purchase p join fetch p.user left join fetch p.events e left join fetch e.cover where p.id = :id")
    Optional<Purchase> findByIdWithDetails(@Param("id") Long id);
}
