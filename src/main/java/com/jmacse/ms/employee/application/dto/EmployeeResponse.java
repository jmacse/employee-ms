package com.jmacse.ms.employee.application.dto;

import com.jmacse.ms.employee.domain.model.Gender;
import com.jmacse.ms.employee.shared.DateConstants;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(name = "EmployeeResponse", description = "Employee data returned by the API")
public record EmployeeResponse(
    @Schema(description = "Unique identifier of the employee", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID id,

    @Schema(description = "First name", example = "Jorge")
    String firstName,

    @Schema(description = "Middle name", example = "Luis")
    String middleName,

    @Schema(description = "Paternal last name", example = "Martinez")
    String paternalLastName,

    @Schema(description = "Maternal last name", example = "Cruz")
    String maternalLastName,

    @Schema(description = "Full name composed of all name fields", example = "Jorge Luis Martinez Cruz")
    String fullName,

    @Schema(description = "Age calculated from birth date", example = "34")
    Integer age,

    @Schema(description = "Gender of the employee", example = "MALE")
    Gender gender,

    @Schema(description = "Birth date in format " + DateConstants.DATE_FORMAT, example = DateConstants.DATE_FORMAT_EXAMPLE)
    String birthDate,

    @Schema(description = "Job position", example = "Software Engineer")
    String position,

    @Schema(description = "Timestamp when the employee was registered in the system")
    Instant registrationDate,

    @Schema(description = "Whether the employee is currently active", example = "true")
    boolean active
) {}
