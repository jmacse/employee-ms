package com.jmacse.ms.employee.infrastructure.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class DatabaseHealthCheck implements HealthCheck {

    @Inject
    EntityManager entityManager;

    private static final String HEALTH_CHECK_NAME = "Database connection";
    private static final String ORACLE_PING_QUERY = "SELECT 1 FROM DUAL";

    @Override
    public HealthCheckResponse call() {
        try {
            entityManager
                    .createNativeQuery(ORACLE_PING_QUERY)
                    .getSingleResult();

            return HealthCheckResponse
                    .named(HEALTH_CHECK_NAME)
                    .up()
                    .withData("database", "Oracle")
                    .withData("query", ORACLE_PING_QUERY)
                    .build();

        } catch (Exception e) {
            return HealthCheckResponse
                    .named(HEALTH_CHECK_NAME)
                    .down()
                    .withData("database", "Oracle")
                    .withData("error", e.getMessage())
                    .build();
        }
    }
}

