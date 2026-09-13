package it.uniroma3.siw.photoblog.analysis;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.photoblog.model.Event;
import it.uniroma3.siw.photoblog.model.Purchase;
import it.uniroma3.siw.photoblog.repository.EventRepository;
import it.uniroma3.siw.photoblog.repository.PurchaseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;

@Service
public class FetchStrategyAnalysisService {

    @PersistenceContext
    private EntityManager entityManager;

    private final EntityManagerFactory entityManagerFactory;
    private final EventRepository eventRepository;
    private final PurchaseRepository purchaseRepository;

    public FetchStrategyAnalysisService(EntityManagerFactory entityManagerFactory, EventRepository eventRepository,
            PurchaseRepository purchaseRepository) {
        this.entityManagerFactory = entityManagerFactory;
        this.eventRepository = eventRepository;
        this.purchaseRepository = purchaseRepository;
    }

    //elenco eventi con copertina (pagina /events)
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public StrategyResult eventsLazy() {
        Statistics statistics = reset();
        long start = System.nanoTime();
        List<Event> events = eventRepository.findAllByOrderByDateDesc();
        touchCovers(events);
        return result("LAZY", events.size(), statistics, start);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public StrategyResult eventsJoinFetch() {
        Statistics statistics = reset();
        long start = System.nanoTime();
        List<Event> events = eventRepository.findAllWithCover();
        touchCovers(events);
        return result("JOIN FETCH", events.size(), statistics, start);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public StrategyResult eventsEntityGraph() {
        Statistics statistics = reset();
        long start = System.nanoTime();
        List<Event> events = eventRepository.findAllWithEntityGraph();
        touchCovers(events);
        return result("ENTITY GRAPH", events.size(), statistics, start);
    }

    //ordini di un utente con i pacchetti acquistati (pagina /purchases)
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public StrategyResult purchasesLazy(Long userId) {
        Statistics statistics = reset();
        long start = System.nanoTime();
        List<Purchase> purchases = purchaseRepository.findByUser_IdOrderByDateDesc(userId);
        touchEvents(purchases);
        return result("LAZY", purchases.size(), statistics, start);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public StrategyResult purchasesJoinFetch(Long userId) {
        Statistics statistics = reset();
        long start = System.nanoTime();
        List<Purchase> purchases = purchaseRepository.findByUserIdWithEvents(userId);
        touchEvents(purchases);
        return result("JOIN FETCH", purchases.size(), statistics, start);
    }

    //accesso alle associazioni x caricamento LAZY
    private void touchCovers(List<Event> events) {
        for (Event event : events) {
            if (event.getCover() != null) {
                event.getCover().getImageUrl();
            }
        }
    }

    private void touchEvents(List<Purchase> purchases) {
        for (Purchase purchase : purchases) {
            for (Event event : purchase.getEvents()) {
                event.getTitle();
                if (event.getCover() != null) {
                    event.getCover().getImageUrl();
                }
            }
        }
    }

    //persistence context e statistiche azzerati per cache vuota
    private Statistics reset() {
        entityManager.clear();
        Statistics statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
        statistics.clear();
        return statistics;
    }

    private StrategyResult result(String strategy, int loaded, Statistics statistics, long start) {
        long millis = (System.nanoTime() - start) / 1_000_000;
        return new StrategyResult(strategy, loaded, statistics.getPrepareStatementCount(), millis);
    }
}
