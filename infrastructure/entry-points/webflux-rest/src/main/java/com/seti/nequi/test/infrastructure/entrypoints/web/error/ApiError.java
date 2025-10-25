package com.seti.nequi.test.infrastructure.entrypoints.web.error;

import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.List;

@Value
@Builder
public class ApiError {
    String path;
    int status;
    String error;       // reason phrase
    String message;     // human-readable
    String code;        // optional app-specific code
    OffsetDateTime timestamp;
    List<ApiFieldError> details;
}
