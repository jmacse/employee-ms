package com.jmacse.ms.employee.infrastructure.persistence;

import com.jmacse.ms.employee.domain.model.Employee;
import com.jmacse.ms.employee.domain.repository.EmployeeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class EmployeeRepositoryAdapter implements EmployeeRepository {

    private static final String FIND_ALL_QUERY = "SELECT e FROM EmployeeEntity e";
    private static final String COUNT_QUERY = "SELECT COUNT(e) FROM EmployeeEntity e";
    private static final String SEARCH_BY_NAME_QUERY =
            "SELECT e FROM EmployeeEntity e WHERE " +
            "LOWER(e.firstName) LIKE :pattern OR " +
            "LOWER(e.middleName) LIKE :pattern OR " +
            "LOWER(e.paternalLastName) LIKE :pattern OR " +
            "LOWER(e.maternalLastName) LIKE :pattern";

    private final EntityManager entityManager;

    @Inject
    public EmployeeRepositoryAdapter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Employee> findAll() {
        return entityManager
                .createQuery(FIND_ALL_QUERY, EmployeeEntity.class)
                .getResultList()
                .stream()
                .map(EmployeeMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Employee> findById(UUID id) {
        EmployeeEntity entity = entityManager.find(EmployeeEntity.class, id);
        return Optional.ofNullable(entity).map(EmployeeMapper::toDomain);
    }

    @Override
    @Transactional
    public Employee save(Employee employee) {
        EmployeeEntity entity = EmployeeMapper.toEntity(employee);
        EmployeeEntity saved = entityManager.merge(entity);
        return EmployeeMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public List<Employee> saveAll(List<Employee> employees) {
        return employees.stream()
                .map(this::save)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        EmployeeEntity entity = entityManager.find(EmployeeEntity.class, id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    @Override
    public List<Employee> searchByName(String name) {
        String pattern = "%" + name.toLowerCase() + "%";
        TypedQuery<EmployeeEntity> query = entityManager.createQuery(SEARCH_BY_NAME_QUERY, EmployeeEntity.class);
        query.setParameter("pattern", pattern);
        return query.getResultList()
                .stream()
                .map(EmployeeMapper::toDomain)
                .toList();
    }

    @Override
    public long count() {
        return entityManager
                .createQuery(COUNT_QUERY, Long.class)
                .getSingleResult();
    }
}
