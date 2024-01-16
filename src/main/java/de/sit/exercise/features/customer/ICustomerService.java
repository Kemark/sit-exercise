package de.sit.exercise.features.customer;

import org.springframework.lang.NonNull;

import de.sit.exercise.util.service.IService;

/**
 *
 */
public interface ICustomerService extends IService<Customer, CustomerDto> {
    /**
     *
     */
    Customer findByEmail(@NonNull String email);

    /**
     *
     */
    boolean isAnyCustomerExist();
}
