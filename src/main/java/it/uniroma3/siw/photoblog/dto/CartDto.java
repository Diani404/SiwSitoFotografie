package it.uniroma3.siw.photoblog.dto;

import java.util.List;

import it.uniroma3.siw.photoblog.model.Cart;

public record CartDto(List<Long> eventIds, int count) {

    public static CartDto from(Cart cart) {
        return new CartDto(List.copyOf(cart.getEventIds()), cart.size());
    }
}
