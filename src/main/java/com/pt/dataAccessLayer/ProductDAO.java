package com.pt.dataAccessLayer;

import com.pt.model.Product;

/**
 * Data Access Object (DAO) class for managing persistent operations related to the {@link Product} entity.
 * <p>
 * This class inherits all fundamental CRUD operations (findAll, findById, insert, update, delete)
 * from the generic {@link AbstractDAO} superclass. It uses Java Reflection techniques to inspect the model and map fields
 * directly to the database columns of the Product table.
 * </p>
 */

public class ProductDAO extends AbstractDAO<Product> {
    // Left intentionally blank because all CRUD logic is handled
    // dynamically by the generic superclass AbstractDAO via reflection.
}