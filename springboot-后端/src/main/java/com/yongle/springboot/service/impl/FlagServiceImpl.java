package com.yongle.springboot.service.impl;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.common.Constants;
import com.yongle.springboot.common.Result;
import com.yongle.springboot.entity.Host;
import com.yongle.springboot.entity.User;
import com.yongle.springboot.mapper.HostMapper;
import com.yongle.springboot.mapper.UserMapper;
import com.yongle.springboot.utils.OpenstackUtils;
import com.yongle.springboot.entity.Flag;
import com.yongle.springboot.mapper.FlagMapper;
import com.yongle.springboot.service.IFlagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yongle
 * @since 2023-03-07
 */
@Service
public class FlagServiceImpl extends ServiceImpl<FlagMapper, Flag> implements IFlagService {
        @Resource
        private FlagMapper flagMapper;
        @Resource
        private HostMapper hostMapper;
        @Resource
        private UserMapper userMapper;

    @Override
    public Result insertFlag(Flag flag) {

        QueryWrapper<Host> queryWrapper1=new QueryWrapper<>();
        queryWrapper1.eq("flag",flag.getFlag());
        queryWrapper1.eq("rangce_id",flag.getRangceId());
        queryWrapper1.eq("user_id",flag.getUserId());
        Host host= hostMapper.selectOne(queryWrapper1);
        if(host==null){
            return Result.error(Constants.CODE_600,"flag码错误");
        }else{
            QueryWrapper<Flag> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("user_id",flag.getUserId());
            queryWrapper.eq("rangce_id",flag.getRangceId());
            queryWrapper.eq("number",host.getNumber());
            if(flagMapper.selectOne(queryWrapper)!=null)
            {
                return Result.error(Constants.CODE_600,"请不要重复提交flag");
            }else{
                flag.setFlag(host.getFlag());
                flag.setTime(DateUtil.now());
                flag.setNumber(host.getNumber());
                flag.setGrade(host.getGrade());
                flagMapper.insert(flag);
                User user= userMapper.selectById(flag.getUserId());
                user.setGrade(user.getGrade()+host.getGrade());
                userMapper.updateById(user);
                return Result.success(null,"提交成功获取"+host.getGrade()+"分");
            }
        }
    }

    @Override
    public Page<Flag> findPage(Page<Flag> page, String rangceName, Integer userId) {
       return  flagMapper.findPage(page,rangceName,userId);
    }
}
