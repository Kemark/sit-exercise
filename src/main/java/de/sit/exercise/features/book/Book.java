package de.sit.exercise.features.book;

import java.util.UUID;

import org.hibernate.annotations.NaturalId;

import de.sit.exercise.features.category.Category;
import de.sit.exercise.util.model.IModel;
import de.sit.exercise.util.model.UUIDListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 *
 */
@Entity
@Getter
@Setter
@EntityListeners(UUIDListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Book implements IModel {
    @Id
    private UUID id;

    @NaturalId(mutable = true)
    @EqualsAndHashCode.Include
    @Column(nullable = false)
    private String title;

    @NaturalId(mutable = true)
    @EqualsAndHashCode.Include
    @Column(nullable = false, length = 100)
    private String author;

    @Column(nullable = false, length = 100)
    private String publisher;

    @Column(nullable = false)
    private Integer publishingYear;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id")
    private Category category;
}
