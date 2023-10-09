package com.example.PowerUpGym.services.packagee;

import com.example.PowerUpGym.entity.packagesGym.PackagesEntity;
import java.util.List;

public interface PackageService {
    List<PackagesEntity> getAllPackages();
    PackagesEntity addPackage(PackagesEntity packageEntity);
    PackagesEntity getPackageById(Long id);
}
