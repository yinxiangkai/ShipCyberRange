package com.yongle.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author yongle
 * @since 2023-02-24
 */
@Getter
@Setter
  @ApiModel(value = "Rangce对象", description = "")
public class Rangce implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private String name;


    private Integer anumber;


    private String introduction;

    private String url;

    private String cidr;
    private String background;

    private LocalDateTime time;
    private LocalTime useTime;

    private Boolean status;
    @TableField(exist = false)
    private Integer grade;

}
