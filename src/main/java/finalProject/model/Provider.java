package finalProject.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * JPA entity representing a clinic provider.
 *
 * <p>This class allows the user to create a provider for a patient
 * given a provider ID, name, specialty, and location. The provider
 * can be associated with multiple appointments, but only one provider
 * may be assigned to any single appointment, and appointments for
 * the same provider cannot overlap in time.
 *
 * <p>Maps to the PROVIDERS table in the SQLite database.
 * Each instance corresponds to one row.
 *
 * @author Hailey Whitley
 * @version Final Project
 * @bugs None
 */
@Entity
@Table(name = "providers")
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer providerId;

    @NotBlank(message = "Provider name is required")
    @Column(nullable = false)
    private String name;

    @Column
    private String specialty;

    @Column
    private String location;

    /**
     * Default constructor required by JPA.
     * Should not be called directly in application code.
     */
    public Provider() {}

    /**
     * Creates a new provider with the given professional information.
     * The provider ID is assigned automatically by the database.
     *
     * @param name      the full name of the provider
     * @param specialty the provider's medical specialty
     * @param location  the clinic or office location of the provider
     */
    public Provider(String name, String specialty, String location) {
        this.name = name;
        this.specialty = specialty;
        this.location = location;
    }

    // Getters and setters

    /**
     * Returns the unique provider ID assigned by the database.
     *
     * @return the provider ID
     */
    public Integer getProviderId() { return providerId; }

    /**
     * Sets the provider ID.
     *
     * @param providerId the provider ID to set
     */
    public void setProviderId(Integer providerId) { this.providerId = providerId; }

    /**
     * Returns the provider's full name.
     *
     * @return the provider's name
     */
    public String getName() { return name; }

    /**
     * Sets the provider's full name.
     *
     * @param name the name to set
     */
    public void setName(String name) { this.name = name; }

    /**
     * Returns the provider's medical specialty.
     *
     * @return the specialty string
     */
    public String getSpecialty() { return specialty; }

    /**
     * Sets the provider's medical specialty.
     *
     * @param specialty the specialty to set
     */
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    /**
     * Returns the provider's clinic or office location.
     *
     * @return the location string
     */
    public String getLocation() { return location; }

    /**
     * Sets the provider's clinic or office location.
     *
     * @param location the location to set
     */
    public void setLocation(String location) { this.location = location; }

    /**
     * Returns a string representation of the provider.
     *
     * @return a formatted string with the provider ID, name, specialty, and location
     */
    @Override
    public String toString() {
        return "Provider ID: " + providerId + ", Name: " + name +
               ", Specialty: " + specialty + ", Location: " + location;
    }
}
