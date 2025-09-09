package com.krainet.auth.core.models.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ValidationResult {
    boolean valid;
    String message;
}