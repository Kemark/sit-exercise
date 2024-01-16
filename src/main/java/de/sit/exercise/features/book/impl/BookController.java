package de.sit.exercise.features.book.impl;

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

import de.sit.exercise.features.book.BookDto;
import de.sit.exercise.features.book.BookMapper;
import de.sit.exercise.features.book.IBookService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class BookController {

    private final BookMapper mapper;

    private final IBookService service;

    // @@@@  @@@@@@ @@@@@
    // @    @ @        @
    // @      @@@@@    @
    // @  @@@ @        @
    // @    @ @        @
    //  @@@@  @@@@@@   @

    @GetMapping(value = "/book/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BookDto> getById(final @NonNull @PathVariable UUID id) {
        var model = service.findById(id);
        return ResponseEntity.ok(mapper.toDto(model));
    }

    @GetMapping(value = "/book", produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<Page<BookDto>> getFiltered(@NonNull final Pageable pageable,
            @RequestParam(required = false) final String searchString) {
        var pagedModels = service.findFiltered(pageable, searchString);
        return ResponseEntity.ok(mapper.toPage(pagedModels, pageable));
    }

    // @@@@@   @@@@   @@@@  @@@@@
    // @    @ @    @ @        @
    // @    @ @    @  @@@@    @
    // @@@@@  @    @      @   @
    // @      @    @ @    @   @
    // @       @@@@   @@@@    @

    @PostMapping(value = "/book", produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BookDto> create(
            final @RequestBody @Validated @NonNull BookDto dto) {
        var model = service.create(dto);
        return ResponseEntity.ok(mapper.toDto(model));
    }

    // @@@@@  @    @ @@@@@
    // @    @ @    @   @
    // @    @ @    @   @
    // @@@@@  @    @   @
    // @      @    @   @
    // @       @@@@    @

    @PutMapping(value = "/book/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BookDto> update(
            @NonNull @PathVariable final UUID id,
            final @RequestBody @Validated @NonNull BookDto dto) {
        var model = service.update(id, dto);
        return ResponseEntity.ok(mapper.toDto(model));
    }

    // @@@@@  @@@@@@ @      @@@@@@ @@@@@ @@@@@@
    // @    @ @      @      @        @   @
    // @    @ @@@@@  @      @@@@@    @   @@@@@
    // @    @ @      @      @        @   @
    // @    @ @      @      @        @   @
    // @@@@@  @@@@@@ @@@@@@ @@@@@@   @   @@@@@@

    @DeleteMapping(value = "/book/{id}")
    public final ResponseEntity<Void> delete(@NonNull @PathVariable final UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
