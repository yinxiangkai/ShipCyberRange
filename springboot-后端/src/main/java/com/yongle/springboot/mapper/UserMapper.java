package com.yongle.springboot.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yongle
 * @since 2023-02-09
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
   @Select("SELECT *FROM `user` WHERE role=1 LIMIT #{number},#{size};")
     List<User> findpage(Integer number ,Integer size);
    @Select("SELECT *FROM `user` WHERE role=1 AND username LIKE #{username} LIMIT #{number},#{size};")
    List<User> findpagebyname(Integer number ,Integer size, String username);

    @Select("SELECT COUNT(*) FROM `user` WHERE role=1")
    Integer slectTotal();

}
