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
 * @since 2023-03-02
 */
@Getter
@Setter
  @ApiModel(value = "Host对象", description = "")
public class Host implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("id")
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("主机名")
      private String name;

      @ApiModelProperty("主机类型0攻击机1靶机")
      private Integer type;

      @ApiModelProperty("状态")
      private String status;

      @ApiModelProperty("IP地址")
      private String ipaddr;

      @ApiModelProperty("创建时间")
      private LocalDateTime createtime;

      @ApiModelProperty("用户id")
      private Integer userId;

      @ApiModelProperty("flag码")
      private String flag;

      @ApiModelProperty("分数")
      private Integer grade;

      @ApiModelProperty("靶机组")
      private Integer groupId;

      @ApiModelProperty("靶场")
      private Integer rangceId;

      @ApiModelProperty("opurl")
      private String url;

      @ApiModelProperty("opid")
      private String host;

      @ApiModelProperty("端口")
      private Integer portId;

      @ApiModelProperty("实例类型")
      private Integer flavorId;

      @ApiModelProperty("镜像")
      private Integer imageId;
      @ApiModelProperty("靶机组内编号")
      private Integer number;
      @ApiModelProperty("子网")
      private Integer subnetId;
      @TableField(exist = false)
      private String userName;
      @TableField(exist = false)
      private String no;
      @TableField(exist = false)
      private String subnetName;
      @TableField(exist = false)
      private String rangceName;
      @TableField(exist = false)
      private Port port;
      @TableField(exist = false)
      private Img img;
      @TableField(exist = false)
      private Flavor flavor;



}
