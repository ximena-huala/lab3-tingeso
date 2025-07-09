package com.example.demo.repositories;

import com.example.demo.entities.KartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface KartRepository extends JpaRepository<KartEntity, Long> {
    Optional<KartEntity> findByCode(String code);

    List<KartEntity> findByStatus(String status);

    boolean existsByCode(String code);
}
