package it.uniroma3.siw.photoblog.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.uniroma3.siw.photoblog.model.Cart;
import it.uniroma3.siw.photoblog.model.Credentials;

@ControllerAdvice
public class GlobalController {

    private final Cart cart;

    public GlobalController(Cart cart) {
        this.cart = cart;
    }

    @ModelAttribute("userDetails")
    public UserDetails getUser() {
        UserDetails user = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            user = (UserDetails) authentication.getPrincipal();
        }
        return user;
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (Credentials.ADMIN_ROLE.equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    //numero di foto nel carrello
    @ModelAttribute("cartCount")
    public int cartCount() {
        return getUser() == null ? 0 : cart.size();
    }
}
