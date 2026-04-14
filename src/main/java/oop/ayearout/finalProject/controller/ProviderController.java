package oop.ayearout.finalProject.controller;

import jakarta.validation.Valid;
import oop.ayearout.finalProject.model.Provider;
import oop.ayearout.finalProject.service.ProviderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for provider CRUD operations.
 *
 * <p>This class exposes HTTP endpoints for adding, retrieving, updating,
 * and deleting providers. All requests are routed through the ProviderService,
 * which enforces business rules and data integrity constraints.
 * Base path: /api/providers.
 *
 * @author Hailey Whitley
 * @version Final Project
 * @bugs None
 */
@RestController
@RequestMapping("/api/providers")
@CrossOrigin(origins = "*")
public class ProviderController {

    private final ProviderService providerService;

    /**
     * Constructs a ProviderController with the required service.
     * Spring injects the dependency automatically.
     *
     * @param providerService the service used to handle provider operations
     */
    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    /**
     * Adds a new provider to the system.
     * Mapped to POST /api/providers.
     *
     * @param provider the provider data from the request body
     * @return a ResponseEntity containing the saved provider and HTTP status 201
     */
    @PostMapping
    public ResponseEntity<Provider> addProvider(@Valid @RequestBody Provider provider) {
        return ResponseEntity.status(HttpStatus.CREATED).body(providerService.addProvider(provider));
    }

    /**
     * Returns all providers, or filters by specialty if a search term is provided.
     * Mapped to GET /api/providers.
     *
     * @param specialty an optional specialty string to filter results by, or null for all providers
     * @return a ResponseEntity containing a list of matching providers and HTTP status 200
     */
    @GetMapping
    public ResponseEntity<List<Provider>> getAllProviders(
            @RequestParam(required = false) String specialty) {
        if (specialty != null && !specialty.isBlank()) {
            return ResponseEntity.ok(providerService.searchBySpecialty(specialty));
        }
        return ResponseEntity.ok(providerService.getAllProviders());
    }

    /**
     * Returns the provider with the given ID.
     * Mapped to GET /api/providers/{id}.
     *
     * @param id the ID of the provider to retrieve
     * @return a ResponseEntity containing the provider and HTTP status 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<Provider> getProvider(@PathVariable Integer id) {
        return ResponseEntity.ok(providerService.getProvider(id));
    }

    /**
     * Updates the information of an existing provider.
     * Mapped to PUT /api/providers/{id}.
     *
     * @param id       the ID of the provider to update
     * @param provider the updated provider data from the request body
     * @return a ResponseEntity containing the updated provider and HTTP status 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<Provider> updateProvider(
            @PathVariable Integer id, @Valid @RequestBody Provider provider) {
        return ResponseEntity.ok(providerService.updateProvider(id, provider));
    }

    /**
     * Deletes the provider with the given ID, provided they have no appointments.
     * Mapped to DELETE /api/providers/{id}.
     *
     * @param id the ID of the provider to delete
     * @return a ResponseEntity with HTTP status 204 and no body on success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvider(@PathVariable Integer id) {
        providerService.deleteProvider(id);
        return ResponseEntity.noContent().build();
    }
}
