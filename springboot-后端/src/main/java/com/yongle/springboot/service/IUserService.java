package com.yongle.springboot.service;

import com.yongle.springboot.common.Result;
import com.yongle.springboot.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yongle.springboot.entity.dto.UserDTO;
import com.yongle.springboot.entity.dto.UserPasswordDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yongle
 * @since 2023-02-08
 */
public interface IUserService extends IService<User> {


    Result login(UserDTO userDto);

    Result changepassword(UserPasswordDTO userPasswordDTO);


    Result addadmin(User user);

    String addurl(MultipartFile file) throws IOException;
     UserDTO returnDTO(User one,UserDTO userDTO );

    Result register(User user);
}
