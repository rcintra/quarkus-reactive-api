package com.rcintra;

import java.util.Optional;

import io.quarkus.Generated;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Cacheable
public class Employee extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    
    @NotEmpty
    public String name;

    @NotEmpty
    @Email
    public String email;

    public Employee() {
    }

    public Employee(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public static Uni<Employee> findByEmployeeId(Long id) {
        return findById(id);
    }

    public static Optional<Employee> findByIdOptional(Long id) {
        Uni<Employee> employee = findById(id);
        return Optional.ofNullable(employee == null ? null : employee.await().indefinitely());
    }

    public static Uni<Employee> updateEmployee(Long id, Employee employee) {
        return Panache
            .withTransaction(() -> findByEmployeeId(id)
                .onItem().ifNotNull()
                .transform(entity -> {
                    entity.name = employee.name;
                    entity.email = employee.email;
                    return entity;
                }))
                .onFailure().recoverWithNull();
    }
}

