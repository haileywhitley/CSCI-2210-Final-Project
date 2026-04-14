package oop.ayearout.finalProject.service;

import oop.ayearout.finalProject.AppointmentStatus;
import oop.ayearout.finalProject.dto.AppointmentRequest;
import oop.ayearout.finalProject.dto.RescheduleRequest;
import oop.ayearout.finalProject.exception.InvalidAppointmentException;
import oop.ayearout.finalProject.exception.ResourceNotFoundException;
import oop.ayearout.finalProject.exception.SchedulingConflictException;
import oop.ayearout.finalProject.model.Appointment;
import oop.ayearout.finalProject.model.Patient;
import oop.ayearout.finalProject.model.Provider;
import oop.ayearout.finalProject.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Business logic layer for appointment operations.
 *
 * <p>This class handles scheduling, retrieving, filtering, rescheduling,
 * and updating the status of appointments. It enforces all data integrity
 * constraints defined by the system:
 * <ul>
 *   <li>Start time must be before end time</li>
 *   <li>Patient and provider must exist before an appointment is created</li>
 *   <li>No overlapping appointments for the same provider</li>
 *   <li>All constraints are re-validated when an appointment is rescheduled</li>
 * </ul>
 *
 * @author Abbey Yearout
 * @version Final Project
 * @bugs None
 */
