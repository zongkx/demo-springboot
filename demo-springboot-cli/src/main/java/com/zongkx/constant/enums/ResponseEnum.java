package com.zongkx.constant.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author raynorkxc
 * @Description
 * @create 2021/6/4  14:02
 */
@Getter
@RequiredArgsConstructor
public enum ResponseEnum {
    SUCCESS(20000, "操作成功！"),
    ERROR(40000, "操作失败！"),
    DATA_NOT_FOUND(40001, "查询失败！"),
    PARAMS_NULL(40002, "参数不能为空！"),
    NOT_LOGIN(40003, "当前账号未登录！"),
    PARAMS_ERROR(40005, "参数不合法！"),
    AUTH_DENY(4006,"没有权限"),
    ;

    private final Integer code;
    private final String msg;
}
