package com.example.demo.services;

import com.example.demo.entities.TariffDiscountEntity;
import com.example.demo.repositories.TariffDiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TariffDiscountService {
    @Autowired
    private TariffDiscountRepository repository;

    public List<TariffDiscountEntity> getAll() {
        return repository.findAll();
    }

    public TariffDiscountEntity save(TariffDiscountEntity entity) {
        return repository.save(entity);
    }

    public Optional<TariffDiscountEntity> getById(Long id) {
        return repository.findById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
