package com.test.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.test.demo.entity.User;
import com.test.demo.service.EmailService;
import com.test.demo.service.TokenService;
import com.test.demo.service.UserService;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class EmailController {
    private final EmailService emailService;
    private final UserService userService;

    public EmailController(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    @GetMapping("/email")
    public String sendSimpleEmail() {
        Optional<String> emailOpt = TokenService.getCurrentUserLogin();
        if (emailOpt.isEmpty()) {
            throw new RuntimeException("No authenticated user found");
        }
        String email = emailOpt.get();
        Optional<User> userOpt = userService.checkEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found for email: " + email);
        }
        User user = userOpt.get();
        this.emailService.sendEmailFromTemplateSync(
                user.getEmail(),
                "Thư cảm ơn",
                "sendemailbs",
                user.getName());
        return "ok";
    }
}