package com.pt.dataAccessLayer;

import com.pt.connection.ConnectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.RecordComponent;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic Data Access Object class that implements the common CRUD operations for model classes.
 *
 * <p>
 * This class uses Java Reflection to dynamically inspect the fields of a model class and to build
 * SQL statements for insert, update, delete and select operations. It is meant to be extended by
 * concrete DAO classes, such as ClientDAO, ProductDAO, OrderDAO or BillDAO.
 * </p>
 *
 * <p>
 * The class also supports Java records, which is useful for the immutable Bill model required by
 * the assignment. For normal classes, objects are created using the no-argument constructor. For
 * records, objects are created using the canonical record constructor.
 * </p>
 *
 * @param <T> the model class type managed by this DAO
 */
public class AbstractDAO<T> {
    private final Class<T> type;

    /**
     * Constructs a new AbstractDAO and determines the actual generic type used by the child DAO.
     *
     * <p>
     * For example, when ClientDAO extends AbstractDAO&lt;Client&gt;, this constructor extracts
     * the Client class at runtime.
     * </p>
     */
    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Returns the database table name corresponding to the current model class.
     *
     * <p>
     * The Order class is mapped to the Orders table because ORDER is a reserved SQL keyword.
     * The Bill record is mapped to the Log table because the assignment requires bills to be
     * stored in a log table.
     * </p>
     *
     * @return the database table name
     */
    private String getTableName() {
        if ("Order".equals(type.getSimpleName())) {
            return "Orders";
        }
        if ("Bill".equals(type.getSimpleName())) {
            return "Log";
        }
        return type.getSimpleName();
    }

    /**
     * Creates a SELECT query that retrieves all rows from the mapped table.
     *
     * @return the generated SELECT query
     */
    private String createSelectAllQuery() {
        return "SELECT * FROM " + getTableName();
    }

    /**
     * Creates a SELECT query that retrieves one row by its primary key.
     *
     * <p>
     * This implementation assumes that all tables use a primary key column named id.
     * </p>
     *
     * @return the generated SELECT query
     */
    private String createSelectByIdQuery() {
        return "SELECT * FROM " + getTableName() + " WHERE id = ?";
    }

    /**
     * Creates a DELETE query that removes one row by its primary key.
     *
     * <p>
     * This implementation assumes that all tables use a primary key column named id.
     * </p>
     *
     * @return the generated DELETE query
     */
    private String createDeleteQuery() {
        return "DELETE FROM " + getTableName() + " WHERE id = ?";
    }

    /**
     * Creates an INSERT query dynamically based on the non-null fields of the given object.
     *
     * <p>
     * The id field is skipped because it is assumed to be generated automatically by the database.
     * Null fields are also skipped so that database default values can still be applied.
     * </p>
     *
     * @param object the object that will be inserted
     * @return the generated INSERT query
     * @throws IllegalAccessException if a field value cannot be accessed through reflection
     */
    private String createInsertQuery(T object) throws IllegalAccessException {
        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();
        for (Field field : type.getDeclaredFields()) {
            field.setAccessible(true);
            if ("id".equals(field.getName())) {
                continue;
            }
            Object value = field.get(object);
            if (value == null) {
                continue;
            }
            columns.append(field.getName()).append(", ");
            placeholders.append("?, ");
        }
        if (columns.length() == 0) {
            throw new IllegalArgumentException("Cannot insert object with no non-null fields.");
        }
        columns.setLength(columns.length() - 2);
        placeholders.setLength(placeholders.length() - 2);
        return "INSERT INTO " + getTableName() + " (" + columns + ") VALUES (" + placeholders + ")";
    }

    /**
     * Creates an UPDATE query dynamically based on the fields of the model class.
     *
     * <p>
     * The id field is not updated. It is only used in the WHERE clause.
     * </p>
     *
     * @return the generated UPDATE query
     */
    private String createUpdateQuery() {
        StringBuilder query = new StringBuilder();
        query.append("UPDATE ").append(getTableName()).append(" SET ");
        for (Field field : type.getDeclaredFields()) {
            if (!"id".equals(field.getName())) {
                query.append(field.getName()).append(" = ?, ");
            }
        }
        query.setLength(query.length() - 2);
        query.append(" WHERE id = ?");
        return query.toString();
    }

