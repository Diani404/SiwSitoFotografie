package it.uniroma3.siw.photoblog.exception;

public class EmptyCartException extends RuntimeException {

    public EmptyCartException() {
        super("Il carrello e' vuoto");
    }
}
