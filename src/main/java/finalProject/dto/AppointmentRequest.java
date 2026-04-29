package finalProject.dto;

import finalProject.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Data transfer object for creating or updating an appointment via the REST API.
 *
 * <p>This class separates the API contract from the internal Appointment entity,
 * accepting patient and provider IDs as integers rather than full object references.
 * The service layer resolves those IDs to validated objects before persisting.
 *
 * @author Abbey Yearout
 * @version Final Project
 * @bugs None
 */
public class AppointmentRequest {

    @NotNull(message = "patientId is required")
    private Integer patientId;

    @NotNull(message = "providerId is required")
    private Integer providerId;

    @NotNull(message = "startDateTime is required")
    private LocalDateTime startDateTime;

    @NotNull(message = "endDateTime is required")
    private LocalDateTime endDateTime;

    private AppointmentStatus status;
    private String reason;

    // Getters and setters

    /**
     * Returns the ID of the patient for this appointment.
     *
     * @return the patient ID
     */
    public Integer getPatientId() { return patientId; }

    /**
     * Sets the ID of the patient for this appointment.
     *
     * @param patientId the patient ID to set
     */
    public void setPatientId(Integer patientId) { this.patientId = patientId; }

    /**
     * Returns the ID of the provider for this appointment.
     *
     * @return the provider ID
     */
    public Integer getProviderId() { return providerId; }

    /**
     * Sets the ID of the provider for this appointment.
     *
     * @param providerId the provider ID to set
     */
    public void setProviderId(Integer providerId) { this.providerId = providerId; }

    /**
     * Returns the requested start date and time.
     *
     * @return the start date and time
     */
    public LocalDateTime getStartDateTime() { return startDateTime; }

    /**
     * Sets the requested start date and time.
     *
     * @param startDateTime the start date and time to set
     */
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }

    /**
     * Returns the requested end date and time.
     *
     * @return the end date and time
     */
    public LocalDateTime getEndDateTime() { return endDateTime; }

    /**
     * Sets the requested end date and time.
     *
     * @param endDateTime the end date and time to set
     */
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }

    /**
     * Returns the requested appointment status.
     *
     * @return the status, or null if not specified
     */
    public AppointmentStatus getStatus() { return status; }

    /**
     * Sets the requested appointment status.
     *
     * @param status the status to set
     */
    public void setStatus(AppointmentStatus status) { this.status = status; }

    /**
     * Returns the reason for the visit.
     *
     * @return the reason string, or null if not provided
     */
    public String getReason() { return reason; }

    /**
     * Sets the reason for the visit.
     *
     * @param reason the reason to set
     */
    public void setReason(String reason) { this.reason = reason; }
}
