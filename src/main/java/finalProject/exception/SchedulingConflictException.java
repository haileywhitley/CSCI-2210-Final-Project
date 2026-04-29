package finalProject.exception;

/**
 * Thrown when a new or rescheduled appointment overlaps with an existing
 * appointment for the same provider.
 *
 * <p>This exception is caught by the GlobalExceptionHandler and returned
 * to the client as an HTTP 409 Conflict response.
 *
 * @author Abbey Yearout
 * @version Final Project
 * @bugs None
 */
public class SchedulingConflictException extends RuntimeException {

    /**
     * Creates a new SchedulingConflictException with the given detail message.
     *
     * @param message a description of the conflicting appointment
     */
    public SchedulingConflictException(String message) {
        super(message);
    }
}
