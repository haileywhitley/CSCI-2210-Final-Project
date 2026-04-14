package oop.ayearout.finalProject.exception;

/**
 * Thrown when a requested patient, provider, or appointment does not exist
 * in the database.
 *
 * <p>This exception is caught by the GlobalExceptionHandler and returned
 * to the client as an HTTP 404 Not Found response.
 *
 * @author Abbey Yearout
 * @version Final Project
 * @bugs None
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Creates a new ResourceNotFoundException with the given detail message.
     *
     * @param message a description of which resource was not found
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
