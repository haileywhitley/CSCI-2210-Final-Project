package finalProject;

import finalProject.exception.ResourceNotFoundException;
import finalProject.model.Appointment;
import finalProject.model.Patient;
import finalProject.repository.AppointmentRepository;
import finalProject.repository.PatientRepository;
import finalProject.service.PatientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for PatientService business logic.
 *
 * <p>This class uses Mockito to isolate the service layer from the database,
 * verifying that patient retrieval and deletion rules are enforced correctly
 * without requiring a live database connection.
 *
 * @author Abbey Yearout
 * @version Final Project
 * @bugs None
 */
@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private PatientService patientService;

    /**
     * Verifies that requesting a patient with a nonexistent ID
     * throws ResourceNotFoundException.
     */
    @Test
    void getPatient_notFound_throwsException() {
        when(patientRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> patientService.getPatient(99));
    }

    /**
     * Verifies that attempting to delete a patient who has existing appointments
     * throws IllegalStateException to preserve referential integrity.
     */
    @Test
    void deletePatient_withAppointments_throwsIllegalState() {
        Patient p = new Patient("Test", "01-01-2000", "555-0000");
        p.setPatientId(1);
        when(patientRepository.findById(1)).thenReturn(Optional.of(p));
        when(appointmentRepository.findByPatient_PatientId(1))
            .thenReturn(List.of(mock(Appointment.class)));
        assertThrows(IllegalStateException.class, () -> patientService.deletePatient(1));
    }

    /**
     * Verifies that a patient with no existing appointments can be deleted
     * successfully and that the repository delete method is called.
     */
    @Test
    void deletePatient_noAppointments_deletesSuccessfully() {
        Patient p = new Patient("Test", "01-01-2000", "555-0000");
        p.setPatientId(1);
        when(patientRepository.findById(1)).thenReturn(Optional.of(p));
        when(appointmentRepository.findByPatient_PatientId(1))
            .thenReturn(Collections.emptyList());
        assertDoesNotThrow(() -> patientService.deletePatient(1));
        verify(patientRepository).delete(p);
    }
}
