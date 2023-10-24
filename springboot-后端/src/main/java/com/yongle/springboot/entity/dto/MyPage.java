package com.yongle.springboot.entity.dto;

import com.yongle.springboot.entity.User;
import lombok.Data;
import org.springframework.boot.autoconfigure.jms.JmsProperties;

import java.util.List;

@Data
public class MyPage<T>{
    Integer total;
    List<T> records;


}
