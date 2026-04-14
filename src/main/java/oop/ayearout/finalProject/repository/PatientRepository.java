package oop.ayearout.finalProject.repository;

import oop.ayearout.finalProject.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Data access interface for Patient entities.
 *
 * <p>Spring Data JPA automatically generates the implementation for
 * all methods at runtime. Direct SQL is hidden behind this interface,
 * satisfying the object-oriented data access requirement.
 *
 * @author Abbey Yearout
 * @version Final Project
 * @bugs None
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    /**
     * Returns all patients whose names contain the given string,
     * ignoring case.
     *
     * @param name the substring to search for within patient names
     * @return a list of matching patients, or an empty list if none found
     */
    List<Patient> findByNameContainingIgnoreCase(String name);
}
