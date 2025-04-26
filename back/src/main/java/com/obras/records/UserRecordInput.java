package com.obras.records;

import jakarta.validation.constraints.NotBlank;

public record UserRecordInput(@NotBlank String name, @NotBlank String password) {}