package oop.ayearout.finalProject;

import oop.ayearout.finalProject.dto.AppointmentRequest;
import oop.ayearout.finalProject.dto.RescheduleRequest;
import oop.ayearout.finalProject.exception.InvalidAppointmentException;
import oop.ayearout.finalProject.exception.ResourceNotFoundException;
import oop.ayearout.finalProject.exception.SchedulingConflictException;
import oop.ayearout.finalProject.model.Appointment;
import oop.ayearout.finalProject.model.Patient;
import oop.ayearout.finalProject.model.Provider;
import oop.ayearout.finalProject.repository.AppointmentRepository;
import oop.ayearout.finalProject.service.AppointmentService;
import oop.ayearout.finalProject.service.PatientService;
import oop.ayearout.finalProject.service.ProviderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AppointmentService business logic.
 *
 * <p>This class uses Mockito to isolate the service layer from the database,
 * verifying that each business rule is enforced correctly without requiring
 * a live database connection.
 *
 * @author Abbey Yearout
 * @version Final Project
 * @bugs None
 */
@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private PatientService patientService;
    @Mock
    private ProviderService providerService;

    @InjectMocks
    private AppointmentService appointmentService;

    private Patient patient;
    private Provider provider;
    private LocalDateTime start;
    private LocalDateTime end;

    /**
     * Initializes shared test fixtures before each test method runs.
     */
    @BeforeEach
    void setUp() {
        patient = new Patient("Abbey Y", "01-01-2006", "555-1234");
        patient.setPatientId(1);

        provider = new Provider("Dr. Smith", "Cardiology", "Clinic A");
        provider.setProviderId(1);

        start = LocalDateTime.of(2026, 5, 1, 10, 0);
        end   = LocalDateTime.of(2026, 5, 1, 11, 0);
    }

    /**
     * Verifies that a valid appointment request is saved and returned with
     * SCHEDULED status.
     */
    @Test
    void scheduleAppointment_validRequest_savesAndReturns() {
        when(patientService.getPatient(1)).thenReturn(patient);
        when(providerService.getProvider(1)).thenReturn(provider);
        when(appointmentRepository.findOverlapping(any(), any(), any(), any()))
            .thenReturn(Collections.emptyList());

        Appointment saved = new Appointment(patient, provider, start, end,
                                            AppointmentStatus.SCHEDULED, "Checkup");
        when(appointmentRepository.save(any())).thenReturn(saved);

        Appointment result = appointmentService.scheduleAppointment(buildRequest(1, 1, start, end));
        assertNotNull(result);
        assertEquals(AppointmentStatus.SCHEDULED, result.getStatus());
        verify(appointmentRepository).save(any());
    }

    /**
     * Verifies that scheduling an appointment where start is after end
     * throws InvalidAppointmentException.
     */
    @Test
    void scheduleAppointment_startAfterEnd_throwsInvalidAppointmentException() {
        when(patientService.getPatient(1)).thenReturn(patient);
        when(providerService.getProvider(1)).thenReturn(provider);

        assertThrows(InvalidAppointmentException.class, () ->
            appointmentService.scheduleAppointment(buildRequest(1, 1, end, start))
        );
    }

    /**
     * Verifies that scheduling an appointment that overlaps an existing one
     * throws SchedulingConflictException.
     */
    @Test
    void scheduleAppointment_conflict_throwsSchedulingConflictException() {
        when(patientService.getPatient(1)).thenReturn(patient);
        when(providerService.getProvider(1)).thenReturn(provider);

        Appointment conflicting = new Appointment(patient, provider, start, end,
                                                  AppointmentStatus.SCHEDULED, "Other");
        when(appointmentRepository.findOverlapping(any(), any(), any(), any()))
            .thenReturn(Collections.singletonList(conflicting));

        assertThrows(SchedulingConflictException.class, () ->
            appointmentService.scheduleAppointment(buildRequest(1, 1, start, end))
        );
    }

    /**
     * Verifies that requesting an appointment with a nonexistent ID
     * throws ResourceNotFoundException.
     */
    @Test
    void getAppointment_notFound_throwsResourceNotFoundException() {
        when(appointmentRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () ->
            appointmentService.getAppointment(99)
        );
    }

    /**
     * Verifies that rescheduling with valid new times updates the start
     * and end date/time and saves the appointment.
     */
    @Test
    void rescheduleAppointment_validTimes_updatesAndSaves() {
        Appointment existing = new Appointment(patient, provider, start, end,
                                               AppointmentStatus.SCHEDULED, "Routine");
        existing.setAppointmentId(1);
        when(appointmentRepository.findById(1)).thenReturn(Optional.of(existing));
        when(appointmentRepository.findOverlapping(any(), any(), any(), any()))
            .thenReturn(Collections.emptyList());

        LocalDateTime newStart = start.plusDays(1);
        LocalDateTime newEnd   = end.plusDays(1);
        RescheduleRequest req = new RescheduleRequest();
        req.setStartDateTime(newStart);
        req.setEndDateTime(newEnd);

        when(appointmentRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        Appointment result = appointmentService.rescheduleAppointment(1, req);
        assertEquals(newStart, result.getStartDateTime());
        assertEquals(newEnd,   result.getEndDateTime());
    }

    /**
     * Verifies that updating the status of an existing appointment
     * changes its status and saves the result.
     */
    @Test
    void updateStatus_existingAppt_changesStatus() {
        Appointment existing = new Appointment(patient, provider, start, end,
                                               AppointmentStatus.SCHEDULED, "Routine");
        existing.setAppointmentId(1);
        when(appointmentRepository.findById(1)).thenReturn(Optional.of(existing));
        when(appointmentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Appointment result = appointmentService.updateStatus(1, AppointmentStatus.COMPLETED);
        assertEquals(AppointmentStatus.COMPLETED, result.getStatus());
    }

    /**
     * Builds an AppointmentRequest DTO from the given parameters for use in tests.
     *
     * @param pid   the patient ID
     * @param vid   the provider ID
     * @param s     the start date and time
     * @param e     the end date and time
     * @return a populated AppointmentRequest
     */
    private AppointmentRequest buildRequest(int pid, int vid,
                                            LocalDateTime s, LocalDateTime e) {
        AppointmentRequest r = new AppointmentRequest();
        r.setPatientId(pid);
        r.setProviderId(vid);
        r.setStartDateTime(s);
        r.setEndDateTime(e);
        r.setReason("Test");
        return r;
    }
}
