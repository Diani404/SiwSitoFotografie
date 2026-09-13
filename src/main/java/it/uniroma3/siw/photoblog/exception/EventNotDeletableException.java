package it.uniroma3.siw.photoblog.exception;

public class EventNotDeletableException extends RuntimeException {

    public EventNotDeletableException(String eventTitle) {
        super("Il pacchetto di '" + eventTitle + "' e' stato acquistato da un cliente: l'evento non puo' essere eliminato");
    }
}
