package com.yongle.springboot.service.impl;
import com.yongle.springboot.utils.OpenstackUtils;
import com.yongle.springboot.entity.Comment;
import com.yongle.springboot.mapper.CommentMapper;
import com.yongle.springboot.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yongle
 * @since 2023-03-06
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {
        @Resource
        private CommentMapper commentMapper;

    @Override
    public List<Comment> findCommentDetail(Integer rangceId) {
        return commentMapper.findCommentDetail( rangceId);
    }
}
