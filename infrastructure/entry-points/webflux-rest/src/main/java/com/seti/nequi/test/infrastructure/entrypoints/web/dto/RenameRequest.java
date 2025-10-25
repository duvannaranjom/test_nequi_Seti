package com.seti.nequi.test.infrastructure.entrypoints.web.dto;

import jakarta.validation.constraints.NotBlank;

public record RenameRequest(@NotBlank String name) {}
