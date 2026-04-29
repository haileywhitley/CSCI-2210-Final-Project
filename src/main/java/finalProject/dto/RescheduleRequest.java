package finalProject.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Data transfer object for rescheduling an existing appointment.
 *
 * <p>Contains only the new start and end times. The service layer
 * re-validates all business rules with the new times before saving,
 * excluding the appointment being updated from the conflict check.
 *
 * @author Abbey Yearout
 * @version Final Project
 * @bugs None
 */
public class RescheduleRequest {

    @NotNull(message = "startDateTime is required")
    private LocalDateTime startDateTime;

    @NotNull(message = "endDateTime is required")
    private LocalDateTime endDateTime;

    // Getters and setters

    /**
     * Returns the new start date and time for the appointment.
     *
     * @return the new start date and time
     */
    public LocalDateTime getStartDateTime() { return startDateTime; }

    /**
     * Sets the new start date and time for the appointment.
     *
     * @param startDateTime the new start date and time to set
     */
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }

    /**
     * Returns the new end date and time for the appointment.
     *
     * @return the new end date and time
     */
    public LocalDateTime getEndDateTime() { return endDateTime; }

    /**
     * Sets the new end date and time for the appointment.
     *
     * @param endDateTime the new end date and time to set
     */
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }
}
