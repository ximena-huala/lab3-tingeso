package com.example.demo.services;

import com.example.demo.entities.KartEntity;
import com.example.demo.repositories.KartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KartServices {
    @Autowired
    private KartRepository kartRepository;

    public KartEntity saveKart(KartEntity kart) {
        return kartRepository.save(kart);
    }

    public List<KartEntity> getAllKarts() {
        return kartRepository.findAll();
    }

    public Optional<KartEntity> getKartById(Long id) {
        return kartRepository.findById(id);
    }

    public Optional<KartEntity> getKartByCode(String code) {
        return kartRepository.findByCode(code);
    }

    public boolean existsByCode(String code) {
        return kartRepository.existsByCode(code);
    }

    public List<KartEntity> getKartsByStatus(String status) {
        return kartRepository.findByStatus(status);
    }

    public KartEntity updateKart(KartEntity kart) {
        return kartRepository.save(kart);
    }

    public boolean deleteKart(Long id) throws Exception {
        try {
            kartRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
