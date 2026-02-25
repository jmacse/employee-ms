package com.jmacse.ms.employee.application;

import com.jmacse.ms.employee.application.dto.CreateEmployeeRequest;
import com.jmacse.ms.employee.application.dto.EmployeeResponse;
import com.jmacse.ms.employee.application.dto.UpdateEmployeeRequest;
import com.jmacse.ms.employee.application.usecase.EmployeeUseCase;
import com.jmacse.ms.employee.domain.exception.EmployeeNotFoundException;
import com.jmacse.ms.employee.domain.model.Employee;
import com.jmacse.ms.employee.domain.repository.EmployeeRepository;
import com.jmacse.ms.employee.infrastructure.persistence.EmployeeMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class EmployeeService implements EmployeeUseCase {

    private final EmployeeRepository employeeRepository;

    @Inject
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<EmployeeResponse> findAll() {
        return employeeRepository.findAll()
                .stream()
                .map(EmployeeMapper::toResponse)
                .toList();
    }

    @Override
    public EmployeeResponse findById(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        return EmployeeMapper.toResponse(employee);
    }

    @Override
    public List<EmployeeResponse> create(List<CreateEmployeeRequest> requests) {
        List<Employee> employees = requests.stream()
                .map(EmployeeMapper::toDomain)
                .toList();

        return employeeRepository.saveAll(employees)
                .stream()
                .map(EmployeeMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public EmployeeResponse update(UUID id, UpdateEmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        if (request.position() != null && !request.position().isBlank()) {
            employee = employee.promote(request.position());
        }

        if (request.birthDate() != null && !request.birthDate().isBlank()) {
            employee = employee.updateBirthDate(request.birthDate());
        }

        if (request.active() != null) {
            employee = request.active() ? employee.activate() : employee.deactivate();
        }

        return EmployeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Override
    public void delete(UUID id) {
        employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        employeeRepository.deleteById(id);
    }

    @Override
    public List<EmployeeResponse> searchByName(String name) {
        return employeeRepository.searchByName(name)
                .stream()
                .map(EmployeeMapper::toResponse)
                .toList();
    }
}
