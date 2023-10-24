package com.yongle.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
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
 * @since 2023-02-22
 */
@Getter
@Setter
  @ApiModel(value = "Img对象", description = "")
public class Img implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("镜像id")
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("镜像名称")
      private String name;

      @ApiModelProperty("创建时间")
      private LocalDateTime time;

      @ApiModelProperty("创建者")
      private Integer userId;

      @ApiModelProperty("描述")
      private String discription;
      private String status;

      @ApiModelProperty(" openstack  id")
      private String value;

      @ApiModelProperty("来源")
      private String url;
      @TableField(exist = false)
      private String nickName;


}
