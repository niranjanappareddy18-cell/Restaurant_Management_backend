package com.restaurant;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/restaurants")
@CrossOrigin(origins = "http://localhost:5173")
public class RestaurantController {

    private final RestaurantRepository repository;
    private final AdminTokenService tokenService;

    public RestaurantController(
            RestaurantRepository repository,
            AdminTokenService tokenService) {
        this.repository = repository;
        this.tokenService = tokenService;
    }

    private void checkAdmin(String authorization) {
        if (authorization == null ||
                !authorization.startsWith("Bearer ") ||
                !tokenService.isValid(authorization.substring(7))) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Admin login required");
        }
    }

    // READ: Customers can view menu items
    @GetMapping
    public List<Restaurant> getAll() {
        return repository.findAll();
    }

    // CREATE: Admin only
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Restaurant create(
            @RequestHeader(value = "Authorization", required = false)
            String authorization,
            @Valid @RequestBody Restaurant restaurant) {

        checkAdmin(authorization);
        return repository.save(restaurant);
    }

    // UPDATE: Admin only
    @PutMapping("/{id}")
    public Restaurant update(
            @RequestHeader(value = "Authorization", required = false)
            String authorization,
            @PathVariable Long id,
            @Valid @RequestBody Restaurant restaurant) {

        checkAdmin(authorization);

        Restaurant existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Menu item not found"));

        existing.setName(restaurant.getName());
        existing.setCategory(restaurant.getCategory());
        existing.setPrice(restaurant.getPrice());

        return repository.save(existing);
    }

    // DELETE: Admin only
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @RequestHeader(value = "Authorization", required = false)
            String authorization,
            @PathVariable Long id) {

        checkAdmin(authorization);

        if (!repository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Menu item not found");
        }

        repository.deleteById(id);
    }
}