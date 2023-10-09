package com.example.PowerUpGym.services.packagee;

import com.example.PowerUpGym.entity.packagesGym.PackagesEntity;
import com.example.PowerUpGym.repositories.PackagesEntityRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PackageServiceImp implements PackageService{

    private final PackagesEntityRepository packagesEntityRepository;

    public PackageServiceImp(PackagesEntityRepository packagesEntityRepository) {
        this.packagesEntityRepository = packagesEntityRepository;
    }
    @Override
    public List<PackagesEntity> getAllPackages() {
        return packagesEntityRepository.findAll();
    }

    @Override
    public PackagesEntity addPackage(PackagesEntity packageEntity) {
        packagesEntityRepository.save(packageEntity);
        return packageEntity;
    }

    @Override
    public PackagesEntity getPackageById(Long id) {
        return packagesEntityRepository.findById(id).orElseThrow(()->new RuntimeException("Package not found for this ID : " + id));
    }

}
