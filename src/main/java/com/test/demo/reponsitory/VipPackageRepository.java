package com.test.demo.reponsitory;

import com.test.demo.entity.VipPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VipPackageRepository extends JpaRepository<VipPackage, Long> {
}