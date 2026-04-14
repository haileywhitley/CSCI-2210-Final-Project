package oop.ayearout.finalProject.repository;

import oop.ayearout.finalProject.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Data access interface for Provider entities.
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
public interface ProviderRepository extends JpaRepository<Provider, Integer> {

    /**
     * Returns all providers whose specialty exactly matches the given
     * string, ignoring case.
     *
     * @param specialty the specialty to filter by
     * @return a list of matching providers, or an empty list if none found
     */
    List<Provider> findBySpecialtyIgnoreCase(String specialty);

    /**
     * Returns all providers whose names contain the given string,
     * ignoring case.
     *
     * @param name the substring to search for within provider names
     * @return a list of matching providers, or an empty list if none found
     */
    List<Provider> findByNameContainingIgnoreCase(String name);
}
