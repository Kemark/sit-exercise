package de.sit.exercise.util.service;

import java.util.UUID;

import org.springframework.lang.NonNull;

import de.sit.exercise.util.dto.IDTO;
import de.sit.exercise.util.mapper.IMapper;
import de.sit.exercise.util.model.IModel;
import de.sit.exercise.util.repository.IRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * each feature service should implement this interface
 */
public interface IService<T1 extends IModel, T2 extends IDTO> {

    /**
     * Is used to map the dto to the model representation
     *
     * @return instance of the specific model mapper
     */
    IMapper<T1, T2> getMapper();

    IRepository<T1> getRepository();

    String getEntityName();

    /**
     * creates a new model
     *
     * @param dto dto representation of the model
     * @return created model
     */
    default T1 create(@NonNull T2 dto) {
        var model = getMapper().toModel(dto);
        return getRepository().save(model);
    }

    /**
     * updates a model with the given unique id and the dto
     *
     * @param id unique identifier of the existing model
     * @return updated model
     */
    default T1 update(@NonNull UUID id, @NonNull T2 dto) {
        var model = getRepository().getReferenceById(id, "Entity with id '%s' not found for '{}'", id,
                getEntityName());

        getMapper().updateModel(dto, model, id);
        return getRepository().save(model);
    }

    /**
     * deletes a model with the given unique id
     *
     * @param id unique identifier of the model
     */
    default void delete(@NonNull UUID id) {
        var model = getRepository().getReferenceById(id, "Entity with id '%s' not found for '{}'", id,
                getEntityName());

        getRepository().delete(model);
    }

    /**
     * checks if the model exists
     *
     * @param id unique identifier representing the model
     * @return true, if the model exists or false if not
     */
    default boolean exists(@NonNull UUID id) {
        return getRepository().existsById(id);
    }

    /**
     * retrieves a model by the unique id
     *
     * @param id unique identifier of the model
     * @return model or exception is thrown if not find by the id
     */
    default T1 findById(@NonNull UUID id) {
        return getRepository().getReferenceById(id, "Entity with id '%s' not found for '{}'", id,
                getEntityName());
    }

    /**
     * generic filter for filtering all natural id properties
     *
     * @param pageable     the Pageable
     * @param searchString The string to search for
     * @return The filtered entities
     */
    @NonNull
    default Page<T1> findFiltered(@NonNull Pageable pageable, @NonNull String searchString) {
        return getRepository().findFiltered(pageable, searchString.toLowerCase());
    }

}
