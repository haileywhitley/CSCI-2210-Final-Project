package finalProject.controller;

import jakarta.validation.Valid;
import finalProject.model.Patient;
import finalProject.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for patient CRUD operations.
 *
 * <p>This class exposes HTTP endpoints for adding, retrieving, updating,
 * and deleting patients. All requests are routed through the PatientService,
 * which enforces business rules and data integrity constraints.
 * Base path: /api/patients.
 *
 * @author Abbey Yearout
 * @version Final Project
 * @bugs None
 */
@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {

    private final PatientService patientService;

    /**
     * Constructs a PatientController with the required service.
     * Spring injects the dependency automatically.
     *
     * @param patientService the service used to handle patient operations
     */
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Adds a new patient to the system.
     * Mapped to POST /api/patients.
     *
     * @param patient the patient data from the request body
     * @return a ResponseEntity containing the saved patient and HTTP status 201
     */
    @PostMapping
    public ResponseEntity<Patient> addPatient(@Valid @RequestBody Patient patient) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.addPatient(patient));
    }

    /**
     * Returns all patients, or filters by name if a search term is provided.
     * Mapped to GET /api/patients.
     *
     * @param name an optional name substring to filter results by, or null for all patients
     * @return a ResponseEntity containing a list of matching patients and HTTP status 200
     */
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients(
            @RequestParam(required = false) String name) {
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(patientService.searchByName(name));
        }
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    /**
     * Returns the patient with the given ID.
     * Mapped to GET /api/patients/{id}.
     *
     * @param id the ID of the patient to retrieve
     * @return a ResponseEntity containing the patient and HTTP status 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable Integer id) {
        return ResponseEntity.ok(patientService.getPatient(id));
    }

    /**
     * Updates the information of an existing patient.
     * Mapped to PUT /api/patients/{id}.
     *
     * @param id      the ID of the patient to update
     * @param patient the updated patient data from the request body
     * @return a ResponseEntity containing the updated patient and HTTP status 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable Integer id, @Valid @RequestBody Patient patient) {
        return ResponseEntity.ok(patientService.updatePatient(id, patient));
    }

    /**
     * Deletes the patient with the given ID, provided they have no appointments.
     * Mapped to DELETE /api/patients/{id}.
     *
     * @param id the ID of the patient to delete
     * @return a ResponseEntity with HTTP status 204 and no body on success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Integer id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
