package com.test.demo.controller;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.test.demo.dto.request.RequestLoginDTO;
import com.test.demo.dto.response.AuthResponse;
import com.test.demo.dto.response.UserAuthDTO;
import com.test.demo.entity.User;
import com.test.demo.service.TokenService;
import com.test.demo.service.UserService;
import com.test.demo.util.constrant.EnumGender;
import com.test.demo.util.exception.AppException;
import com.test.demo.entity.Role;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;


@RestController
@RequestMapping("/api/v1")
public class AuthController {
    @Value("${pcv.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenService tokenService;
    private final UserService userService;

    public AuthController(
            AuthenticationManagerBuilder authenticationManagerBuilder,
            TokenService tokenService,
            UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenService = tokenService;
        this.userService = userService;

    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody RequestLoginDTO loginlogin) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginlogin.getUsername(), loginlogin.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // lưu trữ thông tin người đăng nhập
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AuthResponse accessToken = new AuthResponse();
        accessToken.setAccessToken(this.tokenService.createAccessToken(authentication.getName()));
        String refreshToken = this.tokenService.createRefreshToken(authentication.getName());
        this.userService.updateRefreshToken(authentication.getName(), refreshToken);

        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                // .domain("example.com")
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(accessToken);
    }

    @GetMapping("/auth/account")
    public ResponseEntity<UserAuthDTO> getMyAcount() {
        String email = TokenService.getCurrentUserLogin().isPresent() ? TokenService.getCurrentUserLogin().get() : "";
        User user = this.userService.checkEmail(email).get();
        UserAuthDTO res = new UserAuthDTO(user.getId(), user.getName(), user.getEmail());
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @CookieValue("refreshToken") String refreshToken) throws AppException {
        Jwt jwt = this.tokenService.checkValidRefeshToken(refreshToken);
        String email = jwt.getSubject();
        Optional<User> user = this.userService.checkEmailAndRefreshToken(email, refreshToken);
        if (!user.isPresent()) {
            throw new AppException("Refesh Token false");
        }
        AuthResponse accessToken = new AuthResponse();
        accessToken.setAccessToken(this.tokenService.createAccessToken(email));

        String newRefreshToken = this.tokenService.createAccessToken(email);
        this.userService.updateRefreshToken(email, newRefreshToken);

        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                // .domain("example.com")
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(accessToken);

    }

    // @GetMapping("/auth/google")
    // public ResponseEntity<Map<String, String>> googleLogin() {
    //     Map<String, String> response = new HashMap<>();
    //     response.put("redirect", "/oauth2/authorization/google");
    //     return ResponseEntity.ok(response);
    // }

    // @PostMapping("/auth/google/success")
    // public ResponseEntity<AuthResponse> googleLoginSuccess(@RequestBody Map<String, String> payload) {
    //     String idTokenString = payload.get("credential");
    //     if (idTokenString == null) {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    //     }
    
    //     GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
    //             new NetHttpTransport(),
    //             JacksonFactory.getDefaultInstance()
    //     ).setAudience(Collections.singletonList("895110505394-8cgdapjh8tprl968tu7ekg6pauflca0e.apps.googleusercontent.com"))
    //      .build();
    
    //     try {
    //         GoogleIdToken idToken = verifier.verify(idTokenString);
    //         if (idToken != null) {
    //             Payload googlePayload = idToken.getPayload();
    //             String email = googlePayload.getEmail();
    //             String name = (String) googlePayload.get("name");
    
    //             Optional<User> userOpt = userService.checkEmail(email);
    //             User user = userOpt.orElseGet(() -> {
    //                 User newUser = new User();
    //                 newUser.setEmail(email);
    //                 newUser.setName(name);
    //                 newUser.setPassword(null);
    //                 newUser.setRole(new Role(2L));
    //                 newUser.setCreatedBy(email);
    //                 newUser.setUpdatedBy(email);
    //                 newUser.setAge(18);
    //                 newUser.setGender(EnumGender.KHAC);
    //                 newUser.setAddress("N/A");
    //                 return newUser;
    //             });
    
    //             user = userService.save(user); // Save user to DB
    //             String accessToken = tokenService.createAccessToken(email);
    //             String refreshToken = tokenService.createRefreshToken(email);
    //             user.setRefreshToken(refreshToken);
    //             userService.save(user); // Update user with refreshToken
    
    //             AuthResponse response = new AuthResponse();
    //             response.setAccessToken(accessToken);
    
    //             ResponseCookie cookie = ResponseCookie
    //                     .from("refreshToken", refreshToken)
    //                     .httpOnly(true)
    //                     .secure(true)
    //                     .path("/")
    //                     .maxAge(refreshTokenExpiration)
    //                     .build();
    
    //             return ResponseEntity.ok()
    //                     .header(HttpHeaders.SET_COOKIE, cookie.toString())
    //                     .body(response);
    //         } else {
    //             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    //     }
    // }
    // @GetMapping("/auth/google/failure")
    // public ResponseEntity<Map<String, String>> googleLoginFailure() {
    //     Map<String, String> response = new HashMap<>();
    //     response.put("error", "Google login failed");
    //     return ResponseEntity.status(401).body(response);
    // }
}
