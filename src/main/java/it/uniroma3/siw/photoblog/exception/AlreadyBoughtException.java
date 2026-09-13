package it.uniroma3.siw.photoblog.exception;

public class AlreadyBoughtException extends RuntimeException {

    public AlreadyBoughtException(String eventTitle) {
        super("Hai gia' acquistato il pacchetto di '" + eventTitle + "': lo trovi nei tuoi ordini");
    }
}
