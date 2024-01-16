package de.sit.exercise.features.customer;

import java.util.UUID;

import org.hibernate.annotations.NaturalId;

import de.sit.exercise.util.model.IModel;
import de.sit.exercise.util.model.UUIDListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Customer Entity
 */
@Entity
@Getter
@Setter
@EntityListeners(UUIDListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Customer implements IModel {
    @Id
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String password;

    @NaturalId(mutable = true)
    @EqualsAndHashCode.Include
    @Column(nullable = false)
    private String email;
}
