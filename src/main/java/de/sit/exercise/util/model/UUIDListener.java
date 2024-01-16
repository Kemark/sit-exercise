package de.sit.exercise.util.model;

import java.util.UUID;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

/**
 * Entity Listener for setting the id, if the id is not defined by the payload
 */
public class UUIDListener {
    @PrePersist
    @PreUpdate
    void setIdIfMissing(IModel model) {
        if (model.getId() == null) {
            model.setId(UUID.randomUUID());
        }
    }
}
