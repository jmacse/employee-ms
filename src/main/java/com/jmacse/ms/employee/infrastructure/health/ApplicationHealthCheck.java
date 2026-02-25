package com.jmacse.ms.employee.infrastructure.health;

import com.jmacse.ms.employee.domain.repository.EmployeeStatisticsRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
@ApplicationScoped
public class ApplicationHealthCheck implements HealthCheck {

    @Inject
    EmployeeStatisticsRepository employeeStatisticsRepository;

    private static final String HEALTH_CHECK_NAME = "Employee Microservice";

    @Override
    public HealthCheckResponse call() {
        try {
            long totalEmployees = employeeStatisticsRepository.count();

            return HealthCheckResponse
                    .named(HEALTH_CHECK_NAME)
                    .up()
                    .withData("status", "running")
                    .withData("totalEmployees", totalEmployees)
                    .build();

        } catch (Exception e) {
            return HealthCheckResponse
                    .named(HEALTH_CHECK_NAME)
                    .down()
                    .withData("status", "unavailable")
                    .withData("error", e.getMessage())
                    .build();
        }
    }
}

