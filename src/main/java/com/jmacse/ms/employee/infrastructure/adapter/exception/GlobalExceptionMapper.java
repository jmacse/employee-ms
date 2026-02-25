package com.jmacse.ms.employee.infrastructure.adapter.exception;

import com.jmacse.ms.employee.domain.exception.EmployeeDomainException;
import com.jmacse.ms.employee.domain.exception.EmployeeNotFoundException;
import com.jmacse.ms.employee.domain.exception.InvalidEmployeeFieldException;
import com.jmacse.ms.employee.domain.exception.InvalidEmployeeStateException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<RuntimeException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionMapper.class);

    private static final Map<Class<? extends EmployeeDomainException>, Response.Status>
                EXCEPTION_STATUS_MAP = Map.of(
            EmployeeNotFoundException.class,     Response.Status.NOT_FOUND,
            InvalidEmployeeFieldException.class, Response.Status.BAD_REQUEST,
            InvalidEmployeeStateException.class, Response.Status.CONFLICT
    );

    @Override
    public Response toResponse(RuntimeException exception) {
        if (exception instanceof EmployeeDomainException) {
            Response.Status status = EXCEPTION_STATUS_MAP.getOrDefault(
                    exception.getClass(),
                    Response.Status.INTERNAL_SERVER_ERROR
            );
            LOGGER.error(exception.getMessage(), exception);
            return buildResponse(status, exception.getMessage());
        }

        LOGGER.error("An unexpected error occurred", exception);
        return buildResponse(Response.Status.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    private Response buildResponse(Response.Status status, String message) {
        return Response
                .status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse(status.getStatusCode(), message))
                .build();
    }

    record ErrorResponse(int status, String message) {}
}

