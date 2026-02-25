package com.jmacse.ms.employee.infrastructure.adapter;

import com.jmacse.ms.employee.application.dto.CreateEmployeeRequest;
import com.jmacse.ms.employee.application.dto.EmployeeResponse;
import com.jmacse.ms.employee.application.dto.UpdateEmployeeRequest;
import com.jmacse.ms.employee.application.usecase.EmployeeCommandUseCase;
import com.jmacse.ms.employee.application.usecase.EmployeeQueryUseCase;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@Path("/employees")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Employees", description = "Operations for employee management")
public class EmployeeResource {

    private final EmployeeQueryUseCase employeeQueryUseCase;
    private final EmployeeCommandUseCase employeeCommandUseCase;

    public EmployeeResource(EmployeeQueryUseCase employeeQueryUseCase,
                            EmployeeCommandUseCase employeeCommandUseCase) {
        this.employeeQueryUseCase = employeeQueryUseCase;
        this.employeeCommandUseCase = employeeCommandUseCase;
    }

    @GET
    @Operation(summary = "Get all employees", description = "Returns the full list of registered employees")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "List of employees retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = EmployeeResponse.class))),
            @APIResponse(responseCode = "500", description = "Unexpected server error")
    })
    public Response findAll() {
        List<EmployeeResponse> employees = employeeQueryUseCase.findAll();
        return Response.ok(employees).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get employee by ID", description = "Returns the detail of a single employee by their UUID")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Employee found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = EmployeeResponse.class))),
            @APIResponse(responseCode = "404", description = "Employee not found"),
            @APIResponse(responseCode = "500", description = "Unexpected server error")
    })
    public Response findById(
            @Parameter(description = "Employee UUID", required = true)
            @PathParam("id") UUID id) {
        EmployeeResponse employee = employeeQueryUseCase.findById(id);
        return Response.ok(employee).build();
    }

    @POST
    @Operation(summary = "Create employees", description = "Creates one or more employees in a single request")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Employees created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = EmployeeResponse.class))),
            @APIResponse(responseCode = "400", description = "Invalid field in request body"),
            @APIResponse(responseCode = "409", description = "Business rule conflict"),
            @APIResponse(responseCode = "500", description = "Unexpected server error")
    })
    public Response create(
            @RequestBody(description = "List of employees to create", required = true,
                    content = @Content(schema = @Schema(implementation = CreateEmployeeRequest.class)))
            List<CreateEmployeeRequest> requests) {
        List<EmployeeResponse> employees = employeeCommandUseCase.create(requests);
        return Response.status(Response.Status.CREATED).entity(employees).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update employee", description = "Updates all or some fields of an existing employee")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Employee updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = EmployeeResponse.class))),
            @APIResponse(responseCode = "400", description = "Invalid field in request body"),
            @APIResponse(responseCode = "404", description = "Employee not found"),
            @APIResponse(responseCode = "500", description = "Unexpected server error")
    })
    public Response update(
            @Parameter(description = "Employee UUID", required = true)
            @PathParam("id") UUID id,
            @RequestBody(description = "Fields to update", required = true,
                    content = @Content(schema = @Schema(implementation = UpdateEmployeeRequest.class)))
            UpdateEmployeeRequest request) {
        EmployeeResponse employee = employeeCommandUseCase.update(id, request);
        return Response.ok(employee).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete employee", description = "Deletes an employee by their UUID")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Employee deleted successfully"),
            @APIResponse(responseCode = "404", description = "Employee not found"),
            @APIResponse(responseCode = "500", description = "Unexpected server error")
    })
    public Response delete(
            @Parameter(description = "Employee UUID", required = true)
            @PathParam("id") UUID id) {
        employeeCommandUseCase.delete(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/search")
    @Operation(summary = "Search employees by name", description = "Returns employees whose first or last name partially matches the search term")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Search results retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = EmployeeResponse.class))),
            @APIResponse(responseCode = "500", description = "Unexpected server error")
    })
    public Response searchByName(
            @Parameter(description = "Partial name to search for", required = true, example = "Jorge")
            @QueryParam("name") String name) {
        List<EmployeeResponse> employees = employeeQueryUseCase.searchByName(name);
        return Response.ok(employees).build();
    }
}

