package com.pt.presentation;

import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;
import java.util.List;

/**
 * Presentation Layer utility class designed to dynamically map data object domains into Swing GUI table models.
 * <p>
 * It leverages advanced Java Reflection techniques to inspect properties from both standard entity classes
 * and modern Java Records, ensuring decoupled population and beautiful UI labels formatting.
 * </p>
 */

public class ReflectionTableModel {

    /**
     * Private constructor to enforce utility pattern constraints, preventing external instantiation of this model factory.
     */
    private ReflectionTableModel() {
    }

    /**
     * Inspects a generic dataset list and builds a matching {@link DefaultTableModel} container through introspection.
     * Dynamically branches execution to handle fields structure extraction or records component extraction.
     *
     * @param <T>     the generic object type within the targeting processing collection list
     * @param objects the underlying list source of data objects used to extract table headers and values
     * @return a completely configured {@link DefaultTableModel} matrix structure populated with data entries
     */
    public static <T> DefaultTableModel createTableModel(List<T> objects) {
        DefaultTableModel model = new DefaultTableModel();
        if (objects == null || objects.isEmpty()) {
            return model;
        }
        Class<?> objectClass = objects.getFirst().getClass();s
        if (objectClass.isRecord()) {
            java.util.Arrays.stream(objectClass.getRecordComponents())
                    .map(RecordComponent::getName)
                    .map(ReflectionTableModel::formatHeaderName)
                    .forEach(model::addColumn);
        } else {
            java.util.Arrays.stream(objectClass.getDeclaredFields())
                    .map(Field::getName)
                    .map(ReflectionTableModel::formatHeaderName)
                    .forEach(model::addColumn);
        }
        objects.stream().map(object -> {
            if (objectClass.isRecord()) {
                RecordComponent[] components = objectClass.getRecordComponents();
                return java.util.Arrays.stream(components)
                        .map(comp -> {
                            try {
                                return comp.getAccessor().invoke(object);
                            } catch (Exception e) {
                                return null;
                            }
                        })
                        .toArray();
            } else {
                Field[] fields = objectClass.getDeclaredFields();
                return java.util.Arrays.stream(fields)
                        .map(field -> {
                            field.setAccessible(true);
                            try {
                                return field.get(object);
                            } catch (IllegalAccessException e) {
                                return null;
                            }
                        })
                        .toArray();
            }
        }).forEach(model::addRow);
        return model;
    }

    /**
     * Parses a variable token key label structured under standard camelCase formatting rules into an elegant UI title.
     * For example, it translates the field token "totalPrice" into the graphical label display "Total Price".
     *
     * @param camelCaseName the raw property field identification name string
     * @return a beautifully formatted capital-case layout display name split by words
     */
    private static String formatHeaderName(String camelCaseName) {
        if (camelCaseName == null || camelCaseName.isBlank()) {
            return "";
        }
        String regexSplit = camelCaseName.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
        return regexSplit.substring(0, 1).toUpperCase() + regexSplit.substring(1);
    }
}