package de.sit.exercise.util.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * Each entity implements this interface. It provides a common id handling. And
 * it enables generic handling.
 */
public interface IModel extends Serializable {
    UUID getId();

    void setId(UUID id);
}
