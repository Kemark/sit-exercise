package de.sit.exercise.features.category;

import java.util.UUID;

import org.springframework.lang.NonNull;

import de.sit.exercise.util.service.IService;

/**
 * Categorie service interface
 *
 * The generic crud implemmentions are in the base interface (IService).
 */
public interface ICategoryService extends IService<Category, CategoryDto> {
    /**
     * Retrieves the amount of books, associated to the category.
     *
     * @param id uuid of the category
     * @return count of associated books
     */
    @NonNull
    Integer getBookCount(UUID id);
}
