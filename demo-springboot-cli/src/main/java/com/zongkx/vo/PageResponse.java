package com.zongkx.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author zongkxc
 * @Description
 * @create 2021/6/4  14:30
 */
@Data
@AllArgsConstructor
public class PageResponse<T> {
    private Integer pageNum;
    private Integer pageSize;
    private Long total;
    private List<T> data;
}
