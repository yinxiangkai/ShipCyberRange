package com.yongle.springboot.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.entity.User;
import com.yongle.springboot.mapper.NoticeMapper;
import com.yongle.springboot.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yongle.springboot.common.Result;

import com.yongle.springboot.service.INoticeService;
import com.yongle.springboot.entity.Notice;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yongle
 * @since 2023-02-17
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    private INoticeService noticeService;
    @Resource
    private NoticeMapper noticeMapper;

    private final String now = DateUtil.now();

    // 更新
    @PostMapping("/save")
    public Result save(@RequestBody Notice notice) {

        if (notice.getId() == null) {
            notice.setTime(LocalDateTime.now());
            notice.setUser(TokenUtils.getCurrentUser().getNickname());
            noticeMapper.insert( notice);
            return Result.success(null,"新增成功");
        }else{
            noticeMapper.updateById(notice);
            return Result.success();
        }

    }


    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Integer id) {
        noticeService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        noticeService.removeByIds(ids);
        return Result.success();
    }


    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String title,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(title)) {
            queryWrapper.like("title", title);
        }

        return Result.success(noticeService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
     * 导出接口
     */
    @GetMapping("/get")
    public Result get() {
        return Result.success(noticeMapper.selectList(null));
    }


}

