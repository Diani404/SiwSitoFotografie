package it.uniroma3.siw.photoblog.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(AlreadyBoughtException.class)
    public ResponseEntity<Map<String, Object>> handleAlreadyBought(AlreadyBoughtException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body(409, e.getMessage()));
    }

    @ExceptionHandler(EmptyCartException.class)
    public ResponseEntity<Map<String, Object>> handleEmptyCart(EmptyCartException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body(400, e.getMessage()));
    }

    @ExceptionHandler(PurchaseNotOwnedException.class)
    public ResponseEntity<Map<String, Object>> handlePurchaseNotOwned(PurchaseNotOwnedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body(403, e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception e) {
        logger.error("Errore non gestito nelle API", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body(500, "Errore interno del server"));
    }

    private Map<String, Object> body(int status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status);
        body.put("message", message);
        return body;
    }
}
