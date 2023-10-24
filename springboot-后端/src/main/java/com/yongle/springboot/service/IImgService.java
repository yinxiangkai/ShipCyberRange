package com.yongle.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.entity.Img;
import com.baomidou.mybatisplus.extension.service.IService;

import java.net.MalformedURLException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yongle
 * @since 2023-02-22
 */
public interface IImgService extends IService<Img> {

    void insertImg(Img img,Integer taskid) throws MalformedURLException;

    void delImg(Integer id);

    Page<Img> findPage(Page<Img> page, String name,String status);
}
