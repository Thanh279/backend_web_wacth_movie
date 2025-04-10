package com.test.demo.service.serviceimpl;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.demo.dto.BannerDTO;
import com.test.demo.entity.Animation;
import com.test.demo.entity.Banner;
import com.test.demo.reponsitory.AnimationRepository;
import com.test.demo.reponsitory.BannerRepository;
import com.test.demo.service.BannerService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private AnimationRepository animationRepository; 

    @Override
    public List<BannerDTO> getAllBanners() {
        return bannerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BannerDTO getBannerById(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banner not found with id: " + id));
        return convertToDTO(banner);
    }

    @Override
    public BannerDTO createBanner(BannerDTO bannerDTO) {
        Banner banner = new Banner();
        BeanUtils.copyProperties(bannerDTO, banner, "id", "animation"); // Loại trừ animation
        if (bannerDTO.getAnimationId() != null) {
            Animation animation = animationRepository.findById(bannerDTO.getAnimationId())
                    .orElseThrow(() -> new RuntimeException("Animation not found with id: " + bannerDTO.getAnimationId()));
            banner.setAnimation(animation);
        }
        Banner savedBanner = bannerRepository.save(banner);
        return convertToDTO(savedBanner);
    }

    @Override
    public BannerDTO updateBanner(Long id, BannerDTO bannerDTO) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banner not found with id: " + id));
        BeanUtils.copyProperties(bannerDTO, banner, "id", "createdAt", "animation"); // Loại trừ animation
        if (bannerDTO.getAnimationId() != null) {
            Animation animation = animationRepository.findById(bannerDTO.getAnimationId())
                    .orElseThrow(() -> new RuntimeException("Animation not found with id: " + bannerDTO.getAnimationId()));
            banner.setAnimation(animation);
        } else {
            banner.setAnimation(null); // Nếu animationId là null, xóa liên kết
        }
        Banner updatedBanner = bannerRepository.save(banner);
        return convertToDTO(updatedBanner);
    }

    @Override
    public void deleteBanner(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banner not found with id: " + id));
        bannerRepository.delete(banner);
    }

    private BannerDTO convertToDTO(Banner banner) {
        BannerDTO bannerDTO = new BannerDTO();
        BeanUtils.copyProperties(banner, bannerDTO);
        if (banner.getAnimation() != null) {
            bannerDTO.setAnimationId(banner.getAnimation().getId());
        }
        return bannerDTO;
    }
}