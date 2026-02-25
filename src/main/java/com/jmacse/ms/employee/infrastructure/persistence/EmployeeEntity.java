package com.jmacse.ms.employee.infrastructure.persistence;

import com.jmacse.ms.employee.domain.model.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "employees")
public class EmployeeEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "paternal_last_name", nullable = false)
    private String paternalLastName;

    @Column(name = "maternal_last_name", nullable = false)
    private String maternalLastName;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "position", nullable = false)
    private String position;

    @JdbcTypeCode(SqlTypes.TIMESTAMP_WITH_TIMEZONE)
    @Column(name = "registration_date", nullable = false, updatable = false)
    private Instant registrationDate;

    @Column(name = "active", nullable = false)
    private boolean active;

    protected EmployeeEntity() {}

    public EmployeeEntity(UUID id,
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
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.paternalLastName = paternalLastName;
        this.maternalLastName = maternalLastName;
        this.age = age;
        this.gender = gender;
        this.birthDate = birthDate;
        this.position = position;
        this.registrationDate = registrationDate;
        this.active = active;
    }

    public UUID getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getMiddleName() { return middleName; }
    public String getPaternalLastName() { return paternalLastName; }
    public String getMaternalLastName() { return maternalLastName; }
    public Integer getAge() { return age; }
    public Gender getGender() { return gender; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getPosition() { return position; }
    public Instant getRegistrationDate() { return registrationDate; }
    public boolean isActive() { return active; }
}
