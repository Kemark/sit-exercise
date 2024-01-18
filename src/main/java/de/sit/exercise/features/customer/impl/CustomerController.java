package de.sit.exercise.features.customer.impl;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.sit.exercise.features.customer.CustomerDto;
import de.sit.exercise.features.customer.CustomerMapper;
import de.sit.exercise.features.customer.ICustomerService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Crud Operations for managing customer. A Cusomer is need for
 * authentification.
 */
@RestController
@RequestMapping("api")
@Getter
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerMapper mapper;

    private final ICustomerService service;

    // @@@@  @@@@@@ @@@@@
    // @    @ @        @
    // @      @@@@@    @
    // @  @@@ @        @
    // @    @ @        @
    //  @@@@  @@@@@@   @

    @GetMapping(value = "/customer/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<CustomerDto> getById(final @NonNull @PathVariable UUID id) {
        var model = getService().findById(id);
        return ResponseEntity.ok(getMapper().toDto(model));
    }

    @GetMapping(value = "/customer", produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<Page<CustomerDto>> getFiltered(@NonNull final Pageable pageable,
            @NonNull @RequestParam(required = false) final String searchString) {
        var pagedModels = getService().findFiltered(pageable, searchString);
        return ResponseEntity.ok(getMapper().toPage(pagedModels, pageable));
    }

    // @@@@@   @@@@   @@@@  @@@@@
    // @    @ @    @ @        @
    // @    @ @    @  @@@@    @
    // @@@@@  @    @      @   @
    // @      @    @ @    @   @
    // @       @@@@   @@@@    @

    @PostMapping(value = "/customer", produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<CustomerDto> create(
            final @RequestBody @Validated @NonNull CustomerDto dto) {
        var model = getService().create(dto);
        return ResponseEntity.ok(getMapper().toDto(model));
    }

    // @@@@@  @    @ @@@@@
    // @    @ @    @   @
    // @    @ @    @   @
    // @@@@@  @    @   @
    // @      @    @   @
    // @       @@@@    @

    @PutMapping(value = "/customer/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<CustomerDto> update(
            @NonNull @PathVariable final UUID id,
            final @RequestBody @Validated @NonNull CustomerDto dto) {
        var model = getService().update(id, dto);
        return ResponseEntity.ok(getMapper().toDto(model));
    }

    // @@@@@  @@@@@@ @      @@@@@@ @@@@@ @@@@@@
    // @    @ @      @      @        @   @
    // @    @ @@@@@  @      @@@@@    @   @@@@@
    // @    @ @      @      @        @   @
    // @    @ @      @      @        @   @
    // @@@@@  @@@@@@ @@@@@@ @@@@@@   @   @@@@@@

    @DeleteMapping(value = "/customer/{id}")
    public final ResponseEntity<Void> delete(@NonNull @PathVariable final UUID id) {
        getService().delete(id);
        return ResponseEntity.noContent().build();
    }
}
