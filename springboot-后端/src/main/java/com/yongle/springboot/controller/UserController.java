package com.yongle.springboot.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.common.Constants;
import com.yongle.springboot.entity.Network;
import com.yongle.springboot.entity.dto.MyPage;
import com.yongle.springboot.entity.dto.UserDTO;
import com.yongle.springboot.entity.dto.UserPasswordDTO;
import com.yongle.springboot.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yongle.springboot.common.Result;

import com.yongle.springboot.service.IUserService;
import com.yongle.springboot.entity.User;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yongle
 * @since 2023-02-08
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;
    @Resource
    private UserMapper userMapper;

//    登录
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        return userService.login(userDTO);
    }
    @PostMapping("/logout/{id}")
    public Result logout(@PathVariable Integer id) {
       User one=userMapper.selectById(id);
       if(one!=null)
       {
           one.setStatus(false);
           userMapper.updateById(one);
       }
        return Result.success();
    }
//    修改密码
    @PostMapping("/changepassword")
    public Result login(@RequestBody UserPasswordDTO userPasswordDTO) {
        String username = userPasswordDTO.getUsername();
        String password = userPasswordDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        return userService.changepassword(userPasswordDTO);
    }
//    添加管理员
    @PostMapping("/addadmin")
    public Result addadmin(@RequestBody User user){
        String username = user.getUsername();
        String password = user.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        return userService.addadmin(user);
    }
    @PostMapping("/register")
    public Result register(@RequestBody User user){
        String username = user.getUsername();
        String password = user.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        return userService.register(user);
    }
//删除管理员
    @DeleteMapping("/del/{id}")
    public Result delete( @PathVariable Integer id) {
        userService.removeById(id);
        return Result.success(null,"注销成功");
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> uids) {
        userService.removeByIds(uids);
        return Result.success(null,"删除成功");
    }

 //分页查询
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pagenumber,@RequestParam Integer pagesize){
        pagenumber=(pagenumber-1)*pagesize;
        MyPage<User> myPage=new MyPage();
        myPage.setTotal(userMapper.slectTotal());
        myPage.setRecords(userMapper.findpage(pagenumber,pagesize));
        return Result.success(myPage,null);
    }
    @GetMapping("/grade")
    public Result getGrade(){
        QueryWrapper<User> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("role",2);
        queryWrapper.orderByDesc("grade");
        return Result.success(userMapper.selectList(queryWrapper),null);
    }
    @GetMapping("/pageuser")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {

            queryWrapper.like("username", name);
        }
        queryWrapper.eq("role",2);
        return Result.success(userService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }


    @GetMapping("/getcookie")
    public Result getcookie(@RequestParam  Integer uid) {
        User one=userMapper.selectById(uid);
        UserDTO userDTO = new UserDTO();
        return Result.success(userService.returnDTO(one,userDTO),"获取新的cookie");
    }
    @GetMapping("/username/{username}")
    public Result findByUsername(@PathVariable String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User one =userService.getOne( queryWrapper);
        one.setPassword("");
        return Result.success(one,"获取用户信息成功");
    }
    @PostMapping ("/update")
    public  Result updateuser(@RequestBody User user){
        User one=userMapper.selectById(user.getUid());
        one.setNickname(user.getNickname());
        one.setAvatarurl(user.getAvatarurl());
        one.setEmail(user.getEmail());
        userMapper.updateById(one);
        UserDTO userDTO = new UserDTO();
        return Result.success(userService.returnDTO(one,userDTO),"更新成功");
    }
    @GetMapping("/getNumber")
    public Result getNumber() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role",2);
        return Result.success(userService.count(queryWrapper));
    }
    @GetMapping("/getOnlineNumber")
    public Result getOnlineNumber() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role",2);
        queryWrapper.eq("status",true);
        return Result.success(userService.count(queryWrapper));
    }
}

