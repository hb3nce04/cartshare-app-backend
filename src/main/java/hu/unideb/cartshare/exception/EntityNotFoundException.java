package hu.unideb.cartshare.exception;

/**
 * Exception class for specific cases when a specific entity does not exist.
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
