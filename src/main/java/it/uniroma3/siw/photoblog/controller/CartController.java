package it.uniroma3.siw.photoblog.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.photoblog.exception.EmptyCartException;
import it.uniroma3.siw.photoblog.exception.AlreadyBoughtException;
import it.uniroma3.siw.photoblog.model.Cart;
import it.uniroma3.siw.photoblog.model.Event;
import it.uniroma3.siw.photoblog.model.Purchase;
import it.uniroma3.siw.photoblog.model.User;
import it.uniroma3.siw.photoblog.service.CredentialsService;
import it.uniroma3.siw.photoblog.service.EventService;
import it.uniroma3.siw.photoblog.service.PurchaseService;

@Controller
public class CartController {

    private final Cart cart;
    private final EventService eventService;
    private final PurchaseService purchaseService;
    private final CredentialsService credentialsService;

    public CartController(Cart cart, EventService eventService, PurchaseService purchaseService,
            CredentialsService credentialsService) {
        this.cart = cart;
        this.eventService = eventService;
        this.purchaseService = purchaseService;
        this.credentialsService = credentialsService;
    }

    @GetMapping("/cart")
    public String show(Model model) {
        List<Event> events = eventService.findAllById(cart.getEventIds());
        BigDecimal total = BigDecimal.ZERO;
        for (Event event : events) {
            total = total.add(event.getPrice());
        }
        model.addAttribute("events", events);
        model.addAttribute("total", total);
        return "cart/show";
    }

    @PostMapping("/cart/add/{eventId}")
    public String add(@PathVariable Long eventId) {
        if (eventService.findById(eventId).isPresent()) {
            cart.add(eventId);
        }
        return "redirect:/events/" + eventId;
    }

    @PostMapping("/cart/remove/{eventId}")
    public String remove(@PathVariable Long eventId) {
        cart.remove(eventId);
        return "redirect:/cart";
    }

    //il carrello diventa persistente e poi viene svuotato
    @PostMapping("/cart/checkout")
    public String checkout(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        try {
            User user = credentialsService.getUser(userDetails.getUsername());
            Purchase purchase = purchaseService.create(user, cart.getEventIds());
            cart.clear();
            return "redirect:/purchases/" + purchase.getId();
        } catch (EmptyCartException | AlreadyBoughtException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return show(model);
        }
    }
}
