package com.test.demo.dto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BannerDTO {
    private Long id;
    private String title;
    private String imageUrl;
    private String link;
    private String description;
    private LocalDateTime createdAt;
    private Long animationId; 
}