package com.test.demo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "vip_packages")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Thông tin gói VIP")
public class VipPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID gói VIP")
    private long id;

    @Schema(description = "Tên gói VIP", example = "Gói Ngày")
    private String name;

    @Schema(description = "Giá gói (VND)", example = "20000")
    private long price;

    @Schema(description = "Thời hạn gói (số ngày)", example = "1")
    private int durationDays;

    @Schema(description = "Mô tả gói", example = "Gói VIP 1 ngày, bỏ qua quảng cáo")
    private String description;

    // Getters and setters
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

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}