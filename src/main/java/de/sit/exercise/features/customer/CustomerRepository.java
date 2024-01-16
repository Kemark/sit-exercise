package de.sit.exercise.features.customer;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import de.sit.exercise.util.repository.IRepository;

public interface CustomerRepository extends IRepository<Customer> {

    /**
     *
     */
    @Query("SELECT cu "
            + " FROM Customer cu "
            + " WHERE "
            + "    LOWER(cu.name) LIKE :searchString "
            + "    OR LOWER(cu.email) LIKE :searchString ")
    @Override
    @NonNull
    Page<Customer> findFiltered(Pageable pageable, String searchString);

    /**
     *
     */
    Optional<Customer> findByEmail(String email);
}
