package com.pt.validator;

import com.pt.model.Client;

/**
 * Validator implementation dedicated to checking data constraints for {@link Client} instances.
 */

public class ClientValidator implements Validator<Client> {

    /**
     * Validates that the client instance is not null, has a non-empty name, and possesses a valid structured email format.
     *
     * @param client the {@link Client} instance to check
     * @throws IllegalArgumentException if fields are empty or email formatting parameters are invalid
     */
    @Override
    public void validate(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null.");
        }
        if (client.getName() == null || client.getName().isBlank()) {
            throw new IllegalArgumentException("Client name cannot be empty.");
        }
        if (client.getEmail() == null || !client.getEmail().contains("@") || !client.getEmail().contains(".")) {
            throw new IllegalArgumentException("Invalid client email.");
        }
    }
}