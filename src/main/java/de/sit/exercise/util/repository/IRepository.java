package de.sit.exercise.util.repository;

import static java.lang.String.format;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

import de.sit.exercise.exception.EntityNotFoundException;

@NoRepositoryBean
public interface IRepository<T> extends JpaRepository<T, UUID> {

    /**
     * retrieves an entity by the unique id
     *
     * @param id           Uniqe id to search for
     * @param errorMessage Error message in case the entity can't be found
     * @return found entity or exception is thrown if the entity can't be found by
     *         the given id
     */
    @NonNull
    default T getReferenceById(@NonNull UUID id, String errorMessage, Object... args) {
        try {
            return this.getReferenceById(id);
        } catch (jakarta.persistence.EntityNotFoundException ex) {
            throw new EntityNotFoundException(format(errorMessage, args));
        }
    }

    /**
     * generic filter for filtering all natural id properties
     *
     * @param pageable     the Pageable
     * @param searchString The string to search for
     * @return The filtered entities
     */
    @NonNull
    Page<T> findFiltered(Pageable pageable, String searchString);
}
