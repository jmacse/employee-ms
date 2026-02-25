package com.jmacse.ms.employee.domain.exception;

public abstract class EmployeeDomainException extends RuntimeException {

    protected EmployeeDomainException(String message) {
        super(message);
    }
}

