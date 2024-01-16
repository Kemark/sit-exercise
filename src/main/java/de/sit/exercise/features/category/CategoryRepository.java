package de.sit.exercise.features.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import de.sit.exercise.util.repository.IRepository;

public interface CategoryRepository extends IRepository<Category> {

    /**
     * generic filter for filtering all natural id properties
     */
    @Override
    @NonNull
    @Query("SELECT cat "
            + " FROM Category cat "
            + " WHERE "
            + "    LOWER(cat.name) LIKE :searchString ")
    Page<Category> findFiltered(Pageable pageable, String searchString);
}
