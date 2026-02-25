package com.jmacse.ms.employee.domain.model;

import com.jmacse.ms.employee.domain.exception.InvalidEmployeeFieldException;
import com.jmacse.ms.employee.domain.exception.InvalidEmployeeStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Employee Domain Model")
class EmployeeTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final String DEFAULT_BIRTH_DATE = "15-06-1990";

    private Employee buildEmployee() {
        return new Employee(
                "Jorge",
                "Luis",
                "Martinez",
                "Castro",
                DEFAULT_BIRTH_DATE,
                Gender.MALE,
                "Software Engineer"
        );
    }

    @Nested
    @DisplayName("Creation")
    class Creation {

        @Test
        @DisplayName("should create employee with valid data")
        void shouldCreateEmployeeWithValidData() {
            // Act
            Employee employee = buildEmployee();

            // Assert
            assertAll(
                    () -> assertNotNull(employee.getId()),
                    () -> assertEquals("Jorge", employee.getFirstName()),
                    () -> assertEquals("Luis", employee.getMiddleName()),
                    () -> assertEquals("Martinez", employee.getPaternalLastName()),
                    () -> assertEquals("Castro", employee.getMaternalLastName()),
                    () -> assertEquals(Gender.MALE, employee.getGender()),
                    () -> assertEquals("Software Engineer", employee.getPosition()),
                    () -> assertNotNull(employee.getRegistrationDate()),
                    () -> assertTrue(employee.isActive()),
                    () -> assertNotNull(employee.getBirthDate()),
                    () -> assertNotNull(employee.getAge())
            );
        }

        @Test
        @DisplayName("should calculate age based on birth date")
        void shouldCalculateAgeFromBirthDate() {
            // Arrange
            int expectedAge = Period.between(
                    LocalDate.parse(DEFAULT_BIRTH_DATE, FORMATTER), LocalDate.now()
            ).getYears();

            // Act
            Employee employee = buildEmployee();

            // Assert
            assertEquals(expectedAge, employee.getAge());
        }

        @Test
        @DisplayName("should accept null middle name")
        void shouldAcceptNullMiddleName() {
            // Act
            Employee employee = new Employee(
                    "Ana", null, "Lopez", "Gomez",
                    "20-03-1995", Gender.FEMALE, "Designer"
            );

            // Assert
            assertNull(employee.getMiddleName());
        }

        @Test
        @DisplayName("should accept blank middle name")
        void shouldAcceptBlankMiddleName() {
            // Act
            Employee employee = new Employee(
                    "Ana", "   ", "Lopez", "Gomez",
                    "20-03-1995", Gender.FEMALE, "Designer"
            );

            // Assert
            assertEquals("   ", employee.getMiddleName());
        }

        @Test
        @DisplayName("should trim leading/trailing spaces in required fields")
        void shouldTrimFields() {
            // Act
            Employee employee = new Employee(
                    "  Jorge  ", "Luis", "  Martinez  ", "  Castro  ",
                    DEFAULT_BIRTH_DATE, Gender.MALE, "  Engineer  "
            );

            // Assert
            assertAll(
                    () -> assertEquals("Jorge", employee.getFirstName()),
                    () -> assertEquals("Martinez", employee.getPaternalLastName()),
                    () -> assertEquals("Castro", employee.getMaternalLastName()),
                    () -> assertEquals("Engineer", employee.getPosition())
            );
        }

        @Test
        @DisplayName("should throw InvalidEmployeeFieldException when first name is blank")
        void shouldThrowWhenFirstNameIsBlank() {
            // Act & Assert
            InvalidEmployeeFieldException ex = assertThrows(
                    InvalidEmployeeFieldException.class,
                    () -> new Employee("", "Luis", "Martinez", "Castro",
                            DEFAULT_BIRTH_DATE, Gender.MALE, "Engineer")
            );
            assertTrue(ex.getMessage().contains("First name"));
        }

        @Test
        @DisplayName("should throw InvalidEmployeeFieldException when first name is null")
        void shouldThrowWhenFirstNameIsNull() {
            // Act & Assert
            InvalidEmployeeFieldException ex = assertThrows(
                    InvalidEmployeeFieldException.class,
                    () -> new Employee(null, "Luis", "Martinez", "Castro",
                            DEFAULT_BIRTH_DATE, Gender.MALE, "Engineer")
            );
            assertTrue(ex.getMessage().contains("First name"));
        }

        @Test
        @DisplayName("should throw InvalidEmployeeFieldException when paternal last name is blank")
        void shouldThrowWhenPaternalLastNameIsBlank() {
            // Act & Assert
            InvalidEmployeeFieldException ex = assertThrows(
                    InvalidEmployeeFieldException.class,
                    () -> new Employee("Jorge", "Luis", "  ", "Castro",
                            DEFAULT_BIRTH_DATE, Gender.MALE, "Engineer")
            );
            assertTrue(ex.getMessage().contains("Paternal last name"));
        }

        @Test
        @DisplayName("should throw InvalidEmployeeFieldException when maternal last name is blank")
        void shouldThrowWhenMaternalLastNameIsBlank() {
            // Act & Assert
            InvalidEmployeeFieldException ex = assertThrows(
                    InvalidEmployeeFieldException.class,
                    () -> new Employee("Jorge", "Luis", "Martinez", "",
                            DEFAULT_BIRTH_DATE, Gender.MALE, "Engineer")
            );
            assertTrue(ex.getMessage().contains("Maternal last name"));
        }

        @Test
        @DisplayName("should throw InvalidEmployeeFieldException when position is blank")
        void shouldThrowWhenPositionIsBlank() {
            // Act & Assert
            InvalidEmployeeFieldException ex = assertThrows(
                    InvalidEmployeeFieldException.class,
                    () -> new Employee("Jorge", "Luis", "Martinez", "Castro",
                            DEFAULT_BIRTH_DATE, Gender.MALE, "")
            );
            assertTrue(ex.getMessage().contains("Position"));
        }

        @Test
        @DisplayName("should throw InvalidEmployeeFieldException when gender is null")
        void shouldThrowWhenGenderIsNull() {
            // Act & Assert
            InvalidEmployeeFieldException ex = assertThrows(
                    InvalidEmployeeFieldException.class,
                    () -> new Employee("Jorge", "Luis", "Martinez", "Castro",
                            DEFAULT_BIRTH_DATE, null, "Engineer")
            );
            assertTrue(ex.getMessage().contains("Gender"));
        }
    }

    @Nested
    @DisplayName("Birth Date Validation")
    class BirthDateValidation {

        @Test
        @DisplayName("should throw InvalidEmployeeFieldException when birth date format is invalid")
        void shouldThrowWhenBirthDateFormatIsInvalid() {
            // Act & Assert
            InvalidEmployeeFieldException ex = assertThrows(
                    InvalidEmployeeFieldException.class,
                    () -> new Employee("Jorge", "Luis", "Martinez", "Castro",
                            "1990/06/15", Gender.MALE, "Engineer")
            );
            assertTrue(ex.getMessage().contains("dd-MM-yyyy"));
        }

        @Test
        @DisplayName("should throw InvalidEmployeeFieldException when birth date is in the future")
        void shouldThrowWhenBirthDateIsInTheFuture() {
            // Arrange
            String futureDate = LocalDate.now().plusDays(1).format(FORMATTER);

            // Act & Assert
            InvalidEmployeeFieldException ex = assertThrows(
                    InvalidEmployeeFieldException.class,
                    () -> new Employee("Jorge", "Luis", "Martinez", "Castro",
                            futureDate, Gender.MALE, "Engineer")
            );
            assertTrue(ex.getMessage().contains("future"));
        }

        @Test
        @DisplayName("should throw InvalidEmployeeFieldException when birth date is blank")
        void shouldThrowWhenBirthDateIsBlank() {
            // Act & Assert
            InvalidEmployeeFieldException ex = assertThrows(
                    InvalidEmployeeFieldException.class,
                    () -> new Employee("Jorge", "Luis", "Martinez", "Castro",
                            "  ", Gender.MALE, "Engineer")
            );
            assertTrue(ex.getMessage().contains("Birth date"));
        }

        @Test
        @DisplayName("should accept today as birth date and compute age zero")
        void shouldAcceptTodayAsBirthDate() {
            // Arrange
            String today = LocalDate.now().format(FORMATTER);

            // Act
            Employee employee = new Employee("Jorge", "Luis", "Martinez", "Castro",
                    today, Gender.MALE, "Engineer");

            // Assert
            assertEquals(0, employee.getAge());
        }
    }

    @Nested
    @DisplayName("getFullName")
    class GetFullName {

        @Test
        @DisplayName("should return full name with middle name")
        void shouldReturnFullNameWithMiddleName() {
            // Arrange
            Employee employee = buildEmployee();

            // Act
            String fullName = employee.getFullName();

            // Assert
            assertEquals("Jorge Luis Martinez Castro", fullName);
        }

        @Test
        @DisplayName("should return full name without middle name when it is null")
        void shouldReturnFullNameWithoutMiddleNameWhenNull() {
            // Arrange
            Employee employee = new Employee(
                    "Ana", null, "Lopez", "Gomez",
                    "20-03-1995", Gender.FEMALE, "Designer"
            );

            // Act
            String fullName = employee.getFullName();

            // Assert
            assertEquals("Ana Lopez Gomez", fullName);
        }

        @Test
        @DisplayName("should return full name without middle name when it is blank")
        void shouldReturnFullNameWithoutMiddleNameWhenBlank() {
            // Arrange
            Employee employee = new Employee(
                    "Ana", "   ", "Lopez", "Gomez",
                    "20-03-1995", Gender.FEMALE, "Designer"
            );

            // Act
            String fullName = employee.getFullName();

            // Assert
            assertEquals("Ana Lopez Gomez", fullName);
        }
    }

    @Nested
    @DisplayName("promote")
    class Promote {

        @Test
        @DisplayName("should update position when promoted")
        void shouldUpdatePosition() {
            // Arrange
            Employee employee = buildEmployee();

            // Act
            Employee promoted = employee.promote("Tech Lead");

            // Assert
            assertEquals("Tech Lead", promoted.getPosition());
        }

        @Test
        @DisplayName("should throw InvalidEmployeeFieldException when new position is blank")
        void shouldThrowWhenNewPositionIsBlank() {
            // Arrange
            Employee employee = buildEmployee();

            // Act & Assert
            InvalidEmployeeFieldException ex = assertThrows(
                    InvalidEmployeeFieldException.class,
                    () -> employee.promote("")
            );
            assertTrue(ex.getMessage().contains("New position"));
        }

        @Test
        @DisplayName("should throw InvalidEmployeeFieldException when new position equals current position")
        void shouldThrowWhenNewPositionEqualsCurrentPosition() {
            // Arrange
            Employee employee = buildEmployee();

            // Act & Assert
            InvalidEmployeeFieldException ex = assertThrows(
                    InvalidEmployeeFieldException.class,
                    () -> employee.promote("Software Engineer")
            );
            assertTrue(ex.getMessage().contains("already holds the position"));
        }

        @Test
        @DisplayName("should throw InvalidEmployeeStateException when promoting an inactive employee")
        void shouldThrowWhenPromotingInactiveEmployee() {
            // Arrange
            Employee employee = buildEmployee().deactivate();

            // Act & Assert
            InvalidEmployeeStateException ex = assertThrows(
                    InvalidEmployeeStateException.class,
                    () -> employee.promote("Tech Lead")
            );
            assertTrue(ex.getMessage().contains("inactive"));
        }
    }

    @Nested
    @DisplayName("deactivate")
    class Deactivate {

        @Test
        @DisplayName("should deactivate an active employee")
        void shouldDeactivateActiveEmployee() {
            // Arrange
            Employee employee = buildEmployee();

            // Act
            Employee deactivated = employee.deactivate();

            // Assert
            assertFalse(deactivated.isActive());
        }

        @Test
        @DisplayName("should throw InvalidEmployeeStateException when already inactive")
        void shouldThrowWhenAlreadyInactive() {
            // Arrange
            Employee employee = buildEmployee().deactivate();

            // Act & Assert
            InvalidEmployeeStateException ex = assertThrows(
                    InvalidEmployeeStateException.class,
                    employee::deactivate
            );
            assertTrue(ex.getMessage().contains("already inactive"));
        }
    }

    @Nested
    @DisplayName("activate")
    class Activate {

        @Test
        @DisplayName("should activate an inactive employee")
        void shouldActivateInactiveEmployee() {
            // Arrange
            Employee employee = buildEmployee().deactivate();

            // Act
            Employee activated = employee.activate();

            // Assert
            assertTrue(activated.isActive());
        }

        @Test
        @DisplayName("should throw InvalidEmployeeStateException when already active")
        void shouldThrowWhenAlreadyActive() {
            // Arrange
            Employee employee = buildEmployee();

            // Act & Assert
            InvalidEmployeeStateException ex = assertThrows(
                    InvalidEmployeeStateException.class,
                    employee::activate
            );
            assertTrue(ex.getMessage().contains("already active"));
        }
    }

    @Nested
    @DisplayName("updateBirthDate")
    class UpdateBirthDate {

        @Test
        @DisplayName("should update birth date and recalculate age")
        void shouldUpdateBirthDateAndRecalculateAge() {
            // Arrange
            Employee employee = buildEmployee();
            String newBirthDate = "01-01-2000";
            LocalDate expectedDate = LocalDate.parse(newBirthDate, FORMATTER);
            int expectedAge = Period.between(expectedDate, LocalDate.now()).getYears();

            // Act
            Employee updated = employee.updateBirthDate(newBirthDate);

            // Assert
            assertAll(
                    () -> assertEquals(expectedDate, updated.getBirthDate()),
                    () -> assertEquals(expectedAge, updated.getAge()),
                    () -> assertEquals(newBirthDate, updated.getBirthDateFormatted())
            );
        }

        @Test
        @DisplayName("should throw InvalidEmployeeFieldException when new birth date format is invalid")
        void shouldThrowWhenNewBirthDateFormatIsInvalid() {
            // Arrange
            Employee employee = buildEmployee();

            // Act & Assert
            InvalidEmployeeFieldException ex = assertThrows(
                    InvalidEmployeeFieldException.class,
                    () -> employee.updateBirthDate("2000-01-01")
            );
            assertTrue(ex.getMessage().contains("dd-MM-yyyy"));
        }

        @Test
        @DisplayName("should throw InvalidEmployeeFieldException when new birth date is in the future")
        void shouldThrowWhenNewBirthDateIsInTheFuture() {
            // Arrange
            Employee employee = buildEmployee();
            String futureDate = LocalDate.now().plusDays(1).format(FORMATTER);

            // Act & Assert
            InvalidEmployeeFieldException ex = assertThrows(
                    InvalidEmployeeFieldException.class,
                    () -> employee.updateBirthDate(futureDate)
            );
            assertTrue(ex.getMessage().contains("future"));
        }
    }

    @Nested
    @DisplayName("reconstitute")
    class Reconstitute {

        @Test
        @DisplayName("should reconstitute employee with all given values")
        void shouldReconstituteEmployee() {
            // Arrange
            UUID id = UUID.randomUUID();
            LocalDate birthDate = LocalDate.of(1990, 6, 15);
            Instant registrationDate = Instant.now();
            int age = Period.between(birthDate, LocalDate.now()).getYears();

            // Act
            Employee employee = Employee.reconstitute(
                    id, "Jorge", "Luis", "Martinez", "Castro",
                    age, Gender.MALE, birthDate, "Engineer", registrationDate, true
            );

            // Assert
            assertAll(
                    () -> assertEquals(id, employee.getId()),
                    () -> assertEquals("Jorge", employee.getFirstName()),
                    () -> assertEquals("Luis", employee.getMiddleName()),
                    () -> assertEquals("Martinez", employee.getPaternalLastName()),
                    () -> assertEquals("Castro", employee.getMaternalLastName()),
                    () -> assertEquals(age, employee.getAge()),
                    () -> assertEquals(Gender.MALE, employee.getGender()),
                    () -> assertEquals(birthDate, employee.getBirthDate()),
                    () -> assertEquals("Engineer", employee.getPosition()),
                    () -> assertEquals(registrationDate, employee.getRegistrationDate()),
                    () -> assertTrue(employee.isActive())
            );
        }

        @Test
        @DisplayName("should reconstitute inactive employee")
        void shouldReconstituteInactiveEmployee() {
            // Arrange
            UUID id = UUID.randomUUID();
            LocalDate birthDate = LocalDate.of(1994, 3, 20);
            int age = Period.between(birthDate, LocalDate.now()).getYears();

            // Act
            Employee employee = Employee.reconstitute(
                    id, "Ana", null, "Lopez", "Gomez",
                    age, Gender.FEMALE, birthDate, "Designer", Instant.now(), false
            );

            // Assert
            assertFalse(employee.isActive());
        }
    }

    @Nested
    @DisplayName("getBirthDateFormatted")
    class GetBirthDateFormatted {

        @Test
        @DisplayName("should return birth date with format dd-MM-yyyy")
        void shouldReturnFormattedBirthDate() {
            // Arrange
            Employee employee = buildEmployee();

            // Act
            String formatted = employee.getBirthDateFormatted();

            // Assert
            assertEquals(DEFAULT_BIRTH_DATE, formatted);
        }
    }
}
