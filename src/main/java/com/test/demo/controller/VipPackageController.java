package com.test.demo.controller;

import com.test.demo.entity.VipPackage;
import com.test.demo.service.VipPackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class VipPackageController {
    private final VipPackageService vipPackageService;

    public VipPackageController(VipPackageService vipPackageService) {
        this.vipPackageService = vipPackageService;
    }

    @GetMapping("/vip-packages")
    public ResponseEntity<List<VipPackage>> getAllPackages() {
        return ResponseEntity.ok(vipPackageService.getAllPackages());
    }

    @GetMapping("/vip-packages/{id}")
    public ResponseEntity<VipPackage> getPackageById(@PathVariable Long id) {
        return ResponseEntity.ok(vipPackageService.getPackageById(id));
    }
}