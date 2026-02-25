package com.jmacse.ms.employee.domain.model;

import com.jmacse.ms.employee.domain.exception.InvalidEmployeeFieldException;
import com.jmacse.ms.employee.domain.exception.InvalidEmployeeStateException;
import com.jmacse.ms.employee.shared.DateConstants;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Employee {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT);

    private UUID id;
    private String firstName;
    private String middleName;
    private String paternalLastName;
    private String maternalLastName;
    private Integer age;
    private Gender gender;
    private LocalDate birthDate;
    private String position;
    private Instant registrationDate;
    private boolean active;

    public Employee(String firstName,
                    String middleName,
                    String paternalLastName,
                    String maternalLastName,
                    String birthDate,
                    Gender gender,
                    String position) {
        this.id = UUID.randomUUID();
        this.firstName = validateNotBlank(firstName, "First name");
        this.middleName = middleName;
        this.paternalLastName = validateNotBlank(paternalLastName, "Paternal last name");
        this.maternalLastName = validateNotBlank(maternalLastName, "Maternal last name");
        this.birthDate = parseBirthDate(birthDate);
        this.age = calculateAge(this.birthDate);
        this.gender = validateNotNull(gender, "Gender");
        this.position = validateNotBlank(position, "Position");
        this.registrationDate = Instant.now();
        this.active = true;
    }

    private Employee() {
    }

    public static Employee reconstitute(UUID id,
                                        String firstName,
                                        String middleName,
                                        String paternalLastName,
                                        String maternalLastName,
                                        Integer age,
                                        Gender gender,
                                        LocalDate birthDate,
                                        String position,
                                        Instant registrationDate,
                                        boolean active) {
        Employee employee = new Employee();
        employee.id = id;
        employee.firstName = firstName;
        employee.middleName = middleName;
        employee.paternalLastName = paternalLastName;
        employee.maternalLastName = maternalLastName;
        employee.age = age;
        employee.gender = gender;
        employee.birthDate = birthDate;
        employee.position = position;
        employee.registrationDate = registrationDate;
        employee.active = active;
        return employee;
    }

    public Employee promote(String newPosition) {
        validateActive("promoted");
        validateNotBlank(newPosition, "New position");
        if (this.position.equalsIgnoreCase(newPosition)) {
            throw new InvalidEmployeeFieldException("Employee already holds the position: " + newPosition);
        }
        return Employee.reconstitute(id, firstName, middleName, paternalLastName, maternalLastName,
                age, gender, birthDate, newPosition, registrationDate, active);
    }

    public Employee deactivate() {
        if (!this.active) {
            throw new InvalidEmployeeStateException("Employee is already inactive");
        }
        return Employee.reconstitute(id, firstName, middleName, paternalLastName, maternalLastName,
                age, gender, birthDate, position, registrationDate, false);
    }

    public Employee activate() {
        if (this.active) {
            throw new InvalidEmployeeStateException("Employee is already active");
        }
        return Employee.reconstitute(id, firstName, middleName, paternalLastName, maternalLastName,
                age, gender, birthDate, position, registrationDate, true);
    }

    public Employee updateBirthDate(String newBirthDate) {
        LocalDate parsedDate = parseBirthDate(newBirthDate);
        return Employee.reconstitute(id, firstName, middleName, paternalLastName, maternalLastName,
                calculateAge(parsedDate), gender, parsedDate, position, registrationDate, active);
    }

    public String getFullName() {
        return Stream.of(firstName, middleName, paternalLastName, maternalLastName)
                .filter(s -> s != null && !s.isBlank())
                .collect(Collectors.joining(" "));
    }

    private LocalDate parseBirthDate(String birthDate) {
        try {
            LocalDate date = LocalDate.parse(validateNotBlank(birthDate, "Birth date"), DATE_FORMATTER);
            if (date.isAfter(LocalDate.now())) {
                throw new InvalidEmployeeFieldException("Birth date cannot be in the future");
            }
            return date;
        } catch (DateTimeParseException e) {
            throw new InvalidEmployeeFieldException("Birth date must follow the format " + DateConstants.DATE_FORMAT);
        }
    }

    private Integer calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    private void validateActive(String action) {
        if (!this.active) {
            throw new InvalidEmployeeStateException("An inactive employee cannot be " + action);
        }
    }

    private String validateNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidEmployeeFieldException(fieldName + " must not be blank");
        }
        return value.trim();
    }

    private <T> T validateNotNull(T value, String fieldName) {
        if (value == null) {
            throw new InvalidEmployeeFieldException(fieldName + " must not be null");
        }
        return value;
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getPaternalLastName() {
        return paternalLastName;
    }

    public String getMaternalLastName() {
        return maternalLastName;
    }

    public Integer getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getBirthDateFormatted() {
        return birthDate.format(DATE_FORMATTER);
    }

    public String getPosition() {
        return position;
    }

    public Instant getRegistrationDate() {
        return registrationDate;
    }

    public boolean isActive() {
        return active;
    }
}
