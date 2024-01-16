package de.sit.exercise.features.book;

import de.sit.exercise.util.service.IService;

/**
 * BookSerivce interface.
 *
 * The generic crud implemmention is in the base interface (IService).
 */
public interface IBookService extends IService<Book, BookDto> {
}