@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientService patientService;
    private final ProviderService providerService;

    /**
     * Constructs an AppointmentService with the required repositories and services.
     * Spring injects these dependencies automatically.
     *
     * @param appointmentRepository the repository used to access appointment records
     * @param patientService        the service used to validate and retrieve patients
     * @param providerService       the service used to validate and retrieve providers
     */
    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientService patientService,
                              ProviderService providerService) {
        this.appointmentRepository = appointmentRepository;
        this.patientService = patientService;
        this.providerService = providerService;
    }

    /**
     * Validates and saves a new appointment to the database.
     *
     * <p>Confirms that the patient and provider exist, that the start time
     * is before the end time, and that no overlapping appointment exists
     * for the same provider before persisting.
     *
     * @param request the DTO containing the appointment details to schedule
     * @return the newly created appointment as saved in the database
     * @throws ResourceNotFoundException   if the patient or provider does not exist
     * @throws InvalidAppointmentException if the start time is not before the end time
     * @throws SchedulingConflictException if the provider has an overlapping appointment
     */
    public Appointment scheduleAppointment(AppointmentRequest request) {
        Patient patient = patientService.getPatient(request.getPatientId());
        Provider provider = providerService.getProvider(request.getProviderId());

        validateTimes(request.getStartDateTime(), request.getEndDateTime());
        checkForConflicts(provider.getProviderId(), request.getStartDateTime(),
                          request.getEndDateTime(), null);

        Appointment appt = new Appointment(
            patient, provider,
            request.getStartDateTime(), request.getEndDateTime(),
            AppointmentStatus.SCHEDULED,
            request.getReason()
        );
        return appointmentRepository.save(appt);
    }

    /**
     * Retrieves a single appointment by its ID.
     *
     * @param id the ID of the appointment to retrieve
     * @return the appointment with the given ID
     * @throws ResourceNotFoundException if no appointment with that ID exists
     */
    public Appointment getAppointment(Integer id) {
        return appointmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));
    }

    /**
     * Returns a list of all appointments in the database.
     *
     * @return a list of all appointments, or an empty list if none exist
     */
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    /**
     * Returns all appointments associated with a given patient.
     *
     * @param patientId the ID of the patient to filter by
     * @return a list of appointments for that patient, or an empty list if none found
     * @throws ResourceNotFoundException if no patient with that ID exists
     */
    public List<Appointment> getByPatient(Integer patientId) {
        patientService.getPatient(patientId);
        return appointmentRepository.findByPatient_PatientId(patientId);
    }

    /**
     * Returns all appointments associated with a given provider.
     *
     * @param providerId the ID of the provider to filter by
     * @return a list of appointments for that provider, or an empty list if none found
     * @throws ResourceNotFoundException if no provider with that ID exists
     */
    public List<Appointment> getByProvider(Integer providerId) {
        providerService.getProvider(providerId);
        return appointmentRepository.findByProvider_ProviderId(providerId);
    }

    /**
     * Returns all appointments with a given status.
     *
     * @param status the appointment status to filter by
     * @return a list of appointments matching that status, or an empty list if none found
     */
    public List<Appointment> getByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatus(status);
    }

    /**
     * Returns all appointments whose start time falls within a given date range.
     *
     * @param start the beginning of the date range, inclusive
     * @param end   the end of the date range, inclusive
     * @return a list of appointments starting between start and end, or an empty list if none found
     * @throws InvalidAppointmentException if start is after end
     */
    public List<Appointment> getByDateRange(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new InvalidAppointmentException("Range start must be before range end.");
        }
        return appointmentRepository.findByDateRange(start, end);
    }

    /**
     * Reschedules an existing appointment to new start and end times.
     *
     * <p>Re-validates all business rules with the new times, excluding
     * the appointment being updated from the provider conflict check so
     * that it does not conflict with itself.
     *
     * @param id      the ID of the appointment to reschedule
     * @param request the DTO containing the new start and end times
     * @return the rescheduled appointment as saved in the database
     * @throws ResourceNotFoundException   if no appointment with that ID exists
     * @throws InvalidAppointmentException if the new start time is not before the new end time
     * @throws SchedulingConflictException if the provider has an overlapping appointment
     */
    public Appointment rescheduleAppointment(Integer id, RescheduleRequest request) {
        Appointment existing = getAppointment(id);

        validateTimes(request.getStartDateTime(), request.getEndDateTime());
        checkForConflicts(existing.getProvider().getProviderId(),
                          request.getStartDateTime(), request.getEndDateTime(), id);

        existing.setStartDateTime(request.getStartDateTime());
        existing.setEndDateTime(request.getEndDateTime());
        return appointmentRepository.save(existing);
    }

    /**
     * Updates the status of an existing appointment without changing its schedule.
     *
     * @param id        the ID of the appointment to update
     * @param newStatus the new status to apply
     * @return the updated appointment as saved in the database
     * @throws ResourceNotFoundException if no appointment with that ID exists
     */
    public Appointment updateStatus(Integer id, AppointmentStatus newStatus) {
        Appointment existing = getAppointment(id);
        existing.setStatus(newStatus);
        return appointmentRepository.save(existing);
    }

    /**
     * Validates that the given start time is strictly before the given end time.
     *
     * @param start the proposed start date and time
     * @param end   the proposed end date and time
     * @throws InvalidAppointmentException if start is not before end
     */
    private void validateTimes(LocalDateTime start, LocalDateTime end) {
        if (!start.isBefore(end)) {
            throw new InvalidAppointmentException(
                "Appointment start time must be before end time."
            );
        }
    }

    /**
     * Checks whether a proposed time window conflicts with any existing appointments
     * for a given provider, optionally excluding one appointment by ID.
     *
     * @param providerId the ID of the provider to check
     * @param start      the start of the proposed time window
     * @param end        the end of the proposed time window
     * @param excludeId  the appointment ID to exclude from the check,
     *                   or null to include all appointments
     * @throws SchedulingConflictException if any overlapping appointment is found
     */
    private void checkForConflicts(Integer providerId, LocalDateTime start,
                                   LocalDateTime end, Integer excludeId) {
        List<Appointment> conflicts = appointmentRepository.findOverlapping(
            providerId, start, end, excludeId
        );
        if (!conflicts.isEmpty()) {
            throw new SchedulingConflictException(
                "Provider already has an appointment from " +
                conflicts.get(0).getStartDateTime() + " to " +
                conflicts.get(0).getEndDateTime()
            );
        }
    }
}
