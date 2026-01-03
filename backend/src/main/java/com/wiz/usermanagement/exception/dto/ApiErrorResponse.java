package com.wiz.usermanagement.exception.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
@AllArgsConstructor
@Schema(name = "ApiErrorResponse", description = "RFC 7807 compliant error response")
public class ApiErrorResponse {

    @Schema(example = "https://example.com/problems/validation-error")
    private String type;

    @Schema(example = "Validation Failed")
    private String title;

    @Schema(example = "400")
    private int status;

    @Schema(example = "Request validation failed")
    private String detail;

    @Schema(example = "/user/adduser")
    private String instance;

    // RFC-7807 allows extensions
    private Instant timestamp;

    @Schema(description = "Field level validation errors")
    private Map<String, String> errors;
}
