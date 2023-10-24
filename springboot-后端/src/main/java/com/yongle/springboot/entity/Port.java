package com.yongle.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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
  @ApiModel(value = "Port对象", description = "")
public class Port implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private String name;

    private String ipaddr;

    private String port;

    private Integer subnetId;

    private Integer networkId;
    @TableField(exist = false)
    private String subnet;
    @TableField(exist = false)
    private String network;
      @ApiModelProperty("状态")
      private String status;

}
