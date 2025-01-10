package urlshortener.exception.handler;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import urlshortener.exception.ApiException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException exception) {
        log.error("{}", exception.getMessage(), exception);
        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(ErrorResponse.builder()
                        .message(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        log.error("{}", exception.getMessage(), exception);
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.builder()
                        .message(exception.getMessage())
                        .build());
    }
}
