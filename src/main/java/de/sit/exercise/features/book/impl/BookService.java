package de.sit.exercise.features.book.impl;

import org.springframework.stereotype.Service;

import de.sit.exercise.features.book.BookMapper;
import de.sit.exercise.features.book.BookRepository;
import de.sit.exercise.features.book.IBookService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * BookSerivce implemenation
 *
 * The generic crud implemmentions are in the base interface (IService).
 */
@Service
@Getter
@RequiredArgsConstructor
public class BookService implements IBookService {
    private String entityName = "Book";
    private final BookMapper mapper;
    private final BookRepository repository;
}
