package com.seti.nequi.test.infrastructure.entrypoints.web.error;

import com.seti.nequi.test.domain.model.exception.DomainException;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @PostConstruct
    public void onLoad() {
        log.info("[GlobalExceptionHandler] loaded");
    }

    // ===== Helpers =====
    private ApiError build(HttpStatus status, String path, String message, String code, List<ApiFieldError> details) {
        return ApiError.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .path(path)
                .message(message != null && !message.isBlank() ? message : status.getReasonPhrase())
                .code(code)
                .details(details)
                .build();
    }

    private ResponseEntity<ApiError> response(HttpStatus status, String path, String message, String code, List<ApiFieldError> details) {
        return ResponseEntity.status(status).body(build(status, path, message, code, details));
    }

    private ApiFieldError field(String field, String msg, Object rejected) {
        return ApiFieldError.builder().field(field).message(msg).rejectedValue(rejected).build();
    }

    // ===== Validación WebFlux: @Valid en @RequestBody =====
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ApiError>> handleWebFluxBind(WebExchangeBindException ex,
                                                            org.springframework.web.server.ServerWebExchange exchange) {
        log.debug("WebExchangeBindException: {}", ex.getMessage());
        List<ApiFieldError> details = ex.getFieldErrors().stream()
                .map(fe -> field(fe.getField(), fe.getDefaultMessage(), fe.getRejectedValue()))
                .toList();
        return Mono.just(response(HttpStatus.BAD_REQUEST, exchange.getRequest().getPath().value(),
                "Validation failed", "VALIDATION_ERROR", details));
    }

    // (Compatibilidad MVC / tests)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ResponseEntity<ApiError>> handleMethodArgNotValid(MethodArgumentNotValidException ex,
                                                                  org.springframework.web.server.ServerWebExchange exchange) {
        List<ApiFieldError> details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> field(fe.getField(), fe.getDefaultMessage(), fe.getRejectedValue()))
                .toList();
        return Mono.just(response(HttpStatus.BAD_REQUEST, exchange.getRequest().getPath().value(),
                "Validation failed", "VALIDATION_ERROR", details));
    }

    // Constraint en @PathVariable / @RequestParam
    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ResponseEntity<ApiError>> handleConstraint(ConstraintViolationException ex,
                                                           org.springframework.web.server.ServerWebExchange exchange) {
        List<ApiFieldError> details = ex.getConstraintViolations().stream()
                .map(cv -> field(
                        cv.getPropertyPath() != null ? cv.getPropertyPath().toString() : null,
                        cv.getMessage(),
                        cv.getInvalidValue()))
                .toList();
        return Mono.just(response(HttpStatus.BAD_REQUEST, exchange.getRequest().getPath().value(),
                "Constraint violation", "CONSTRAINT_VIOLATION", details));
    }

    // Body mal formado / tipo inválido / JSON inválido
    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ResponseEntity<ApiError>> handleServerWebInput(ServerWebInputException ex,
                                                               org.springframework.web.server.ServerWebExchange exchange) {
        log.debug("ServerWebInputException: {}", ex.getMessage());
        return Mono.just(response(HttpStatus.BAD_REQUEST, exchange.getRequest().getPath().value(),
                "Malformed request", "MALFORMED_REQUEST", null));
    }

    // Errores de negocio
    @ExceptionHandler(DomainException.class)
    public Mono<ResponseEntity<ApiError>> handleDomain(DomainException ex,
                                                       org.springframework.web.server.ServerWebExchange exchange) {
        log.warn("DomainException: {}", ex.getMessage());
        return Mono.just(response(HttpStatus.UNPROCESSABLE_ENTITY, exchange.getRequest().getPath().value(),
                ex.getMessage(), "DOMAIN_ERROR", null));
    }

    // ResponseStatusException (Spring)
    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ResponseEntity<ApiError>> handleRSE(ResponseStatusException ex,
                                                    org.springframework.web.server.ServerWebExchange exchange) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        if (status == null) status = HttpStatus.INTERNAL_SERVER_ERROR;
        return Mono.just(response(status, exchange.getRequest().getPath().value(),
                ex.getReason(), "ERROR", null));
    }

    // Fallback
    @ExceptionHandler(Throwable.class)
    public Mono<ResponseEntity<ApiError>> handleAny(Throwable ex,
                                                    org.springframework.web.server.ServerWebExchange exchange) {
        log.error("Unexpected error", ex);
        return Mono.just(response(HttpStatus.INTERNAL_SERVER_ERROR, exchange.getRequest().getPath().value(),
                "Unexpected error", "INTERNAL_ERROR", null));
    }
}
