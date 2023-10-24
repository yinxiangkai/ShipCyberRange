package com.yongle.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author yongle
 * @since 2023-02-28
 */
@Getter
@Setter
  @TableName("rangce_group")
@ApiModel(value = "RangceGroup对象", description = "")
public class RangceGroup implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private String no;
    private LocalDateTime applyTime;
    private LocalDateTime endTime;
    private String status;
    private Integer rangceId;
    private Integer userId;
    @TableField(exist = false)
    private String rangceName;
    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    private String grade;
    @TableField(exist = false)
    private String myGrade;

}
