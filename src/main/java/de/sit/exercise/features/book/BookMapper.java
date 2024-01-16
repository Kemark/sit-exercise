package de.sit.exercise.features.book;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import de.sit.exercise.features.category.impl.CategoryService;
import de.sit.exercise.util.mapper.IMapper;

/*
 * Maps the Book entity to the Dto represenstion and vice versa
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class BookMapper implements IMapper<Book, BookDto> {

    @Autowired
    private CategoryService categoryService;

    @Override
    @Mapping(source = "category.id", target = "categoryId")
    public abstract BookDto toDto(Book model);

    /**
     * Maps a category UUID to a Category object.
     *
     * 1. for a new book
     *
     * 2. when the category has changd
     */
    @AfterMapping
    protected void unlinkCategory(BookDto dto, @MappingTarget Book model) {
        var category = model.getCategory();

        if (category != null) {
            category.getBooks().remove(model);

        }
        var newCategory = categoryService.findById(dto.getCategoryId());
        model.setCategory(newCategory);
    }
}
