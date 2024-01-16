package de.sit.exercise.features.customer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;

import de.sit.exercise.components.auth.JwtTokenProvider;
import de.sit.exercise.util.mapper.IMapper;
import lombok.Getter;

/*
 * Maps the Category entity to the Dto represenstion and vice versa
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = { JwtTokenProvider.class })
@Getter
public abstract class CustomerMapper implements IMapper<Customer, CustomerDto> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Mapping(target = "password", ignore = true)
    public abstract CustomerDto toDto(Customer model);

    @Override
    @Mapping(target = "password", expression = "java(getPasswordEncoder().encode(dto.getPassword()))")
    @NonNull
    public abstract Customer toModel(CustomerDto dto);
}