    /**
     * Retrieves all rows from the mapped table and converts them into model objects.
     *
     * @return a list containing all objects from the mapped table
     */
    public List<T> findAll() {
        List<T> result = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(createSelectAllQuery());
             ResultSet resultSet = statement.executeQuery()) {
            result = createObjects(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Retrieves one object by its primary key.
     *
     * @param id the primary key value
     * @return the found object, or null if no object exists with the given id
     */
    public T findById(int id) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(createSelectByIdQuery())) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<T> objects = createObjects(resultSet);
                return objects.isEmpty() ? null : objects.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Inserts a new object into the mapped table.
     *
     * <p>
     * The method returns the generated database id. If the insert fails, it returns -1.
     * </p>
     *
     * @param object the object to be inserted
     * @return the generated id, or -1 if the insert operation fails
     */
    public int insert(T object) {
        try {
            String query = createInsertQuery(object);
            try (Connection connection = ConnectionFactory.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         query,
                         Statement.RETURN_GENERATED_KEYS
                 )) {
                setInsertParameters(statement, object);
                statement.executeUpdate();
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Updates an existing object in the mapped table.
     *
     * <p>
     * The object must contain a valid id value because the id is used in the WHERE clause.
     * </p>
     *
     * @param object the object containing the updated values
     */
    public void update(T object) {
        String query = createUpdateQuery();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            setUpdateParameters(statement, object);
            statement.executeUpdate();
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes one row from the mapped table by id.
     *
     * @param id the primary key value of the row to delete
     */
    public void delete(int id) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(createDeleteQuery())) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts a JDBC ResultSet into a list of model objects.
     *
     * <p>
     * If the managed type is a Java record, the method creates objects using the canonical
     * record constructor. Otherwise, it creates objects using the no-argument constructor and
     * sets fields directly through reflection.
     * </p>
     *
     * @param resultSet the ResultSet returned by a SELECT query
     * @return a list of mapped model objects
     */
    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                T object;
                if (type.isRecord()) {
                    object = createRecordObject(resultSet);
                } else {
                    object = createNormalObject(resultSet);
                }
                list.add(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Creates an object of a normal Java class from the current ResultSet row.
     *
     * <p>
     * The class must have a no-argument constructor. Field names must match the database
     * column names.
     * </p>
     *
     * @param resultSet the ResultSet positioned on the current row
     * @return the created object
     * @throws Exception if reflection or ResultSet access fails
     */
    private T createNormalObject(ResultSet resultSet) throws Exception {
        T instance = type.getDeclaredConstructor().newInstance();
        for (Field field : type.getDeclaredFields()) {
            field.setAccessible(true);
            Object value = getColumnValue(resultSet, field.getName());
            value = convertValue(value, field.getType());
            if (value != null || !field.getType().isPrimitive()) {
                field.set(instance, value);
            }
        }
        return instance;
    }

    /**
     * Creates an object of a Java record from the current ResultSet row.
     *
     * <p>
     * The values are passed to the canonical record constructor in the order of the record
     * components. Record component names must match the database column names.
     * </p>
     *
     * @param resultSet the ResultSet positioned on the current row
     * @return the created record object
     * @throws Exception if reflection or ResultSet access fails
     */
    private T createRecordObject(ResultSet resultSet) throws Exception {
        RecordComponent[] components = type.getRecordComponents();
        Class<?>[] constructorParameterTypes = new Class<?>[components.length];
        Object[] constructorArguments = new Object[components.length];
        for (int i = 0; i < components.length; i++) {
            String columnName = components[i].getName();
            Class<?> componentType = components[i].getType();
            Object value = getColumnValue(resultSet, columnName);
            value = convertValue(value, componentType);
            constructorParameterTypes[i] = componentType;
            constructorArguments[i] = value;
        }
        Constructor<T> constructor = type.getDeclaredConstructor(constructorParameterTypes);
        constructor.setAccessible(true);
        return constructor.newInstance(constructorArguments);
    }

    /**
     * Safely retrieves a column value from the current ResultSet row.
     *
     * <p>
     * This method exists to keep the reflection mapping code cleaner.
     * </p>
     *
     * @param resultSet the ResultSet positioned on the current row
     * @param columnName the database column name
     * @return the value stored in the given column
     * @throws SQLException if the column cannot be read
     */
    private Object getColumnValue(ResultSet resultSet, String columnName) throws SQLException {
        return resultSet.getObject(columnName);
    }

    /**
     * Converts database values to the Java field types used in the model classes.
     *
     * <p>
     * SQL Server may return numeric values as Integer, Long, BigDecimal or other Number
     * implementations. This method converts them to the expected Java type.
     * </p>
     *
     * @param value the value read from the database
     * @param targetType the Java type expected by the model field
     * @return the converted value
     */
    private Object convertValue(Object value, Class<?> targetType) {
        if (value == null) {
            return null;
        }
        if ((targetType == int.class || targetType == Integer.class) && value instanceof Number number) {
            return number.intValue();
        }
        if ((targetType == double.class || targetType == Double.class) && value instanceof Number number) {
            return number.doubleValue();
        }
        if ((targetType == long.class || targetType == Long.class) && value instanceof Number number) {
            return number.longValue();
        }
        if ((targetType == float.class || targetType == Float.class) && value instanceof Number number) {
            return number.floatValue();
        }
        if (targetType == LocalDateTime.class && value instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        }
        if (targetType == String.class) {
            return value.toString();
        }
        return value;
    }

    /**
     * Binds the non-null object fields to the INSERT query parameters.
     *
     * <p>
     * The id field is skipped because it is generated by the database. Null values are skipped
     * because the INSERT query also skips the corresponding columns.
     * </p>
     *
     * @param statement the PreparedStatement used for insert
     * @param object the object being inserted
     * @throws SQLException if parameter binding fails
     * @throws IllegalAccessException if a field value cannot be accessed
     */
    private void setInsertParameters(PreparedStatement statement, T object) throws SQLException, IllegalAccessException {
        int parameterIndex = 1;
        for (Field field : type.getDeclaredFields()) {
            field.setAccessible(true);
            if ("id".equals(field.getName())) {
                continue;
            }
            Object value = field.get(object);
            if (value == null) {
                continue;
            }
            statement.setObject(parameterIndex, convertValueForDatabase(value));
            parameterIndex++;
        }
    }

    /**
     * Binds object fields to the UPDATE query parameters.
     *
     * <p>
     * All fields except id are used in the SET part of the query. The id field is bound last
     * because it is used in the WHERE clause.
     * </p>
     *
     * @param statement the PreparedStatement used for update
     * @param object the object being updated
     * @throws SQLException if parameter binding fails
     * @throws IllegalAccessException if a field value cannot be accessed
     */
    private void setUpdateParameters(PreparedStatement statement, T object) throws SQLException, IllegalAccessException {
        int parameterIndex = 1;
        Object idValue = null;
        for (Field field : type.getDeclaredFields()) {
            field.setAccessible(true);
            if ("id".equals(field.getName())) {
                idValue = field.get(object);
                continue;
            }
            Object value = field.get(object);
            statement.setObject(parameterIndex, convertValueForDatabase(value));
            parameterIndex++;
        }
        statement.setObject(parameterIndex, idValue);
    }

    /**
     * Converts Java values to database-friendly values before binding them to a PreparedStatement.
     *
     * <p>
     * LocalDateTime values are converted to Timestamp values for better compatibility with SQL Server.
     * Other values are returned unchanged.
     * </p>
     *
     * @param value the Java value
     * @return the value prepared for database insertion or update
     */
    private Object convertValueForDatabase(Object value) {
        if (value instanceof LocalDateTime localDateTime) {
            return Timestamp.valueOf(localDateTime);
        }
        return value;
    }
}