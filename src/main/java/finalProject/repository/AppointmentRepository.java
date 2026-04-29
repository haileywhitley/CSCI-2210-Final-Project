package finalProject.repository;

import finalProject.AppointmentStatus;
import finalProject.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data access interface for Appointment entities.
 *
 * <p>Spring Data JPA automatically generates implementations for derived
 * query methods at runtime. Custom JPQL queries are provided for date-range
 * filtering and overlap detection. All SQL is hidden behind this interface.
 *
 * @author Abbey Yearout
 * @version Final Project
 * @bugs None
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    /**
     * Returns all appointments associated with a given patient.
     *
     * @param patientId the ID of the patient to filter by
     * @return a list of appointments for that patient, or an empty list if none found
     */
    List<Appointment> findByPatient_PatientId(Integer patientId);

    /**
     * Returns all appointments associated with a given provider.
     *
     * @param providerId the ID of the provider to filter by
     * @return a list of appointments for that provider, or an empty list if none found
     */
    List<Appointment> findByProvider_ProviderId(Integer providerId);

    /**
     * Returns all appointments with a given status.
     *
     * @param status the appointment status to filter by
     * @return a list of appointments matching that status, or an empty list if none found
     */
    List<Appointment> findByStatus(AppointmentStatus status);

    /**
     * Returns all appointments whose start time falls within a given date range.
     *
     * @param start the beginning of the date range, inclusive
     * @param end   the end of the date range, inclusive
     * @return a list of appointments starting between start and end, or an empty list if none found
     */
    @Query("SELECT a FROM Appointment a WHERE a.startDateTime >= :start AND a.startDateTime <= :end")
    List<Appointment> findByDateRange(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    /**
     * Returns all appointments for a given provider that overlap with a
     * specified time window, optionally excluding one appointment by ID.
     *
     * <p>Used by the service layer to detect scheduling conflicts before
     * creating or rescheduling an appointment.
     *
     * @param providerId the ID of the provider to check
     * @param start      the start of the proposed time window
     * @param end        the end of the proposed time window
     * @param excludeId  the appointment ID to exclude from the check,
     *                   or null to include all appointments
     * @return a list of conflicting appointments, or an empty list if no conflicts exist
     */
    @Query("SELECT a FROM Appointment a WHERE a.provider.providerId = :providerId " +
           "AND a.startDateTime < :end AND a.endDateTime > :start " +
           "AND (:excludeId IS NULL OR a.appointmentId != :excludeId)")
    List<Appointment> findOverlapping(
        @Param("providerId") Integer providerId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end,
        @Param("excludeId") Integer excludeId
    );
}
