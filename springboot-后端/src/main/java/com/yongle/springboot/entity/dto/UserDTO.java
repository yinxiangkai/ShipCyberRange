package com.yongle.springboot.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;

@Data
public class UserDTO {
    private Integer uid;
    private String username;

    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;
    private Integer role;
}
