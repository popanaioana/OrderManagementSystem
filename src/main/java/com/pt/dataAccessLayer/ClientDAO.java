package com.pt.dataAccessLayer;

import com.pt.model.Client;

/**
 * Data Access Object (DAO) class for managing persistent operations related to the {@link Client} entity.
 * <p>
 * This class inherits all fundamental CRUD operations (findAll, findById, insert, update, delete)
 * from the generic {@link AbstractDAO} using Java Reflection mechanisms, requiring no local method
 * implementations for standard behaviors.
 * </p>
 */

public class ClientDAO extends AbstractDAO<Client> {
    // Left intentionally blank because all CRUD logic is handled
    // dynamically by the generic superclass AbstractDAO via reflection.
}