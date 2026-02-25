package com.jmacse.ms.employee.application.dto;

import com.jmacse.ms.employee.shared.DateConstants;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "UpdateEmployeeRequest", description = "Payload to update an existing employee. All fields are optional.")
public record UpdateEmployeeRequest(
    @Schema(description = "Birth date in format " + DateConstants.DATE_FORMAT, example = DateConstants.DATE_FORMAT_EXAMPLE)
    String birthDate,

    @Schema(description = "Job position", example = "Senior Software Engineer")
    String position,

    @Schema(description = "Active or inactive status of the employee", example = "false")
    Boolean active
) {}
