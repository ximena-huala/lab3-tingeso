package com.example.demo.repositories;

import com.example.demo.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByEmail(String email);
    Optional<CustomerEntity> findByRut(String rut);
    List<CustomerEntity> findByRutContaining(String rut);
    Boolean existsByRut(String rut);
    Boolean existsByEmail(String email);
}
