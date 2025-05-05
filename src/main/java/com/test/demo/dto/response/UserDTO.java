package com.test.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.test.demo.util.constrant.EnumGender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private long id;
    private String name;
    private String email;
    private int age;
    private EnumGender gender;
    private String address;
    private String avatarUrl;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private boolean isVip;
    private Instant vipExpiryDate;
    private long walletBalance; 
}