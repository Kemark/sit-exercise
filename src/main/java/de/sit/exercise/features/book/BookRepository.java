package de.sit.exercise.features.book;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import de.sit.exercise.util.repository.IRepository;

public interface BookRepository extends IRepository<Book> {

    /**
     *
     */
    @Query("SELECT DISTINCT b "
            + " FROM Book b "
            + " LEFT JOIN b.category cat "
            + " WHERE "
            + "    LOWER(b.title) LIKE :searchString "
            + "    OR LOWER(b.author) LIKE :searchString "
            + "    OR LOWER(b.publisher) LIKE :searchString "
            + "    OR LOWER(cat.name) LIKE :searchString ")
    @Override
    @NonNull
    Page<Book> findFiltered(Pageable pageable, String searchString);

    /**
     * Retrieves the amount of books, associated to a specific category.
     *
     * @param id uuid of the category
     * @return count of associated books
     */
    @NonNull
    Integer countByCategoryId(UUID id);

}
