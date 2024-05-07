package com.zongkx.vo;

import lombok.Data;

/**
 * @author zongkxc
 * @Description
 * @create 2021/5/28  17:39
 */
@Data
public class PageReqVO {
    private Integer pageSize = 10;
    private Integer pageNum = 1;

}
