package it.uniroma3.siw.photoblog.exception;

public class PurchaseNotOwnedException extends RuntimeException {

    public PurchaseNotOwnedException() {
        super("Puoi vedere e annullare solo i tuoi ordini");
    }
}
