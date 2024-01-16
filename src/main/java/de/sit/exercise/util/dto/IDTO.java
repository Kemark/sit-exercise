package de.sit.exercise.util.dto;

import java.io.Serializable;
import java.util.UUID;

import jakarta.annotation.Nullable;

/**
 * Comman interface for all dto's to ensure that getID() is always implemented
 */
public interface IDTO extends Serializable {
    @Nullable
    UUID getId();
}
