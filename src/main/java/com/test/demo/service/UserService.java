package com.test.demo.service;

import com.test.demo.dto.response.UserDTO;
import com.test.demo.dto.response.filter.FilterResponse;
import com.test.demo.dto.response.filter.Meta;
import com.test.demo.entity.User;
import com.test.demo.entity.UserImage;
import com.test.demo.entity.VipPackage;
import com.test.demo.reponsitory.UseRepository;
import com.test.demo.reponsitory.UserImageRepository;
import com.test.demo.reponsitory.VipPackageRepository;
import com.test.demo.util.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UseRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private UserImageRepository userImageRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private VipPackageRepository vipPackageRepository;

    public UserService(UseRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public UserDTO mapperUserToUserDTO(User param) {
        UserDTO res = new UserDTO();
        res.setId(param.getId());
        res.setName(param.getName());
        res.setEmail(param.getEmail());
        res.setAge(param.getAge());
        res.setGender(param.getGender());
        res.setAddress(param.getAddress());
        res.setCreatedAt(param.getCreatedAt());
        res.setUpdatedAt(param.getUpdatedAt());
        res.setCreatedBy(param.getCreatedBy());
        res.setUpdatedBy(param.getUpdatedBy());
        res.setVip(param.isVip());
        res.setVipExpiryDate(param.getVipExpiryDate());
        Optional<UserImage> userImage = userImageRepository.findByUserId(param.getId());
        res.setAvatarUrl(userImage.map(UserImage::getImageUrl).orElse(null));
        res.setWalletBalance(param.getWalletBalance());
        return res;
    }

    public UserDTO createUser(User param) {
        if (param.getPassword() != null) {
            param.setPassword(passwordEncoder.encode(param.getPassword()));
        }
        User user = this.userRepository.save(param);
        return this.mapperUserToUserDTO(user);
    }

    public void updateRefreshToken(String email, String refreshToken) {
        Optional<User> userOpt = this.checkEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setRefreshToken(refreshToken);
            this.userRepository.save(user);
        } else {
            throw new RuntimeException("User not found for email: " + email);
        }
    }

    public FilterResponse filterUser(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser;
        if (spec == null) {
            pageUser = this.userRepository.findAll(pageable);
        } else {
            pageUser = this.userRepository.findAll(spec, pageable);
        }

        Meta meta = new Meta();
        meta.setPage(pageUser.getNumber() + 1);
        meta.setPageSize(pageUser.getSize());
        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());

        FilterResponse res = new FilterResponse();
        res.setMeta(meta);
        List<UserDTO> listUser = pageUser.getContent().stream()
                .map(this::mapperUserToUserDTO)
                .collect(Collectors.toList());
        res.setObject(listUser);
        return res;
    }

    public Optional<User> checkEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public Optional<User> checkId(long id) {
        return this.userRepository.findById(id);
    }

    public Optional<User> checkEmailAndRefreshToken(String email, String refreshToken) {
        return this.userRepository.findByEmailAndRefreshToken(email, refreshToken);
    }

    public User save(User user) {
        return this.userRepository.save(user);
    }

    public UserDTO updateUser(Long id, User updatedUser, MultipartFile avatarFile) throws AppException {
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isEmpty()) {
            throw new AppException("Không tìm thấy user với ID: " + id);
        }

        User existingUser = existingUserOpt.get();
        if (updatedUser.getName() != null)
            existingUser.setName(updatedUser.getName());
        if (updatedUser.getEmail() != null) {
            Optional<User> emailCheck = userRepository.findByEmail(updatedUser.getEmail());
            if (emailCheck.isPresent() && emailCheck.get().getId() != id) {
                throw new AppException("Email đã tồn tại");
            }
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getAge() != 0)
            existingUser.setAge(updatedUser.getAge());
        if (updatedUser.getGender() != null)
            existingUser.setGender(updatedUser.getGender());
        if (updatedUser.getAddress() != null)
            existingUser.setAddress(updatedUser.getAddress());

        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                if (!avatarFile.getContentType().startsWith("image/")) {
                    throw new AppException("File phải là hình ảnh (jpg, png, etc.)");
                }
                if (avatarFile.getSize() > 5 * 1024 * 1024) {
                    throw new AppException("File không được lớn hơn 5MB");
                }

                String avatarUrl = saveAvatarFile(avatarFile, id);
                Optional<UserImage> existingImageOpt = userImageRepository.findByUserId(id);
                UserImage userImage;
                if (existingImageOpt.isPresent()) {
                    userImage = existingImageOpt.get();
                    deleteOldAvatarFile(userImage.getImageUrl());
                    userImage.setImageUrl(avatarUrl);
                } else {
                    userImage = new UserImage();
                    userImage.setUser(existingUser);
                    userImage.setImageUrl(avatarUrl);
                }
                userImageRepository.save(userImage);
            } catch (IOException e) {
                throw new AppException("Lỗi khi lưu file ảnh: " + e.getMessage());
            }
        }

        userRepository.save(existingUser);
        return mapperUserToUserDTO(existingUser);
    }

    private String saveAvatarFile(MultipartFile file, Long userId) throws IOException {
        String uploadDir = "Uploads/avatars/";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : ".jpg";
        String newFilename = "user_" + userId + "_" + UUID.randomUUID() + fileExtension;
        Path filePath = Paths.get(uploadDir, newFilename);

        Files.write(filePath, file.getBytes());
        return "/Uploads/avatars/" + newFilename;
    }

    private void deleteOldAvatarFile(String oldAvatarUrl) {
        if (oldAvatarUrl != null && !oldAvatarUrl.isEmpty()) {
            try {
                Path oldFilePath = Paths.get(oldAvatarUrl);
                Files.deleteIfExists(oldFilePath);
            } catch (IOException e) {
                System.err.println("Lỗi khi xóa file ảnh cũ: " + e.getMessage());
            }
        }
    }

    public void changePassword(Long id, String currentPassword, String newPassword) throws AppException {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            throw new AppException("Không tìm thấy user với ID: " + id);
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new AppException("Mật khẩu hiện tại không đúng");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public UserDTO upgradeToVip(Long userId, Long packageId, Long durationDays) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new AppException("Không tìm thấy user với ID: " + userId);
        }

        User user = userOpt.get();
        Optional<VipPackage> vipPackageOpt = vipPackageRepository.findById(packageId);
        if (vipPackageOpt.isEmpty()) {
            throw new AppException("Không tìm thấy gói VIP với ID: " + packageId);
        }

        VipPackage vipPackage = vipPackageOpt.get();
        Instant now = Instant.now();
        Instant expiryDate = now.plusSeconds(durationDays * 24 * 60 * 60);
        user.setVip(true);
        user.setVipExpiryDate(expiryDate);
        userRepository.save(user);

        emailService.sendEmailFromTemplateSync(
            user.getEmail(),
            "Xác nhận nâng cấp VIP",
            "vip_confirmation",
            user.getName()
        );

        return mapperUserToUserDTO(user);
    }

    public UserDTO upgradeToVipWithWallet(Long userId, Long packageId, Long durationDays) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new AppException("Không tìm thấy user với ID: " + userId);
        }

        User user = userOpt.get();
        Optional<VipPackage> vipPackageOpt = vipPackageRepository.findById(packageId);
        if (vipPackageOpt.isEmpty()) {
            throw new AppException("Không tìm thấy gói VIP với ID: " + packageId);
        }

        VipPackage vipPackage = vipPackageOpt.get();
        if (user.getWalletBalance() < vipPackage.getPrice()) {
            throw new AppException("Số dư không đủ để thanh toán gói VIP");
        }

        user.setWalletBalance(user.getWalletBalance() - vipPackage.getPrice());
        Instant now = Instant.now();
        Instant expiryDate = now.plusSeconds(durationDays * 24 * 60 * 60);
        user.setVip(true);
        user.setVipExpiryDate(expiryDate);
        userRepository.save(user);

        emailService.sendEmailFromTemplateSync(
            user.getEmail(),
            "Xác nhận nâng cấp VIP bằng ví",
            "vip_confirmation",
            user.getName()
        );

        return mapperUserToUserDTO(user);
    }

    public long getWalletBalance(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new AppException("Không tìm thấy user với ID: " + userId);
        }
        return userOpt.get().getWalletBalance();
    }

    public UserDTO depositMoney(Long userId, long amount) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new AppException("Không tìm thấy user với ID: " + userId);
        }

        if (amount <= 0) {
            throw new AppException("Số tiền nạp phải lớn hơn 0");
        }

        User user = userOpt.get();
        user.setWalletBalance(user.getWalletBalance() + amount);
        userRepository.save(user);

        return mapperUserToUserDTO(user);
    }
}