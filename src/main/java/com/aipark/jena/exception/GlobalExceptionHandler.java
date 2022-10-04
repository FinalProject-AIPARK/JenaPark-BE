package com.aipark.jena.exception;

import com.aipark.jena.dto.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final Response response;

    @ExceptionHandler(value = {CustomException.class})
    protected ResponseEntity<Response.Body> handleCustomException(CustomException e) {
        log.error("throw CustomException : {}, {}", e.getHttpStatus(), e.getMessage());
        return response.fail(e.getMessage(), e.getHttpStatus());
    }
}
