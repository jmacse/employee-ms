package com.jmacse.ms.employee.infrastructure.persistence;

import com.jmacse.ms.employee.application.dto.CreateEmployeeRequest;
import com.jmacse.ms.employee.application.dto.EmployeeResponse;
import com.jmacse.ms.employee.domain.model.Employee;

public class EmployeeMapper {

    private EmployeeMapper() {}

    public static Employee toDomain(CreateEmployeeRequest request) {
        return new Employee(
                request.firstName(),
                request.middleName(),
                request.paternalLastName(),
                request.maternalLastName(),
                request.birthDate(),
                request.gender(),
                request.position()
        );
    }

    public static EmployeeEntity toEntity(Employee employee) {
        return new EmployeeEntity(
                employee.getId(),
                employee.getFirstName(),
                employee.getMiddleName(),
                employee.getPaternalLastName(),
                employee.getMaternalLastName(),
                employee.getAge(),
                employee.getGender(),
                employee.getBirthDate(),
                employee.getPosition(),
                employee.getRegistrationDate(),
                employee.isActive()
        );
    }

    public static Employee toDomain(EmployeeEntity entity) {
        return Employee.reconstitute(
                entity.getId(),
                entity.getFirstName(),
                entity.getMiddleName(),
                entity.getPaternalLastName(),
                entity.getMaternalLastName(),
                entity.getAge(),
                entity.getGender(),
                entity.getBirthDate(),
                entity.getPosition(),
                entity.getRegistrationDate(),
                entity.isActive()
        );
    }

    public static EmployeeResponse toResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getMiddleName(),
                employee.getPaternalLastName(),
                employee.getMaternalLastName(),
                employee.getFullName(),
                employee.getAge(),
                employee.getGender(),
                employee.getBirthDateFormatted(),
                employee.getPosition(),
                employee.getRegistrationDate(),
                employee.isActive()
        );
    }
}
