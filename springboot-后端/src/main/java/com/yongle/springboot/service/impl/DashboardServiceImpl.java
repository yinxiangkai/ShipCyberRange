package com.yongle.springboot.service.impl;
import com.yongle.springboot.utils.OpenstackUtils;
import com.yongle.springboot.entity.Dashboard;
import com.yongle.springboot.mapper.DashboardMapper;
import com.yongle.springboot.service.IDashboardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yongle
 * @since 2023-04-13
 */
@Service
public class DashboardServiceImpl extends ServiceImpl<DashboardMapper, Dashboard> implements IDashboardService {
        @Resource
        private DashboardMapper dashboardMapper;
}
