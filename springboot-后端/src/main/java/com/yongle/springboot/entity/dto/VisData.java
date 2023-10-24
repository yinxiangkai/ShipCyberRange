package com.yongle.springboot.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class VisData {
    List<Node> nodes;
    List<Edge> edges;
}
