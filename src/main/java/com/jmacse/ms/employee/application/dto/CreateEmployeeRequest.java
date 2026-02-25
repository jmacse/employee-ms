package com.jmacse.ms.employee.application.dto;

import com.jmacse.ms.employee.domain.model.Gender;
import com.jmacse.ms.employee.shared.DateConstants;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "CreateEmployeeRequest", description = "Payload to create a new employee")
public record CreateEmployeeRequest(
    @Schema(description = "First name of the employee", example = "Jorge", required = true)
    String firstName,

    @Schema(description = "Middle name of the employee", example = "Luis")
    String middleName,

    @Schema(description = "Paternal last name", example = "Martinez", required = true)
    String paternalLastName,

    @Schema(description = "Maternal last name", example = "Cruz")
    String maternalLastName,

    @Schema(description = "Birth date in format " + DateConstants.DATE_FORMAT, example = DateConstants.DATE_FORMAT_EXAMPLE, required = true)
    String birthDate,

    @Schema(description = "Gender of the employee", example = "MALE", required = true)
    Gender gender,

    @Schema(description = "Job position", example = "Software Engineer", required = true)
    String position
) {}
