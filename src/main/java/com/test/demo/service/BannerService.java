package com.test.demo.service;

import java.util.List;

import com.test.demo.dto.BannerDTO;

public interface BannerService {
    List<BannerDTO> getAllBanners();
    BannerDTO getBannerById(Long id);
    BannerDTO createBanner(BannerDTO bannerDTO);
    BannerDTO updateBanner(Long id, BannerDTO bannerDTO);
    void deleteBanner(Long id);
}