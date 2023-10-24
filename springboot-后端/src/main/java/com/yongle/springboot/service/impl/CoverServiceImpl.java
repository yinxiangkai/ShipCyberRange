package com.yongle.springboot.service.impl;
import com.yongle.springboot.utils.OpenstackUtils;
import com.yongle.springboot.entity.Cover;
import com.yongle.springboot.mapper.CoverMapper;
import com.yongle.springboot.service.ICoverService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yongle
 * @since 2023-03-05
 */
@Service
public class CoverServiceImpl extends ServiceImpl<CoverMapper, Cover> implements ICoverService {
}
