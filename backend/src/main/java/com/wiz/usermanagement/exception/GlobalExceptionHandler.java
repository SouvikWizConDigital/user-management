package com.wiz.usermanagement.exception;

import com.wiz.usermanagement.exception.dto.ApiErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* BUSINESS EXCEPTIONS */

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUserNotFound(UserNotFoundException ex, WebRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                "https://example.com/problems/user-not-found",
                "User Not Found",
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getDescription(false),
                Instant.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex, WebRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                "https://example.com/problems/email-already-exists",
                "Email Already Exists",
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getDescription(false),
                Instant.now(),
                null
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(UserAlreadyRestoredException.class)
    public ResponseEntity<ApiErrorResponse> handleUserAlreadyRestored(UserAlreadyRestoredException ex, WebRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                "https://example.com/problems/user-already-restored",
                "User Already Restored",
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getDescription(false),
                Instant.now(),
                null
        );

        return ResponseEntity.badRequest().body(response);
    }

    /* VALIDATION (@RequestBody) */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        fieldErrors.put(error.getField(), error.getDefaultMessage())
                );

        ApiErrorResponse response = new ApiErrorResponse(
                "https://example.com/problems/validation-error",
                "Validation Failed",
                HttpStatus.BAD_REQUEST.value(),
                "Request body validation failed",
                request.getDescription(false),
                Instant.now(),
                fieldErrors
        );

        return ResponseEntity.badRequest().body(response);
    }

    /* VALIDATION (Path / Query) */

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();

        ex.getConstraintViolations().forEach(violation -> {
            String field = violation.getPropertyPath().toString();
            fieldErrors.put(field, violation.getMessage());
        });

        ApiErrorResponse response = new ApiErrorResponse(
                "https://example.com/problems/constraint-violation",
                "Constraint Violation",
                HttpStatus.BAD_REQUEST.value(),
                "Invalid request parameters",
                request.getDescription(false),
                Instant.now(),
                fieldErrors
        );

        return ResponseEntity.badRequest().body(response);
    }

    /* SECURITY EXCEPTIONS */

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(AuthenticationException ex, WebRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                "https://example.com/problems/unauthorized",
                "Unauthorized",
                HttpStatus.UNAUTHORIZED.value(),
                "Authentication required",
                request.getDescription(false),
                Instant.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex, WebRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                "https://example.com/problems/forbidden",
                "Access Denied",
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage(),
                request.getDescription(false),
                Instant.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /* FALLBACK EXCEPTIONS */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalException(Exception ex, WebRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                "https://example.com/problems/internal-server-error",
                "Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred",
                request.getDescription(false),
                Instant.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
