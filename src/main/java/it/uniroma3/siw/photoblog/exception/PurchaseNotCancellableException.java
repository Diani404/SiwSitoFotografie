package it.uniroma3.siw.photoblog.exception;

public class PurchaseNotCancellableException extends RuntimeException {

    public PurchaseNotCancellableException() {
        super("L'ordine non e' piu' in attesa e non puo' essere annullato");
    }
}
