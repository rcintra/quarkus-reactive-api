package com.rcintra;

import java.util.List;

import org.jboss.resteasy.reactive.RestPath;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/employees")
public class EmployeeResource {
    

    @GET
    public Uni<List<Employee>> getEmployees() {
        return Employee.listAll();
    }

    @GET
    @Path("/{id}")
    public Uni<Response> findByid(@RestPath Long id) {
        return Employee.findById(id)
                .map(
                    employee ->  employee != null ? Response.ok(employee).build()
                    : Response.ok().status(Status.NOT_FOUND).build());
    }

    @PUT
    @Path("/{id}")
    public Uni<Response> update(@RestPath Long id, @Valid Employee employee) {
        return Employee.updateEmployee(id, employee)
                .onItem().ifNotNull().transform(entity -> Response.ok(entity).build())
                .onItem().ifNull().continueWith(Response.ok().status(Status.NOT_FOUND).build());                
    }

    @POST
    public Uni<Response> create(@Valid Employee employee) {
        return Panache.withTransaction(employee::persist)
                .replaceWith(Response.ok(employee).status(Status.CREATED).build());
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> delete(@RestPath Long id) {
        return Panache.withTransaction(() -> Employee.deleteById(id)
                .map(deleted -> deleted ? Response.ok().status(Status.NO_CONTENT).build()
                                        : Response.ok().status(Status.NOT_FOUND).build()));
    }
}
