package com.zongkx.vo;

import lombok.Data;

import java.util.List;

/**
 * @author zongkxc
 */
@Data
public class PageRespVO<T> {
    private Integer pageNum;
    private Integer pageSize;
    private Long total;
    private List<T> data;




    public PageRespVO(Integer pageNum, Integer pageSize, Long total, List<T> data) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.data = data;
    }
}
