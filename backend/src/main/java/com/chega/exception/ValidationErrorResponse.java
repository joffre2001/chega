package com.chega.exception;

import java.time.Instant;
import java.util.Map;

public record ValidationErrorResponse(

        int status,
        String error,
        String message,
        String path,
        Map<String, String> fieldErrors,
        Instant timestamp

) {
}