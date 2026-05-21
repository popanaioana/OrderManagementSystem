package com.pt.dataAccessLayer;

import com.pt.model.Order;

/**
 * Data Access Object (DAO) class for managing persistent operations related to the {@link Order} entity.
 * <p>
 * This class inherits all generic boilerplate CRUD behaviors (findAll, findById, insert, update, delete)
 * from the generic {@link AbstractDAO} superclass. It leverages Java Reflection to execute dynamic mapping
 * on the corresponding database Orders table, eliminating the need for local database access methods.
 * </p>
 */

public class OrderDAO extends AbstractDAO<Order> {
    // Left intentionally blank because all CRUD logic is handled
    // dynamically by the generic superclass AbstractDAO via reflection.
}