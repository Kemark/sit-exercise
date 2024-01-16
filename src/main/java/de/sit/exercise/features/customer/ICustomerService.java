package de.sit.exercise.features.customer;

import org.springframework.lang.NonNull;

import de.sit.exercise.util.service.IService;

/**
 *
 */
public interface ICustomerService extends IService<Customer, CustomerDto> {
    /**
     *
     * @param email
     * @return
     */
    Customer findByEmail(@NonNull String email);

    /**
     *
     * @return
     */
    boolean isAnyCustomerExist();
}
