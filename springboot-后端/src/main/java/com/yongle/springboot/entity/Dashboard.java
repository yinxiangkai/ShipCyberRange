package com.yongle.springboot.entity;

import java.io.Serializable;
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
 * @since 2023-04-13
 */
@Getter
@Setter
  @ApiModel(value = "Dashboard对象", description = "")
public class Dashboard implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer cpu;

    private Integer ram;

    private Integer disk;


}
