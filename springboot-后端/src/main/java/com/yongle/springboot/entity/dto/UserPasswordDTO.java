package com.yongle.springboot.entity.dto;

import lombok.Data;

@Data
public class UserPasswordDTO {
    private String username;
    private String password;
    private String newpassword;
    private String repassword;
}
