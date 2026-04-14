package oop.ayearout.finalProject.model;

import oop.ayearout.finalProject.AppointmentStatus;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * represents a scheduled meeting between a patient and a provider.
 *
 * <p>This class stores the appointment ID, patient, provider, start and end
 * date/time, status, and reason for the visit. It also allows updating the
 * appointment status and checking for scheduling conflicts while ensuring
 * that the start time occurs before the end time.
 *
 * <p>Maps to the appointment table in the SQL database.
 * Each instance corresponds to one row in the table
 *
 * @author Abbey Yearout
 * @version Final Project
 * @bugs None
 */
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer appointmentId;

    @NotNull(message = "Patient is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotNull(message = "Provider is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    @NotNull(message = "Start date/time is required")
    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @NotNull(message = "End date/time is required")
    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    @Column
    private String reason;

    /**
     * Default constructor required by JPA.
     * Should not be called directly in application code.
     */
    public Appointment() {}

    /**
     * Creates a new appointment with the given scheduling information.
     * The appointment ID is assigned automatically by the database.
     *
     * @param patient     patient attending the appointment
     * @param provider     provider conducting the appointment
     * @param startDateTime date and time the appointment begins
     * @param endDateTime  date and time the appointment ends
     * @param status        initial status of the appointment
     * @param reason        reason for the visit
     */
    public Appointment(Patient patient, Provider provider,
                       LocalDateTime startDateTime, LocalDateTime endDateTime,
                       AppointmentStatus status, String reason) {
        this.patient = patient;
        this.provider = provider;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.status = status;
        this.reason = reason;
    }

    /**
     * Returns true if this appointment overlaps with another appointment
     * for the same provider.
     *
     * <p>Two appointments overlap when one starts before the other ends
     * and ends after the other starts. Appointments for different providers
     * never conflict and always return false.
     *
     * @param other the other appointment to check against
     * @return true if both appointments share the same provider and their
     *         time windows overlap, false otherwise
     */
    public boolean overlapsWith(Appointment other) {
        if (!this.provider.getProviderId().equals(other.getProvider().getProviderId())) {
            return false;
        }
        return this.startDateTime.isBefore(other.getEndDateTime()) &&
               this.endDateTime.isAfter(other.getStartDateTime());
    }


    /**
     * Returns the unique appointment ID assigned by the database.
     *
     * @return the appointment ID
     */
    public Integer getAppointmentId() { return appointmentId; }

    /**
     * Sets the appointment ID.
     *
     * @param appointmentId the appointment ID to set
     */
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }

    /**
     * Returns the patient associated with this appointment.
     *
     * @return the patient
     */
    public Patient getPatient() { return patient; }

    /**
     * Sets the patient for this appointment.
     *
     * @param patient the patient to set
     */
    public void setPatient(Patient patient) { this.patient = patient; }

    /**
     * Returns the provider associated with this appointment.
     *
     * @return the provider
     */
    public Provider getProvider() { return provider; }

    /**
     * Sets the provider for this appointment.
     *
     * @param provider the provider to set
     */
    public void setProvider(Provider provider) { this.provider = provider; }

    /**
     * Returns the date and time the appointment begins.
     *
     * @return the start date and time
     */
    public LocalDateTime getStartDateTime() { return startDateTime; }

    /**
     * Sets the date and time the appointment begins.
     *
     * @param startDateTime the start date and time to set
     */
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }

    /**
     * Returns the date and time the appointment ends.
     *
     * @return the end date and time
     */
    public LocalDateTime getEndDateTime() { return endDateTime; }

    /**
     * Sets the date and time the appointment ends.
     *
     * @param endDateTime the end date and time to set
     */
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }

    /**
     * Returns the current status of the appointment.
     *
     * @return the appointment status
     */
    public AppointmentStatus getStatus() { return status; }

    /**
     * Sets the status of the appointment.
     *
     * @param status the status to set
     */
    public void setStatus(AppointmentStatus status) { this.status = status; }

    /**
     * Returns the reason for the visit.
     *
     * @return the reason string
     */
    public String getReason() { return reason; }

    /**
     * Sets the reason for the visit.
     *
     * @param reason the reason to set
     */
    public void setReason(String reason) { this.reason = reason; }

    /**
     * Returns a string representation of the appointment.
     *
     * @return a formatted string with the appointment ID, status, patient name,
     *         and provider name
     */
    @Override
    public String toString() {
        return "Appt #" + appointmentId + " [" + status + "] Patient: " +
               patient.getName() + " w/ " + provider.getName();
    }
}
