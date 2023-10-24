package com.yongle.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
 * @since 2023-04-16
 */
@Getter
@Setter
  @TableName("rangce_host")
@ApiModel(value = "RangceHost对象", description = "")
public class RangceHost implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private Integer rangceId;

    private Integer number;

    private String imageUrl;

    private Integer preNumber;

    private Boolean status;

    private Integer imageId;

    private Integer flavorId;

    private Integer grade;

    private Integer type;

    private Integer positionX;

    private Integer positionY;

    private String name;

    private String flag;

    private String ipaddr;
}
