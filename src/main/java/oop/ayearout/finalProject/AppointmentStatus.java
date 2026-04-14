package oop.ayearout.finalProject;

/**
 * Represents the lifecycle states of an appointment.
 *
 * <p>An appointment begins as SCHEDULED, and may be
 * updated to COMPLETED or CANCELLED as the visit concludes
 * or is cancelled by the patient or provider.
 *
 * @author Abbey Yearout
 * @version Final Project
 * @bugs None
 */
public enum AppointmentStatus {
    SCHEDULED,
    COMPLETED,
    CANCELLED
}
