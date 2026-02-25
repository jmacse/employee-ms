package com.jmacse.ms.employee.application.usecase;

import com.jmacse.ms.employee.application.dto.CreateEmployeeRequest;
import com.jmacse.ms.employee.application.dto.EmployeeResponse;
import com.jmacse.ms.employee.application.dto.UpdateEmployeeRequest;

import java.util.List;
import java.util.UUID;

public interface EmployeeCommandUseCase {

    List<EmployeeResponse> create(List<CreateEmployeeRequest> requests);
    EmployeeResponse update(UUID id, UpdateEmployeeRequest request);
    void delete(UUID id);

}