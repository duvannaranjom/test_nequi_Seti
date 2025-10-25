package com.seti.nequi.test.infrastructure.entrypoints.web.error;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ApiFieldError {
    String field;
    String message;
    Object rejectedValue;
}
