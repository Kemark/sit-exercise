package de.sit.exercise.util.mapper;

import java.util.List;
import java.util.UUID;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import de.sit.exercise.util.model.IModel;

/**
 * Maps an Model to a DTO
 */
public interface IMapper<T1 extends IModel, T2> {

    /**
     * Maps a DTO to a Model
     *
     * @param model The Model/Entity
     * @return The corresponding DTO
     */
    T2 toDto(T1 model);

    /**
     * Maps a DTO to a Model
     *
     * @param dto Input DTO
     * @return Mapped Model
     */
    @NonNull
    T1 toModel(T2 dto);

    /**
     * Maps a list of Models to a list of DTOs
     *
     * @param models The list of Models/Entities
     * @return The List of corresponding DTOs
     */
    @NonNull
    List<T2> toList(List<T1> models);

    /**
     * Updates an existing model with the given dto
     *
     * @param dto   Dto with updateable informations
     * @param model The existing model
     * @param id    id of the model when the update is performed by a rest service
     *              and the id is defined by the rest parameter
     */
    T1 updateModel(T2 dto, @MappingTarget T1 model, @Context UUID id);

    /**
     * use the id from the path parameter. It's used for updates
     *
     * @param id id from path parameter
     */
    @AfterMapping
    default void useModelId(@MappingTarget final T1 model, final @Context UUID id) {
        model.setId(id);
    }

    /**
     * Maps a pageable model to a page of DTOs.
     *
     * @param modelPage page containing models
     * @param pageable  containing frame size and page
     *
     */
    default Page<T2> toPage(Page<T1> modelPage, @NonNull Pageable pageable) {
        return new PageImpl<>(this.toList(modelPage.getContent()), pageable, modelPage.getTotalElements());
    }
}
