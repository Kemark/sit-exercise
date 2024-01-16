package de.sit.exercise.features.category;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import de.sit.exercise.util.mapper.IMapper;

/*
 * Maps the Category entity to the Dto represenstion and vice versa
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper extends IMapper<Category, CategoryDto> {
}
