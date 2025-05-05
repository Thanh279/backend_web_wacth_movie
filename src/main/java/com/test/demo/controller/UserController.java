package com.test.demo.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.demo.dto.response.UserDTO;
import com.test.demo.dto.response.filter.FilterResponse;
import com.test.demo.entity.User;
import com.test.demo.service.UserService;
import com.test.demo.util.exception.AppException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody User param) throws AppException {
        Optional<User> checkEmail = this.userService.checkEmail(param.getEmail());
        if (checkEmail.isPresent()) {
            throw new AppException("Email đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.createUser(param));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable("id") long id) throws AppException {
        Optional<User> checkId = this.userService.checkId(id);
        if (!checkId.isPresent()) {
            throw new AppException("Không tìm thấy user tồn tại " + id + " này.");
        }
        UserDTO res = this.userService.mapperUserToUserDTO(checkId.get());
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/users")
    public ResponseEntity<FilterResponse> getAllUsers(Pageable pageable) {
        // Default to sorting by id if unsorted
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").ascending());
        }
        return ResponseEntity.ok().body(this.userService.filterUser(null, pageable));
    }

    @PostMapping("/users/{id}/upgrade-vip")
    public ResponseEntity<UserDTO> upgradeToVip(
            @PathVariable("id") Long userId,
            @RequestParam Long packageId,
            @RequestParam Long durationDays,
            @RequestParam(defaultValue = "false") boolean useWallet) throws AppException {
        Optional<User> user = this.userService.checkId(userId);
        if (!user.isPresent()) {
            throw new AppException("Không tìm thấy user với id " + userId);
        }

        if (useWallet) {
            return ResponseEntity.ok(this.userService.upgradeToVipWithWallet(userId, packageId, durationDays));
        } else {
            return ResponseEntity.ok(this.userService.upgradeToVip(userId, packageId, durationDays));
        }
    }

    @GetMapping("/users/{id}/wallet")
    public ResponseEntity<Map<String, Long>> getUserWallet(@PathVariable("id") Long id) throws AppException {
        Optional<User> user = this.userService.checkId(id);
        if (!user.isPresent()) {
            throw new AppException("Không tìm thấy user với id " + id);
        }
        Map<String, Long> wallet = new HashMap<>();
        wallet.put("balance", this.userService.getWalletBalance(id));
        return ResponseEntity.ok(wallet);
    }

    @PostMapping("/users/{id}/wallet/deposit")
    public ResponseEntity<UserDTO> depositMoney(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Long> body) throws AppException {
        Optional<User> user = this.userService.checkId(id);
        if (!user.isPresent()) {
            throw new AppException("Không tìm thấy user với id " + id);
        }
        Long amount = body.get("amount");
        return ResponseEntity.ok(this.userService.depositMoney(id, amount));
    }
}