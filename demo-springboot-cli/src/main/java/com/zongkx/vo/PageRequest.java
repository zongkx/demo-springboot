package com.zongkx.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author zongkxc
 * @Description
 * @create 2021/6/4  14:29
 */
@Data
public class PageRequest {
    @Max(value = 40L,message = "分页最大值不超过40")
    @Min(value = 1L,message = "分页最小值不小于1")
    private Integer pageSize = 10;
    @Min(value = 1L,message = "页码最小值不小于1")
    private Integer pageNum = 1;
}
