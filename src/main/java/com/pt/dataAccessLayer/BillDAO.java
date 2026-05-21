package com.pt.dataAccessLayer;

import com.pt.model.Bill;

/**
 * Data Access Object (DAO) class specifically designed for handling operations on the {@link Bill} entity.
 * <p>
 * This class extends the generic {@link AbstractDAO} to inherit fundamental retrieval and insertion capabilities.
 * In strict adherence to warehouse logging compliance and relational specifications, update and delete operations
 * are explicitly blocked to safeguard the immutability of recorded financial transactions within the persistent log.
 * </p>
 * @version 1.0
 */

public class BillDAO extends AbstractDAO<Bill> {

    /**
     * Overrides the default update behavioral contract to intentionally prevent data modification on log entries.
     * <p>
     * Financial invoices must remain completely immutable once generated. Calling this method will always fail.
     * </p>
     *
     * @param object the bill instance containing modified fields intended for synchronization (ignored)
     * @throws UnsupportedOperationException always thrown because database updates are forbidden on the Bill log table
     */
    @Override
    public void update(Bill object) {
        throw new UnsupportedOperationException("Modificarea facturilor nu este permisă în tabelul Log.");
    }

    /**
     * Overrides the default delete behavioral contract to intentionally prevent row removal from the log table.
     * <p>
     * Audit logs require total persistence of historical operations. Calling this method will always fail.
     * </p>
     *
     * @param id the unique primary identifier of the target bill record to be erased (ignored)
     * @throws UnsupportedOperationException always thrown because database deletions are forbidden on the Bill log table
     */
    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Ștergerea facturilor nu este permisă în tabelul Log.");
    }
}