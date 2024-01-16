package de.sit.exercise.exception;

import java.sql.SQLException;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * Exception Advice for getting detailed error messages when an error occurs in
 * rest methods
 */
@RestControllerAdvice
@Slf4j
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    private static final String PROBLEM_ERROR_CATEGORY = "errorCategory";
    private static final String PROBLEM_TIMESTAMP = "timestamp";

    /**
     *
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public final ProblemDetail handleAccessDeniedException(final AccessDeniedException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
        problemDetail.setTitle("Access Denied");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setProperty(PROBLEM_ERROR_CATEGORY, "AUTH");
        problemDetail.setProperty(PROBLEM_TIMESTAMP, Instant.now());
        return problemDetail;
    }

    /**
     *
     */
    @ExceptionHandler(value = { SQLException.class })
    public final ProblemDetail handleSQLException(final SQLException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        problemDetail.setTitle("SQL failure");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setProperty(PROBLEM_ERROR_CATEGORY, "SQL");
        problemDetail.setProperty(PROBLEM_TIMESTAMP, Instant.now());
        return problemDetail;
    }

    /**
     *
     */
    @ExceptionHandler(value = { EntityNotFoundException.class, jakarta.persistence.EntityNotFoundException.class })
    public final ProblemDetail handleEntityNotFoundException(final RuntimeException e) {
        log.error("Entity not found", e);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Entity not found");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setProperty(PROBLEM_TIMESTAMP, Instant.now());
        return problemDetail;
    }
}
