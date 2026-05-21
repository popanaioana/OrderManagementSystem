package com.pt.validator;

import com.pt.model.Order;

/**
 * Validator implementation dedicated to checking structural constraints for commercial {@link Order} entries.
 */

public class OrderValidator implements Validator<Order> {

    /**
     * Validates that the order context is initialized, references valid data identifiers, and requests a strictly positive piece quantity.
     *
     * @param order the {@link Order} instance to check
     * @throws IllegalArgumentException if identifiers match zero-bounds or if requested purchase quantity quantities are negative
     */
    @Override
    public void validate(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }
        if (order.getClientId() <= 0) {
            throw new IllegalArgumentException("A valid client must be selected.");
        }
        if (order.getProductId() <= 0) {
            throw new IllegalArgumentException("A valid product must be selected.");
        }
        if (order.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
    }
}