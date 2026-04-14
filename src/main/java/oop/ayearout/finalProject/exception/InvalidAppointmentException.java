package oop.ayearout.finalProject.exception;

/**
 * Thrown when appointment data fails a business-rule validation check,
 * such as a start time that is not before the end time.
 *
 * <p>This exception is caught by the GlobalExceptionHandler and returned
 * to the client as an HTTP 400 Bad Request response.
 *
 * @author Abbey Yearout
 * @version Final Project
 * @bugs None
 */
public class InvalidAppointmentException extends RuntimeException {

    /**
     * Creates a new InvalidAppointmentException with the given detail message.
     *
     * @param message a description of the validation rule that was violated
     */
    public InvalidAppointmentException(String message) {
        super(message);
    }
}
