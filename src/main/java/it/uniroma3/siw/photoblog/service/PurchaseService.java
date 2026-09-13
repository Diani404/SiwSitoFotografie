package it.uniroma3.siw.photoblog.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.photoblog.exception.AlreadyBoughtException;
import it.uniroma3.siw.photoblog.exception.EmptyCartException;
import it.uniroma3.siw.photoblog.exception.PurchaseNotCancellableException;
import it.uniroma3.siw.photoblog.exception.PurchaseNotOwnedException;
import it.uniroma3.siw.photoblog.model.Event;
import it.uniroma3.siw.photoblog.model.Purchase;
import it.uniroma3.siw.photoblog.model.PurchaseStatus;
import it.uniroma3.siw.photoblog.model.User;
import it.uniroma3.siw.photoblog.repository.EventRepository;
import it.uniroma3.siw.photoblog.repository.PurchaseRepository;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final EventRepository eventRepository;

    public PurchaseService(PurchaseRepository purchaseRepository, EventRepository eventRepository) {
        this.purchaseRepository = purchaseRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional(readOnly = true)
    public List<Purchase> findByUserId(Long userId) {
        return purchaseRepository.findByUserIdWithEvents(userId);
    }

    @Transactional(readOnly = true)
    public List<Purchase> findAll() {
        return purchaseRepository.findAllWithUserAndEvents();
    }

    @Transactional(readOnly = true)
    public Optional<Purchase> findById(Long id) {
        return purchaseRepository.findByIdWithDetails(id);
    }

    @Transactional(readOnly = true)
    public boolean alreadyBought(Long userId, Long eventId) {
        return purchaseRepository.alreadyBought(userId, eventId);
    }

    @Transactional
    public Purchase create(User user, Collection<Long> eventIds) throws EmptyCartException, AlreadyBoughtException {
        if (eventIds.isEmpty()) {
            throw new EmptyCartException();
        }
        List<Event> events = eventRepository.findAllByIdWithCover(eventIds);
        if (events.size() != eventIds.size()) {
            throw new IllegalStateException("Alcuni pacchetti del carrello non esistono piu'");
        }
        BigDecimal total = BigDecimal.ZERO;
        for (Event event : events) {
            if (purchaseRepository.alreadyBought(user.getId(), event.getId())) {
                throw new AlreadyBoughtException(event.getTitle());
            }
            total = total.add(event.getPrice());
        }
        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setEvents(new ArrayList<>(events));
        purchase.setTotal(total);
        purchase.setDate(LocalDateTime.now());
        purchase.setStatus(PurchaseStatus.PENDING);
        return purchaseRepository.save(purchase);
    }

    //l'utente annulla un proprio ordine
    @Transactional
    public void cancel(Long id, User user) throws PurchaseNotOwnedException, PurchaseNotCancellableException {
        Purchase purchase = purchaseRepository.findByIdWithDetails(id).orElseThrow();
        checkOwner(purchase, user);
        if (!purchase.isPending()) {
            throw new PurchaseNotCancellableException();
        }
        purchase.setStatus(PurchaseStatus.CANCELLED);
    }

    //il fotografo aggiorna lo stato
    @Transactional
    public void updateStatus(Long id, PurchaseStatus status) {
        Purchase purchase = purchaseRepository.findById(id).orElseThrow();
        purchase.setStatus(status);
    }

    public void checkOwner(Purchase purchase, User user) throws PurchaseNotOwnedException {
        if (!purchase.getUser().getId().equals(user.getId())) {
            throw new PurchaseNotOwnedException();
        }
    }
}
