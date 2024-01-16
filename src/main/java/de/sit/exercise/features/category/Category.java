package de.sit.exercise.features.category;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.NaturalId;

import de.sit.exercise.features.book.Book;
import de.sit.exercise.util.model.IModel;
import de.sit.exercise.util.model.UUIDListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Category of a book
 */
@Entity
@Getter
@Setter
@EntityListeners(UUIDListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Category implements IModel {

    @Id
    private UUID id;

    @NaturalId(mutable = true)
    @EqualsAndHashCode.Include
    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 512)
    private String description;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Book> books;
}
