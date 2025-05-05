package com.test.demo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.test.demo.service.TokenService;
import com.test.demo.util.constrant.EnumGender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

@Entity
@Table(name = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Thông tin người dùng")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID người dùng")
    private long id;

    @Schema(description = "Tên người dùng", example = "Nguyen Van A")
    private String name;

    @NotBlank(message = "Không được để trống email")
    @Schema(description = "Email người dùng", example = "user@gmail.com")
    private String email;

    @NotBlank(message = "Không được để trống password")
    @Schema(description = "Mật khẩu người dùng (được mã hóa)", example = "encoded_password")
    private String password;

    @Column(nullable = false)
    @Schema(description = "Tuổi người dùng", example = "21")
    private int age;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Giới tính người dùng", example = "NAM")
    private EnumGender gender;

    @Schema(description = "Địa chỉ người dùng", example = "259/12 Thủ Đức,TP.Hồ Chí Minh")
    private String address;

    @Column(columnDefinition = "MEDIUMTEXT")
    @Schema(description = "Refresh token", example = "jwt_refresh_token")
    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @Schema(description = "Vai trò của người dùng")
    private Role role;

    @Schema(description = "Thời gian tạo", example = "2025-05-01T11:07:48.306Z")
    private Instant createdAt;

    @Schema(description = "Thời gian cập nhật", example = "2025-05-01T11:07:48.306Z")
    private Instant updatedAt;

    @Schema(description = "Người tạo", example = "USER")
    private String createdBy;

    @Schema(description = "Người cập nhật", example = "admin")
    private String updatedBy;

    @Column(nullable = false)
    @Schema(description = "Trạng thái VIP của người dùng", example = "false")
    private boolean isVip = false;

    @Schema(description = "Ngày hết hạn VIP", example = "2025-06-01T00:00:00Z")
    private Instant vipExpiryDate;

    @Column(nullable = false)
    @Schema(description = "Số dư tiền ảo (coin)", example = "0")
    private long walletBalance = 0L; 

    @PrePersist
    public void beforeCreate() {
        this.createdBy = TokenService.getCurrentUserLogin().isPresent() ? TokenService.getCurrentUserLogin().get() : "";
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void beforeUpdate() {
        this.updatedBy = TokenService.getCurrentUserLogin().isPresent() ? TokenService.getCurrentUserLogin().get() : "";
        this.updatedAt = Instant.now();
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public EnumGender getGender() {
        return gender;
    }

    public void setGender(EnumGender gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public Instant getVipExpiryDate() {
        return vipExpiryDate;
    }

    public void setVipExpiryDate(Instant vipExpiryDate) {
        this.vipExpiryDate = vipExpiryDate;
    }

    public long getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(long walletBalance) {
        this.walletBalance = walletBalance;
    }
}