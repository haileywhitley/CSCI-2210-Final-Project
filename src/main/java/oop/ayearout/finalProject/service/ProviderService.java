package oop.ayearout.finalProject.service;

import oop.ayearout.finalProject.exception.ResourceNotFoundException;
import oop.ayearout.finalProject.model.Provider;
import oop.ayearout.finalProject.repository.AppointmentRepository;
import oop.ayearout.finalProject.repository.ProviderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Business logic layer for provider operations.
 *
 * <p>This class handles creating, retrieving, updating, and deleting
 * providers. It enforces the constraint that a provider with existing
 * appointments cannot be deleted, preserving referential integrity.
 *
 * @author Hailey Whitley
 * @version Final Project
 * @bugs None
 */
@Service
public class ProviderService {

    private final ProviderRepository providerRepository;
    private final AppointmentRepository appointmentRepository;

    /**
     * Constructs a ProviderService with the required repositories.
     * Spring injects these dependencies automatically.
     *
     * @param providerRepository    the repository used to access provider records
     * @param appointmentRepository the repository used to check for existing appointments
     */
    public ProviderService(ProviderRepository providerRepository,
                           AppointmentRepository appointmentRepository) {
        this.providerRepository = providerRepository;
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Saves a new provider to the database and returns the saved instance
     * with its generated ID.
     *
     * @param provider the provider to add
     * @return the saved provider with its assigned ID
     */
    public Provider addProvider(Provider provider) {
        return providerRepository.save(provider);
    }

    /**
     * Retrieves a single provider by their ID.
     *
     * @param id the ID of the provider to retrieve
     * @return the provider with the given ID
     * @throws ResourceNotFoundException if no provider with that ID exists
     */
    public Provider getProvider(Integer id) {
        return providerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Provider not found with ID: " + id));
    }

    /**
     * Returns a list of all providers in the database.
     *
     * @return a list of all providers, or an empty list if none exist
     */
    public List<Provider> getAllProviders() {
        return providerRepository.findAll();
    }

    /**
     * Returns all providers whose specialty exactly matches the given string,
     * ignoring case.
     *
     * @param specialty the specialty to filter by
     * @return a list of matching providers, or an empty list if none found
     */
    public List<Provider> searchBySpecialty(String specialty) {
        return providerRepository.findBySpecialtyIgnoreCase(specialty);
    }

    /**
     * Updates the name, specialty, and location of an existing provider.
     *
     * @param id      the ID of the provider to update
     * @param updated a Provider object containing the new field values
     * @return the updated provider as saved in the database
     * @throws ResourceNotFoundException if no provider with that ID exists
     */
    public Provider updateProvider(Integer id, Provider updated) {
        Provider existing = getProvider(id);
        existing.setName(updated.getName());
        existing.setSpecialty(updated.getSpecialty());
        existing.setLocation(updated.getLocation());
        return providerRepository.save(existing);
    }

    /**
     * Deletes a provider from the database, provided they have no existing appointments.
     *
     * <p>If the provider has one or more appointments on record, deletion is refused
     * to preserve referential integrity. All appointments for that provider must be
     * removed before the provider can be deleted.
     *
     * @param id the ID of the provider to delete
     * @throws ResourceNotFoundException if no provider with that ID exists
     * @throws IllegalStateException     if the provider has existing appointments
     */
    public void deleteProvider(Integer id) {
        Provider provider = getProvider(id);
        List<?> appts = appointmentRepository.findByProvider_ProviderId(id);
        if (!appts.isEmpty()) {
            throw new IllegalStateException(
                "Cannot delete provider " + provider.getName() +
                " — they have " + appts.size() + " appointment(s) on record."
            );
        }
        providerRepository.delete(provider);
    }
}
