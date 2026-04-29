package finalProject.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * JPA entity representing a clinic patient.
 *
 * <p>This class stores the patient ID, name, date of birth,
 * and contact information. It maps to the PATIENTS table in
 * the SQLite database. Each instance corresponds to one row.
 *
 * @author Abbey Yearout
 * @version Final Project
 * @bugs None
 */
@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer patientId;

    @NotBlank(message = "Patient name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Date of birth is required")
    @Column(nullable = false)
    private String dateOfBirth;

    @Column
    private String contactInfo;

    /**
     * Default constructor required by JPA.
     * Should not be called directly in application code.
     */
    public Patient() {}

    /**
     * Creates a new patient with the given personal information.
     * The patient ID is assigned automatically by the database.
     *
     * @param name        the full name of the patient
     * @param dateOfBirth the patient's date of birth as a string
     * @param contactInfo the patient's phone number or email address
     */
    public Patient(String name, String dateOfBirth, String contactInfo) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.contactInfo = contactInfo;
    }

    // Getters and setters

    /**
     * Returns the unique patient ID assigned by the database.
     *
     * @return the patient ID
     */
    public Integer getPatientId() { return patientId; }

    /**
     * Sets the patient ID.
     *
     * @param patientId the patient ID to set
     */
    public void setPatientId(Integer patientId) { this.patientId = patientId; }

    /**
     * Returns the patient's full name.
     *
     * @return the patient's name
     */
    public String getName() { return name; }

    /**
     * Sets the patient's full name.
     *
     * @param name the name to set
     */
    public void setName(String name) { this.name = name; }

    /**
     * Returns the patient's date of birth.
     *
     * @return the date of birth as a string
     */
    public String getDateOfBirth() { return dateOfBirth; }

    /**
     * Sets the patient's date of birth.
     *
     * @param dateOfBirth the date of birth to set
     */
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    /**
     * Returns the patient's contact information.
     *
     * @return the contact info string
     */
    public String getContactInfo() { return contactInfo; }

    /**
     * Sets the patient's contact information.
     *
     * @param contactInfo the contact info to set
     */
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    /**
     * Returns a string representation of the patient.
     *
     * @return a formatted string with the patient ID and name
     */
    @Override
    public String toString() {
        return "Patient ID: " + patientId + " | Name: " + name;
    }
}
