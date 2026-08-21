package dev.khondamir.eventmanager.web;


import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ServerErrorDto> handleValidationException(
            Exception ex
    ) {
        log.error("Got validation exception", ex);
        String detailedMessage = ex instanceof MethodArgumentNotValidException
                ? constructMethodArgumentNotValidMessage((MethodArgumentNotValidException) ex)
                : ex.getMessage();
        var errorDto = new ServerErrorDto(
                "Validation error",
                detailedMessage,
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }

    private static String constructMethodArgumentNotValidMessage(
            MethodArgumentNotValidException ex
    ) {
        return ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }

    @ExceptionHandler
    public ResponseEntity<ServerErrorDto> handleGenericException(
            Exception ex
    ) {
        log.error("Server error", ex);
        var result = new ServerErrorDto(
                "Server error",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(result);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ServerErrorDto> handleNotFoundException(
            EntityNotFoundException ex
    ) {
        log.error("Get exception", ex);
        var result = new ServerErrorDto(
                "Сущность не найдена",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(result);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ServerErrorDto> handleForbiddenException(
            ForbiddenException ex
    ) {
        log.error("Forbidden", ex);
        var result = new ServerErrorDto(
                "Недостаточно прав для выполнения операции",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(result);
    }

}
