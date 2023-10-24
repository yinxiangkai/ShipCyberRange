package com.yongle.springboot.service;

import com.yongle.springboot.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yongle
 * @since 2023-03-06
 */
public interface ICommentService extends IService<Comment> {

    List<Comment> findCommentDetail(Integer rangceId);
}
