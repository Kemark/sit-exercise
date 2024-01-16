package de.sit.exercise.features.category.impl;

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

import de.sit.exercise.features.category.CategoryDto;
import de.sit.exercise.features.category.CategoryMapper;
import de.sit.exercise.features.category.ICategoryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
@Getter
public class CategoryController {

    private final CategoryMapper mapper;

    private final ICategoryService service;

    // @@@@  @@@@@@ @@@@@
    // @    @ @        @
    // @      @@@@@    @
    // @  @@@ @        @
    // @    @ @        @
    //  @@@@  @@@@@@   @

    @GetMapping(value = "/category/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<CategoryDto> getById(final @NonNull @PathVariable UUID id) {
        var model = getService().findById(id);
        return ResponseEntity.ok(getMapper().toDto(model));
    }

    @GetMapping(value = "/category", produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<Page<CategoryDto>> getFiltered(@NonNull final Pageable pageable,
            @NonNull @RequestParam(required = false) final String searchString) {
        var pagedModels = getService().findFiltered(pageable, searchString);
        return ResponseEntity.ok(getMapper().toPage(pagedModels, pageable));
    }

    @GetMapping(value = "/category/{id}/bookCount")
    public final ResponseEntity<Integer> getBookCount(final @NonNull @PathVariable UUID id) {
        var bookCount = getService().getBookCount(id);
        return ResponseEntity.ok(bookCount);
    }

    // @@@@@   @@@@   @@@@  @@@@@
    // @    @ @    @ @        @
    // @    @ @    @  @@@@    @
    // @@@@@  @    @      @   @
    // @      @    @ @    @   @
    // @       @@@@   @@@@    @

    @PostMapping(value = "/category", produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<CategoryDto> create(
            final @RequestBody @Validated @NonNull CategoryDto dto) {
        var model = getService().create(dto);
        return ResponseEntity.ok(getMapper().toDto(model));
    }

    // @@@@@  @    @ @@@@@
    // @    @ @    @   @
    // @    @ @    @   @
    // @@@@@  @    @   @
    // @      @    @   @
    // @       @@@@    @

    @PutMapping(value = "/category/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<CategoryDto> update(
            @NonNull @PathVariable final UUID id,
            final @RequestBody @Validated @NonNull CategoryDto dto) {
        var model = getService().update(id, dto);
        return ResponseEntity.ok(getMapper().toDto(model));
    }

    // @@@@@  @@@@@@ @      @@@@@@ @@@@@ @@@@@@
    // @    @ @      @      @        @   @
    // @    @ @@@@@  @      @@@@@    @   @@@@@
    // @    @ @      @      @        @   @
    // @    @ @      @      @        @   @
    // @@@@@  @@@@@@ @@@@@@ @@@@@@   @   @@@@@@

    @DeleteMapping(value = "/category/{id}")
    public final ResponseEntity<Void> delete(@NonNull @PathVariable final UUID id) {
        getService().delete(id);
        return ResponseEntity.noContent().build();
    }
}
