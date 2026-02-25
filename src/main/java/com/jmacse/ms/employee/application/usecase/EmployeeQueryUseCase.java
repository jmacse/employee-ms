package com.jmacse.ms.employee.application.usecase;

import com.jmacse.ms.employee.application.dto.EmployeeResponse;

import java.util.List;
import java.util.UUID;

public interface EmployeeQueryUseCase {

    List<EmployeeResponse> findAll();
    EmployeeResponse findById(UUID id);
    List<EmployeeResponse> searchByName(String name);

}

