package com.test.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.test.demo.dto.response.UserDTO;
import com.test.demo.dto.response.filter.FilterResponse;
import com.test.demo.dto.response.filter.Meta;
import com.test.demo.entity.User;
import com.test.demo.reponsitory.UseRepository;

@Service
public class UserService {
    private final UseRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UseRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO mapperUserToUserDTO(User param) {
        UserDTO res = new UserDTO(
                param.getId(),
                param.getName(),
                param.getEmail(),
                param.getAge(),
                param.getGender(),
                param.getAddress(),
                param.getCreatedAt(),
                param.getUpdatedAt(),
                param.getCreatedBy(),
                param.getUpdatedBy());
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
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);

        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
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
}