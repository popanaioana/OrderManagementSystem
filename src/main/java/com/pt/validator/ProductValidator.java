package com.pt.validator;

import com.pt.model.Product;

/**
 * Validator implementation dedicated to checking data constraints for {@link Product} instances.
 */

public class ProductValidator implements Validator<Product> {

    /**
     * Validates that the product instance is not null, has a non-empty name, and contains strictly valid positive prices and non-negative stock volumes.
     *
     * @param product the {@link Product} instance to check
     * @throws IllegalArgumentException if the price is negative/zero or if stock metrics drop below zero persistent baselines
     */
    @Override
    public void validate(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty.");
        }
        if (product.getPrice() <= 0) {
            throw new IllegalArgumentException("Product price must be positive.");
        }
        if (product.getStock() < 0) {
            throw new IllegalArgumentException("Product stock cannot be negative.");
        }
    }
}