package com.jmacse.ms.employee.domain.exception;

import java.util.UUID;

public class EmployeeNotFoundException extends EmployeeDomainException {

    public EmployeeNotFoundException(UUID id) {
        super("Employee with id " + id + " was not found");
    }
}

