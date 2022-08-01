package com.github.jaczerob.project1.models;

import java.io.Serializable;

/**
 * Represents the base object for all models
 * @author Jacob
 * @since 0.1
 * @version 0.12
 */
public abstract class ERSObject implements Serializable {
    private int id;

    /**
     * The constructor for {@link ERSObject} and its subclasses
     * @param id The ID of the model
     */
    protected ERSObject(int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }
}
