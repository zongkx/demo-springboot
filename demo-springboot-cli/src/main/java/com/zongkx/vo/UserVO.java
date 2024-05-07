package com.zongkx.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author zongkxc
 * @Description
 * @create 2021/6/4  14:48
 */
@Data
public class UserVO{
    private String name;
    private LocalDate date;
    private LocalDateTime time;
}
