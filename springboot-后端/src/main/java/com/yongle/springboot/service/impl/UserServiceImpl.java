package com.yongle.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yongle.springboot.common.Constants;
import com.yongle.springboot.common.Result;
import com.yongle.springboot.entity.User;
import com.yongle.springboot.entity.dto.UserDTO;
import com.yongle.springboot.entity.dto.UserPasswordDTO;
import com.yongle.springboot.exception.ServiceException;
import com.yongle.springboot.mapper.UserMapper;
import com.yongle.springboot.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yongle.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yongle
 * @since 2023-02-08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private static final Log LOG = Log.get();
    @Value("${files.upload.path}")
    private String fileUploadPath;
    @Resource
    private UserMapper userMapper;
    @Override
    public Result login(UserDTO userDTO) {
        System.out.println(userDTO.getPassword());
        userDTO.setPassword(SecureUtil.pbkdf2(userDTO.getPassword().toCharArray(), "salt".getBytes()));
        System.out.println(userDTO.getPassword());
        User one = getUserInfo(userDTO);
        if (one != null) {
            one.setStatus(true);
            userMapper.updateById(one);
           return Result.success(returnDTO(one,userDTO),"登录成功");
        }else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    @Override
    public Result changepassword(UserPasswordDTO userPasswordDTO) {
        userPasswordDTO.setPassword(SecureUtil.pbkdf2(userPasswordDTO.getPassword().toCharArray(), "salt".getBytes()));
        userPasswordDTO.setNewpassword(SecureUtil.pbkdf2(userPasswordDTO.getNewpassword().toCharArray(), "salt".getBytes()));
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",userPasswordDTO.getUsername());
        queryWrapper.eq("password",userPasswordDTO.getPassword());
        User one;
        try {
            one = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        if (one!=null){
            one.setPassword(userPasswordDTO.getNewpassword());
            userMapper.updateById(one);
            return Result.success(null, "密码修改成功");
        }else{
            return Result.error(Constants.CODE_600, "用户不存在");
        }

    }

    @Override
    public Result addadmin(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",user.getUsername());
        User one;
        one = getOne(queryWrapper);
        if(one!=null)
        {
            return Result.error(Constants.CODE_600,"用户名已存在");
        }else {
            user.setCreatetime(LocalDateTime.now());
            user.setPassword(SecureUtil.pbkdf2(user.getPassword().toCharArray(), "salt".getBytes()));
            user.setRole(1);
            userMapper.insert(user);
            return Result.success(null,"添加成功");
        }

    }

    @Override
    public String addurl(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();//获取文件名称
        String type = FileUtil.extName(originalFilename);//获取文件类型

        // 定义一个文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        //获取文件存储名
        String filename = uuid + StrUtil.DOT + type;
        //文件存储路径
        File uploadFile = new File(fileUploadPath + filename);
        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if(!parentFile.exists()) {
            parentFile.mkdirs();
        }
        String url="http://localhost:8081/file/"+filename;
        file.transferTo(uploadFile);
        return url;
    }

    private User getUserInfo(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        queryWrapper.eq("password", userDTO.getPassword());
        User one;
        try {
            one = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }
    public UserDTO returnDTO(User one,UserDTO userDTO ){
        BeanUtil.copyProperties(one, userDTO, true);
        // 设置token
        String token = TokenUtils.genToken(one.getUid().toString(), one.getPassword());
        userDTO.setToken(token);
        userDTO.setRole(one.getRole());
        userDTO.setPassword("");
        return userDTO;
    }

    @Override
    public Result register(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",user.getUsername());
        User one;
        one = getOne(queryWrapper);
        if(one!=null)
        {
            return Result.error(Constants.CODE_600,"用户名已存在");
        }else {
            user.setCreatetime(LocalDateTime.now());
            user.setPassword(SecureUtil.pbkdf2(user.getPassword().toCharArray(), "salt".getBytes()));
            user.setRole(2);
            user.setGrade(0);
            userMapper.insert(user);
            return Result.success(null,"添加成功");
        }
    }
}
