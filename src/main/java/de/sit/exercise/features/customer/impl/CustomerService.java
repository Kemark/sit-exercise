package de.sit.exercise.features.customer.impl;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import de.sit.exercise.exception.EntityNotFoundException;
import de.sit.exercise.features.customer.Customer;
import de.sit.exercise.features.customer.CustomerMapper;
import de.sit.exercise.features.customer.CustomerRepository;
import de.sit.exercise.features.customer.ICustomerService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *
 */
@Service
@Getter
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {
    private final CustomerMapper mapper;
    private final CustomerRepository repository;
    private String entityName = "Customer";

    /**
     *
     */
    @Override
    public Customer findByEmail(@NonNull String email) {
        return getRepository().findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("customer with email '%s' does not exists", email));
    }

    /**
     *
     */
    @Override
    public boolean isAnyCustomerExist() {
        return getRepository().count() > 0;
    }
}
