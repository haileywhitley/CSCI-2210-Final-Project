package finalProject.service;

import finalProject.exception.ResourceNotFoundException;
import finalProject.model.Patient;
import finalProject.repository.AppointmentRepository;
import finalProject.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Business logic layer for patient operations.
 *
 * <p>This class handles creating, retrieving, updating, and deleting
 * patients. It enforces the constraint that a patient with existing
 * appointments cannot be deleted, preserving referential integrity.
 *
 * @author Abbey Yearout
 * @version Final Project
 * @bugs None
 */
@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    /**
     * Constructs a PatientService with the required repositories.
     * Spring injects these dependencies automatically.
     *
     * @param patientRepository     the repository used to access patient records
     * @param appointmentRepository the repository used to check for existing appointments
     */
    public PatientService(PatientRepository patientRepository,
                          AppointmentRepository appointmentRepository) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Saves a new patient to the database and returns the saved instance
     * with its generated ID.
     *
     * @param patient the patient to add
     * @return the saved patient with its assigned ID
     */
    public Patient addPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    /**
     * Retrieves a single patient by their ID.
     *
     * @param id the ID of the patient to retrieve
     * @return the patient with the given ID
     * @throws ResourceNotFoundException if no patient with that ID exists
     */
    public Patient getPatient(Integer id) {
        return patientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + id));
    }

    /**
     * Returns a list of all patients in the database.
     *
     * @return a list of all patients, or an empty list if none exist
     */
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    /**
     * Returns all patients whose names contain the given string,
     * ignoring case.
     *
     * @param name the substring to search for within patient names
     * @return a list of matching patients, or an empty list if none found
     */
    public List<Patient> searchByName(String name) {
        return patientRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Updates the name, date of birth, and contact info of an existing patient.
     *
     * @param id      the ID of the patient to update
     * @param updated a Patient object containing the new field values
     * @return the updated patient as saved in the database
     * @throws ResourceNotFoundException if no patient with that ID exists
     */
    public Patient updatePatient(Integer id, Patient updated) {
        Patient existing = getPatient(id);
        existing.setName(updated.getName());
        existing.setDateOfBirth(updated.getDateOfBirth());
        existing.setContactInfo(updated.getContactInfo());
        return patientRepository.save(existing);
    }

    /**
     * Deletes a patient from the database, provided they have no existing appointments.
     *
     * <p>If the patient has one or more appointments on record, deletion is refused
     * to preserve referential integrity. The patient must first have all their
     * appointments removed before they can be deleted.
     *
     * @param id the ID of the patient to delete
     * @throws ResourceNotFoundException if no patient with that ID exists
     * @throws IllegalStateException     if the patient has existing appointments
     */
    public void deletePatient(Integer id) {
        Patient patient = getPatient(id);
        List<?> appts = appointmentRepository.findByPatient_PatientId(id);
        if (!appts.isEmpty()) {
            throw new IllegalStateException(
                "Cannot delete patient " + patient.getName() +
                " — they have " + appts.size() + " appointment(s) on record."
            );
        }
        patientRepository.delete(patient);
    }
}
