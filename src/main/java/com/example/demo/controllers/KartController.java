package com.example.demo.controllers;

import com.example.demo.entities.KartEntity;
import com.example.demo.services.KartServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/karts")
@CrossOrigin(origins = "*")

public class KartController {
    @Autowired
    private KartServices kartService;

    @PostMapping
    public ResponseEntity<KartEntity> saveKart(@RequestBody KartEntity kart) {
        return ResponseEntity.ok(kartService.saveKart(kart));
    }

    @GetMapping
    public ResponseEntity<List<KartEntity>> getAllKarts() {
        return ResponseEntity.ok(kartService.getAllKarts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<KartEntity> getKartById(@PathVariable Long id) {
        Optional<KartEntity> kart = kartService.getKartById(id);
        return kart.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<KartEntity> getKartByCode(@PathVariable String code) {
        Optional<KartEntity> kart = kartService.getKartByCode(code);
        return kart.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<KartEntity>> getKartsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(kartService.getKartsByStatus(status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<KartEntity> updateKart(@PathVariable Long id, @RequestBody KartEntity kart) {
        Optional<KartEntity> existing = kartService.getKartById(id);
        if (existing.isPresent()) {
            kart.setId(id);
            return ResponseEntity.ok(kartService.updateKart(kart));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteKart(@PathVariable Long id) {
        try {
            boolean deleted = kartService.deleteKart(id);
            if (deleted) {
                return ResponseEntity.ok("Kart deleted successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
