package com.pt.validator;

/**
 * A generic Functional Interface designed to validate application data models before persistent operations.
 * <p>
 * Being a {@link FunctionalInterface}, it exposes a single abstract method, allowing validation strategies
 * to be implemented cleanly via concrete validator classes or directly inline using Java Lambda expressions.
 * </p>
 *
 * @param <T> the type of the model object instance to be validated
 */

@FunctionalInterface
public interface Validator<T> {

    /**
     * Inspects the attributes of the provided object instance against predefined business integrity rules.
     *
     * @param object the entity object instance targeted for data constraint verification
     * @throws IllegalArgumentException if the inspected object attributes violate any application validation constraints
     */
    void validate(T object);
}