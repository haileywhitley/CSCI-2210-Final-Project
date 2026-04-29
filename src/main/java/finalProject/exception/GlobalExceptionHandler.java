package finalProject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralized exception handler for all REST controllers.
 *
 * <p>This class intercepts exceptions thrown anywhere in the application
 * and converts them into consistent JSON error responses so the client
 * always receives a structured error body with a timestamp, HTTP status
 * code, error name, and detail message.
 *
 * @author Abbey Yearout
 * @version Final Project
 * @bugs None
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ResourceNotFoundException and returns an HTTP 404 response.
     *
     * @param ex the exception that was thrown
     * @return a ResponseEntity containing a JSON error body with status 404
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles SchedulingConflictException and returns an HTTP 409 response.
     *
     * @param ex the exception that was thrown
     * @return a ResponseEntity containing a JSON error body with status 409
     */
    @ExceptionHandler(SchedulingConflictException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(SchedulingConflictException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    /**
     * Handles InvalidAppointmentException and returns an HTTP 400 response.
     *
     * @param ex the exception that was thrown
     * @return a ResponseEntity containing a JSON error body with status 400
     */
    @ExceptionHandler(InvalidAppointmentException.class)
    public ResponseEntity<Map<String, Object>> handleInvalid(InvalidAppointmentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Handles bean validation failures and returns an HTTP 400 response
     * with the first field-level validation message.
     *
     * @param ex the exception that was thrown by the validation framework
     * @return a ResponseEntity containing a JSON error body with status 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .findFirst().orElse("Validation error");
        return buildResponse(HttpStatus.BAD_REQUEST, msg);
    }

    /**
     * Handles all other uncaught exceptions and returns an HTTP 500 response.
     *
     * @param ex the exception that was thrown
     * @return a ResponseEntity containing a JSON error body with status 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred: " + ex.getMessage());
    }

    /**
     * Builds a standard JSON error response body containing a timestamp,
     * HTTP status code, error name, and detail message.
     *
     * @param status  the HTTP status to include in the response
     * @param message the detail message describing the error
     * @return a ResponseEntity wrapping the error body with the given status
     */
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
