package de.sit.exercise.features.customer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import de.sit.exercise.util.mapper.IMapper;
import lombok.Getter;

/*
 * Maps the Category entity to the Dto represenstion and vice versa
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Getter
public abstract class CustomerMapper implements IMapper<Customer, CustomerDto> {

    @Override
    @Mapping(target = "password", ignore = true)
    public abstract CustomerDto toDto(Customer model);
}
