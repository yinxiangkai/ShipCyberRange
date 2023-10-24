package com.yongle.springboot.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.common.StatusEnum;
import com.yongle.springboot.entity.Img;
import com.yongle.springboot.mapper.ImgMapper;
import com.yongle.springboot.service.IImgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yongle.springboot.utils.OpenstackUtils;
import com.yongle.springboot.utils.TokenUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yongle
 * @since 2023-02-22
 */
@Service
public class ImgServiceImpl extends ServiceImpl<ImgMapper, Img> implements IImgService {
    @Resource
    private ImgMapper imgMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Async("asyncExecutor")
    @Override
    public void insertImg(Img img,Integer taskid) throws MalformedURLException {
        OpenstackUtils openstackUtils = new OpenstackUtils();
        img.setValue(openstackUtils.uploadimag(img.getUrl(),img.getName()));
        img.setStatus(openstackUtils.imgBulid(img.getValue()));
        img.setStatus(StatusEnum.Active.toString());
        imgMapper.updateById(img);
        stringRedisTemplate.opsForValue().set(taskid.toString(), StatusEnum.FINISHED.toString());
    }

    @Override
    public void delImg(Integer id) {
        OpenstackUtils openstackUtils = new OpenstackUtils();
        if(imgMapper.selectById(id).getValue()!=null)
        {
            openstackUtils.delimg(imgMapper.selectById(id).getValue());
        }
        imgMapper.deleteById(id);
    }

    @Override
    public Page<Img> findPage(Page<Img> page, String name,String status) {
        return imgMapper.findPage(page,name,status);
    }
}
