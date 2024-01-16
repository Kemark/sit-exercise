package de.sit.exercise.features.category.impl;

import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import de.sit.exercise.features.book.BookRepository;
import de.sit.exercise.features.category.CategoryMapper;
import de.sit.exercise.features.category.CategoryRepository;
import de.sit.exercise.features.category.ICategoryService;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *
 */
@Service
@Getter
@RequiredArgsConstructor
@Transactional
public class CategoryService implements ICategoryService {
    private final CategoryMapper mapper;
    private final CategoryRepository repository;
    private final BookRepository bookRepository;
    private String entityName = "Category";

    /**
     * Retrieves the amount of books, associated to the category.
     */
    @Override
    @NonNull
    public Integer getBookCount(UUID id) {
        return getBookRepository().countByCategoryId(id);
    }
}
