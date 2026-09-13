package it.uniroma3.siw.photoblog.analysis;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.photoblog.model.Purchase;
import it.uniroma3.siw.photoblog.repository.PurchaseRepository;

@Component
@Profile("analysis")
public class FetchStrategyAnalysis implements CommandLineRunner {

    private final FetchStrategyAnalysisService analysisService;
    private final PurchaseRepository purchaseRepository;

    public FetchStrategyAnalysis(FetchStrategyAnalysisService analysisService, PurchaseRepository purchaseRepository) {
        this.analysisService = analysisService;
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public void run(String... args) {
        List<Purchase> purchases = purchaseRepository.findAllWithUserAndEvents();
        if (purchases.isEmpty()) {
            System.out.println("Nessun ordine nel database: analisi non eseguita");
            return;
        }
        Long userId = purchases.get(0).getUser().getId();

        //prima a vuoto per creare cache di hibernate
        analysisService.eventsJoinFetch();
        analysisService.purchasesJoinFetch(userId);

        System.out.println();
        System.out.println("Query 1: elenco eventi con la foto di copertina");
        print(List.of(
                analysisService.eventsLazy(),
                analysisService.eventsJoinFetch(),
                analysisService.eventsEntityGraph()));
        System.out.println();
        System.out.println("Query 2: ordini dell'utente " + userId + " con i pacchetti acquistati (e le copertine)");
        print(List.of(
                analysisService.purchasesLazy(userId),
                analysisService.purchasesJoinFetch(userId)));
        System.out.println();
    }

    private void print(List<StrategyResult> results) {
        for (StrategyResult result : results) {
            System.out.println(result.format());
        }
    }
}
