package com.test.demo.dto.request;

import com.test.demo.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "Yêu cầu tạo hoặc cập nhật người dùng với thông tin JSON và file ảnh")
public class MultiPartRequest {
    @Schema(description = "Thông tin người dùng (JSON)", implementation = User.class)
    private User user;

    @Schema(description = "File ảnh đại diện (jpg, png, v.v.)", type = "string", format = "binary")
    private MultipartFile avatar;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MultipartFile getAvatar() {
        return avatar;
    }

    public void setAvatar(MultipartFile avatar) {
        this.avatar = avatar;
    }
}