package com.krainet.auth.core.models.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.krainet.auth.core.constants.DateTimeConstants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorMessage {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeConstants.DATE_TIME_FORMAT)
    LocalDateTime dateTime;
    String message;
    String description;
}