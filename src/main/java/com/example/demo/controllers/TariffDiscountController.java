package com.example.demo.controllers;

import com.example.demo.entities.TariffDiscountEntity;
import com.example.demo.services.TariffDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tariff-discounts")
@CrossOrigin(origins = "*") // Permite peticiones desde el frontend
public class TariffDiscountController {
    @Autowired
    private TariffDiscountService service;

    @GetMapping
    public ResponseEntity<List<TariffDiscountEntity>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<TariffDiscountEntity> create(@RequestBody TariffDiscountEntity entity) {
        return ResponseEntity.ok(service.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TariffDiscountEntity> update(@PathVariable Long id, @RequestBody TariffDiscountEntity entity) {
        Optional<TariffDiscountEntity> existing = service.getById(id);
        if (existing.isPresent()) {
            entity.setId(id);
            return ResponseEntity.ok(service.save(entity));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok("Deleted successfully");
    }
}
