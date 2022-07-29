package com.github.jaczerob.project1.models;

/**
 * Represents the base object for all models
 * @author Jacob
 * @version 0.1
 * @since 0.1
 */
public abstract class ERSObject {
    private int id;

    /**
     * The constructor for {@link ERSObject} and its subclasses
     * @param id The ID of the model
     */
    public ERSObject(int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }
}
