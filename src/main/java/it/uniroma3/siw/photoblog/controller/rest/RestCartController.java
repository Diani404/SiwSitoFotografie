package it.uniroma3.siw.photoblog.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.photoblog.dto.CartDto;
import it.uniroma3.siw.photoblog.model.Cart;
import it.uniroma3.siw.photoblog.service.EventService;

@RestController
@RequestMapping("/api/cart")
public class RestCartController {

    private final Cart cart;
    private final EventService eventService;

    public RestCartController(Cart cart, EventService eventService) {
        this.cart = cart;
        this.eventService = eventService;
    }

    @GetMapping
    public CartDto show() {
        return CartDto.from(cart);
    }

    @PostMapping("/items/{eventId}")
    public ResponseEntity<CartDto> add(@PathVariable Long eventId) {
        if (eventService.findById(eventId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        cart.add(eventId);
        return ResponseEntity.ok(CartDto.from(cart));
    }

    @DeleteMapping("/items/{eventId}")
    public CartDto remove(@PathVariable Long eventId) {
        cart.remove(eventId);
        return CartDto.from(cart);
    }
}
