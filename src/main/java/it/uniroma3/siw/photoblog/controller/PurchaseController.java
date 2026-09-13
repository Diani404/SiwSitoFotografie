package it.uniroma3.siw.photoblog.controller;

import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.photoblog.exception.PurchaseNotCancellableException;
import it.uniroma3.siw.photoblog.model.Credentials;
import it.uniroma3.siw.photoblog.model.Purchase;
import it.uniroma3.siw.photoblog.model.PurchaseStatus;
import it.uniroma3.siw.photoblog.model.User;
import it.uniroma3.siw.photoblog.service.CredentialsService;
import it.uniroma3.siw.photoblog.service.PurchaseService;

@Controller
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final CredentialsService credentialsService;

    public PurchaseController(PurchaseService purchaseService, CredentialsService credentialsService) {
        this.purchaseService = purchaseService;
        this.credentialsService = credentialsService;
    }

    @GetMapping("/purchases")
    public String list(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = credentialsService.getUser(userDetails.getUsername());
        model.addAttribute("purchases", purchaseService.findByUserId(user.getId()));
        return "purchases/list";
    }

    //il dettaglio è visibile al proprietario dell'ordine e all'admin
    @GetMapping("/purchases/{id}")
    public String show(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        Optional<Purchase> optional = purchaseService.findById(id);
        if (optional.isEmpty()) {
            return "redirect:/purchases";
        }
        Purchase purchase = optional.get();
        if (!isAdmin(userDetails)) {
            User user = credentialsService.getUser(userDetails.getUsername());
            purchaseService.checkOwner(purchase, user);
        }
        model.addAttribute("purchase", purchase);
        model.addAttribute("statuses", PurchaseStatus.values());
        return "purchases/show";
    }

    @PostMapping("/purchases/{id}/cancel")
    public String cancel(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = credentialsService.getUser(userDetails.getUsername());
        try {
            purchaseService.cancel(id, user);
        } catch (PurchaseNotCancellableException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return show(id, userDetails, model);
        }
        return "redirect:/purchases/" + id;
    }

    @GetMapping("/admin/purchases")
    public String adminList(Model model) {
        model.addAttribute("purchases", purchaseService.findAll());
        return "admin/purchases/list";
    }

    @PostMapping("/admin/purchases/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam PurchaseStatus status) {
        purchaseService.updateStatus(id, status);
        return "redirect:/purchases/" + id;
    }

    private boolean isAdmin(UserDetails userDetails) {
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            if (Credentials.ADMIN_ROLE.equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
