package com.test.demo.dto.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.test.demo.util.constrant.EnumGender;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
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

}