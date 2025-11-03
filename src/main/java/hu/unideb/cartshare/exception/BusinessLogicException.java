package hu.unideb.cartshare.exception;

/**
 * Exception class for business logic exceptions.
 */
public class BusinessLogicException extends RuntimeException {
    public BusinessLogicException(String message) {
        super(message);
    }
}
