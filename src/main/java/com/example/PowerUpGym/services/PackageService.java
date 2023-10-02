package com.example.PowerUpGym.services;

import com.example.PowerUpGym.entity.packagesGym.PackagesEntity;
import com.example.PowerUpGym.repositories.PackagesEntityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackageService {

    private final PackagesEntityRepository packagesEntityRepository;

    public PackageService(PackagesEntityRepository packagesEntityRepository) {
        this.packagesEntityRepository = packagesEntityRepository;
    }

    public List<PackagesEntity> getAllPackages() {
        return packagesEntityRepository.findAll();
    }

    public PackagesEntity addPackage(PackagesEntity packageEntity) {
        return packagesEntityRepository.save(packageEntity);
    }

    public PackagesEntity getPackageById(Long id) {
        return packagesEntityRepository.findById(id).orElse(null);
    }

}
