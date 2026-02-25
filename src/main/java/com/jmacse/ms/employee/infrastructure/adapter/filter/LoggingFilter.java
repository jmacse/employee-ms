package com.jmacse.ms.employee.infrastructure.adapter.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.stream.Collectors;

@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

    private static final String REQUEST_ID_PROPERTY = "requestId";
    private static final String START_TIME_PROPERTY = "startTime";

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String requestId = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();

        requestContext.setProperty(REQUEST_ID_PROPERTY, requestId);
        requestContext.setProperty(START_TIME_PROPERTY, startTime);

        LOGGER.info("[{}] --> {} {} | headers: {}",
                requestId,
                requestContext.getMethod(),
                requestContext.getUriInfo().getRequestUri(),
                formatHeaders(requestContext.getHeaders()));
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        String requestId = (String) requestContext.getProperty(REQUEST_ID_PROPERTY);
        long startTime = (long) requestContext.getProperty(START_TIME_PROPERTY);
        long duration = System.currentTimeMillis() - startTime;

        LOGGER.info("[{}] <-- {} {} | status: {} | duration: {}ms | headers: {}",
                requestId,
                requestContext.getMethod(),
                requestContext.getUriInfo().getRequestUri(),
                responseContext.getStatus(),
                duration,
                formatHeaders(responseContext.getHeaders()));
    }

    private String formatHeaders(MultivaluedMap<String, ?> headers) {
        return headers.entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(", ", "{", "}"));
    }
}





