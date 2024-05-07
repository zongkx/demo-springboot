package com.zongkx.vo;

import com.zongkx.constant.enums.ResponseEnum;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author raynorkxc
 * @Description
 * @create 2021/6/4  14:25
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private int code;

    @Getter
    @Setter
    private String msg;

    @Getter
    @Setter
    private T data;

    public static <T> Result<T> ok() {
        return restResult(null, ResponseEnum.SUCCESS.getCode(), null);
    }

    public static <T> Result<T> ok(T data) {
        return restResult(data, ResponseEnum.SUCCESS.getCode(), null);
    }

    public static <T> Result<T> ok(T data, String msg) {
        return restResult(data, ResponseEnum.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> failed() {
        return restResult(null, ResponseEnum.ERROR.getCode(), null);
    }

    public static <T> Result<T> failed(String msg) {
        return restResult(null, ResponseEnum.ERROR.getCode(), msg);
    }

    public static <T> Result<T> failed(T data) {
        return restResult(data, ResponseEnum.ERROR.getCode(), null);
    }

    public static <T> Result<T> failed(T data, String msg) {
        return restResult(data,ResponseEnum.ERROR.getCode(), msg);
    }

    private static <T> Result<T> restResult(T data, int code, String msg) {
        Result<T> apiResult = new Result<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }
}
