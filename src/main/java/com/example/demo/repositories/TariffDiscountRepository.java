package com.example.demo.repositories;

import com.example.demo.entities.TariffDiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TariffDiscountRepository extends JpaRepository<TariffDiscountEntity, Long> {
}
