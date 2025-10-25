package com.seti.nequi.test.infrastructure.entrypoints.web.dto;

import jakarta.validation.constraints.Min;

public record UpdateCountRequest(@Min(0) int count) {}
