package com.jmacse.ms.employee.domain.repository;

import com.jmacse.ms.employee.domain.model.Employee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends EmployeeStatisticsRepository {

    List<Employee> findAll();

    Optional<Employee> findById(UUID id);

    Employee save(Employee employee);

    List<Employee> saveAll(List<Employee> employees);

    void deleteById(UUID id);

    List<Employee> searchByName(String name);
}

