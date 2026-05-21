package com.pt.model;

import java.sql.Timestamp;

/**
 * An immutable data record representing a finalized order financial invoice (Bill).
 * Implemented as a Java record to satisfy structural immutability specifications.
 *
 * @param id          the unique internal log index identifier
 * @param orderId     the unique associated order context primary key
 * @param clientName  the name of the customer at invoice closure
 * @param productName the name of the acquired item product
 * @param quantity    total volume count of items sold
 * @param totalPrice  the calculated gross monetary cost value
 * @param orderDate   the specific processing date timestamp log
 */

public record Bill(
        int id,
        int orderId,
        String clientName,
        String productName,
        int quantity,
        double totalPrice,
        Timestamp orderDate
) {
}