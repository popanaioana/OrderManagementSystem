package com.pt.businessLayer;

import com.pt.dataAccessLayer.ClientDAO;
import com.pt.model.Client;
import com.pt.validator.ClientValidator;

import java.util.List;

/**
 * Business Logic Layer class for handling operations related to Clients.
 * Manages data validation and database communication via the Client data access layer.
 */

public class ClientBLL {
    private final ClientDAO clientDAO = new ClientDAO();
    private final ClientValidator clientValidator = new ClientValidator();

    /**
     * Retrieves all clients from the database.
     *
     * @return a List of all registered Client objects
     */
    public List<Client> findAllClients() {
        return clientDAO.findAll();
    }

    /**
     * Retrieves all clients sorted alphabetically by their name using Java Streams.
     *
     * @return a List of sorted Client objects
     */
    public List<Client> findClientsSortedByName() {
        return clientDAO.findAll()
                .stream()
                .sorted((client1, client2) -> client1.getName().compareToIgnoreCase(client2.getName()))
                .toList();
    }

    /**
     * Finds a specific client by their unique identifier.
     *
     * @param id the unique identifier of the client
     * @return the Client object if found, or null otherwise
     */
    public Client findClientById(int id) {
        return clientDAO.findById(id);
    }

    /**
     * Validates and inserts a new client into the database.
     *
     * @param client the Client object to be added
     * @return the generated database identifier of the new client, or -1 if the insertion fails
     * @throws IllegalArgumentException if the client properties fail validation rules
     */
    public int addClient(Client client) {
        clientValidator.validate(client);
        return clientDAO.insert(client);
    }

    /**
     * Validates and updates the details of an existing client.
     *
     * @param client the Client object with updated details (must include a valid ID)
     * @throws IllegalArgumentException if the updated client properties fail validation rules
     */
    public void updateClient(Client client) {
        clientValidator.validate(client);
        clientDAO.update(client);
    }

    /**
     * Deletes a client record from the database based on its unique identifier.
     *
     * @param id the unique identifier of the client to be removed
     */
    public void deleteClient(int id) {
        clientDAO.delete(id);
    }
}