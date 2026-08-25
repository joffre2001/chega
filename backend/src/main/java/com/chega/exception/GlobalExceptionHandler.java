package com.chega.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(EmailAlreadyRegisteredException.class)
        public ResponseEntity<ApiError> handleEmailAlreadyRegistered(
                        EmailAlreadyRegisteredException exception,
                        HttpServletRequest request) {
                HttpStatus status = HttpStatus.CONFLICT;

                ApiError error = new ApiError(
                                Instant.now(),
                                status.value(),
                                status.getReasonPhrase(),
                                exception.getMessage(),
                                request.getRequestURI(),
                                Map.of());

                return ResponseEntity
                                .status(status)
                                .body(error);
        }

        @ExceptionHandler(InvalidCredentialsException.class)
        public ResponseEntity<ApiError> handleInvalidCredentials(
                        InvalidCredentialsException exception,
                        HttpServletRequest request) {
                HttpStatus status = HttpStatus.UNAUTHORIZED;

                ApiError error = new ApiError(
                                Instant.now(),
                                status.value(),
                                status.getReasonPhrase(),
                                exception.getMessage(),
                                request.getRequestURI(),
                                Map.of());

                return ResponseEntity
                                .status(status)
                                .body(error);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiError> handleValidation(
                        MethodArgumentNotValidException exception,
                        HttpServletRequest request) {
                HttpStatus status = HttpStatus.BAD_REQUEST;
                Map<String, String> fieldErrors = new LinkedHashMap<>();

                for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {

                        fieldErrors.putIfAbsent(
                                        fieldError.getField(),
                                        fieldError.getDefaultMessage());
                }

                ApiError error = new ApiError(
                                Instant.now(),
                                status.value(),
                                status.getReasonPhrase(),
                                "Existem campos inválidos na requisição.",
                                request.getRequestURI(),
                                fieldErrors);

                return ResponseEntity
                                .status(status)
                                .body(error);
        }

        @ExceptionHandler(MigrantProfileAlreadyExistsException.class)
        public ResponseEntity<ApiError> handleProfileAlreadyExists(
                        MigrantProfileAlreadyExistsException exception,
                        HttpServletRequest request) {
                HttpStatus status = HttpStatus.CONFLICT;

                ApiError error = new ApiError(
                                Instant.now(),
                                status.value(),
                                status.getReasonPhrase(),
                                exception.getMessage(),
                                request.getRequestURI(),
                                Map.of());

                return ResponseEntity
                                .status(status)
                                .body(error);
        }

        @ExceptionHandler(ConsentRequiredException.class)
        public ResponseEntity<ApiError> handleConsentRequired(
                        ConsentRequiredException exception,
                        HttpServletRequest request) {
                HttpStatus status = HttpStatus.BAD_REQUEST;

                ApiError error = new ApiError(
                                Instant.now(),
                                status.value(),
                                status.getReasonPhrase(),
                                exception.getMessage(),
                                request.getRequestURI(),
                                Map.of());

                return ResponseEntity
                                .status(status)
                                .body(error);
        }

        @ExceptionHandler(MigrantProfileNotFoundException.class)
        public ResponseEntity<ApiError> handleProfileNotFound(
                        MigrantProfileNotFoundException exception,
                        HttpServletRequest request) {
                HttpStatus status = HttpStatus.NOT_FOUND;

                ApiError error = new ApiError(
                                Instant.now(),
                                status.value(),
                                status.getReasonPhrase(),
                                exception.getMessage(),
                                request.getRequestURI(),
                                Map.of());

                return ResponseEntity
                                .status(status)
                                .body(error);
        }

        @ExceptionHandler(DocumentRequirementNotFoundException.class)
        public ResponseEntity<ApiError> handleDocumentRequirementNotFound(
                        DocumentRequirementNotFoundException exception,
                        HttpServletRequest request) {
                HttpStatus status = HttpStatus.NOT_FOUND;

                ApiError error = new ApiError(
                                Instant.now(),
                                status.value(),
                                status.getReasonPhrase(),
                                exception.getMessage(),
                                request.getRequestURI(),
                                Map.of());

                return ResponseEntity
                                .status(status)
                                .body(error);
        }
}