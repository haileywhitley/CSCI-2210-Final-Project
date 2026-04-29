package finalProject.controller;

import jakarta.validation.Valid;
import finalProject.AppointmentStatus;
import finalProject.dto.AppointmentRequest;
import finalProject.dto.RescheduleRequest;
import finalProject.model.Appointment;
import finalProject.service.AppointmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for appointment operations.
 *
 * <p>This class exposes HTTP endpoints for scheduling, retrieving, filtering,
 * rescheduling, and updating the status of appointments. All requests are
 * taken through the AppointmentService, which enforces all business rules
 * and data integrity constraints. Base path: /api/appointments.
 *
 * <p>Supported query filters via GET /api/appointments:
 * <ul>
 *   <li>patientId — all appointments for a specific patient</li>
 *   <li>providerId — all appointments for a specific provider</li>
 *   <li>status — all appointments with a given status</li>
 *   <li>start and end — all appointments within a date range</li>
 * </ul>
 *
 * @author Abbey Yearout
 * @version Final Project
 * @bugs None
 */
@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final AppointmentService appointmentService;

    /**
     * Constructs an AppointmentController with the required service.
     * Spring injects the dependency automatically.
     *
     * @param appointmentService the service used to handle appointment operations
     */
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Schedules a new appointment.
     * Mapped to POST /api/appointments.
     *
     * @param request the appointment details from the request body
     * @return a ResponseEntity containing the saved appointment and HTTP status 201
     */
    @PostMapping
    public ResponseEntity<Appointment> schedule(@Valid @RequestBody AppointmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(appointmentService.scheduleAppointment(request));
    }

    /**
     * Returns all appointments, or a filtered subset based on the provided
     * query parameters. Only one filter is applied per request in the order:
     * patientId, providerId, status, date range, or no filter.
     * Mapped to GET /api/appointments.
     *
     * @param patientId  an optional patient ID to filter by, or null for no filter
     * @param providerId an optional provider ID to filter by, or null for no filter
     * @param status     an optional appointment status to filter by, or null for no filter
     * @param start      the start of an optional date range filter, or null for no filter
     * @param end        the end of an optional date range filter, or null for no filter
     * @return a ResponseEntity containing the filtered list of appointments and HTTP status 200
     */
    @GetMapping
    public ResponseEntity<List<Appointment>> getAppointments(
            @RequestParam(required = false) Integer patientId,
            @RequestParam(required = false) Integer providerId,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        if (patientId  != null) return ResponseEntity.ok(appointmentService.getByPatient(patientId));
        if (providerId != null) return ResponseEntity.ok(appointmentService.getByProvider(providerId));
        if (status     != null) return ResponseEntity.ok(appointmentService.getByStatus(status));
        if (start != null && end != null)
            return ResponseEntity.ok(appointmentService.getByDateRange(start, end));
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    /**
     * Returns the appointment with the given ID.
     * Mapped to GET /api/appointments/{id}.
     *
     * @param id the ID of the appointment to retrieve
     * @return a ResponseEntity containing the appointment and HTTP status 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointment(@PathVariable Integer id) {
        return ResponseEntity.ok(appointmentService.getAppointment(id));
    }

    /**
     * Reschedules an existing appointment to new start and end times.
     * Mapped to PUT /api/appointments/{id}/reschedule.
     *
     * @param id      the ID of the appointment to reschedule
     * @param request the new start and end times from the request body
     * @return a ResponseEntity containing the rescheduled appointment and HTTP status 200
     */
    @PutMapping("/{id}/reschedule")
    public ResponseEntity<Appointment> reschedule(
            @PathVariable Integer id,
            @Valid @RequestBody RescheduleRequest request) {
        return ResponseEntity.ok(appointmentService.rescheduleAppointment(id, request));
    }

    /**
     * Updates the status of an existing appointment without changing its schedule.
     * Mapped to PATCH /api/appointments/{id}/status.
     *
     * @param id     the ID of the appointment to update
     * @param status the new status to apply
     * @return a ResponseEntity containing the updated appointment and HTTP status 200
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Appointment> updateStatus(
            @PathVariable Integer id,
            @RequestParam AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, status));
    }
}
