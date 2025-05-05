package com.test.demo.service;

import com.test.demo.entity.VipPackage;
import com.test.demo.reponsitory.VipPackageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VipPackageService {
    private final VipPackageRepository vipPackageRepository;

    public VipPackageService(VipPackageRepository vipPackageRepository) {
        this.vipPackageRepository = vipPackageRepository;
    }

    public List<VipPackage> getAllPackages() {
        return vipPackageRepository.findAll();
    }

    public VipPackage getPackageById(Long id) {
        return vipPackageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy gói VIP với ID: " + id));
    }
}