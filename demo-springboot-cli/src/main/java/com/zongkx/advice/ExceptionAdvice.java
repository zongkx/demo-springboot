package com.zongkx.advice;

import com.zongkx.constant.enums.ResponseEnum;
import com.zongkx.vo.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zongkxc
 * @Description
 * @create 2021/6/4  14:38
 */
@ControllerAdvice
@Slf4j
public class ExceptionAdvice {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<String> handlerException(Exception e){
        log.error(" 公共异常:  ",e);
        return new Result<>(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMsg(), e.getMessage());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public Result<String> handlerIllegalArgumentException(IllegalArgumentException e){
        log.error("非法参数异常: ",e);
        return new Result<>(ResponseEnum.PARAMS_ERROR.getCode(), ResponseEnum.PARAMS_ERROR.getMsg(),e.getMessage());
    }
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public Result<String> handlerNullPointerException(NullPointerException e){
        log.error("空值异常:  ",e);
        return new Result<>(ResponseEnum.PARAMS_NULL.getCode(), ResponseEnum.PARAMS_NULL.getMsg(),e.getMessage());
    }
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result<String> handlerCustomException(CustomException e){
        log.error("自定义异常:  ",e);
        return new Result<>(e.getCode(),e.getMsg(),"");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result<String> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("非法参数异常:  ",e);
        List<String> errors = e.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.toCollection(ArrayList::new));
        return new Result<>(ResponseEnum.PARAMS_ERROR.getCode(), ResponseEnum.PARAMS_ERROR.getMsg(),errors.toString());
    }
    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    public static class CustomException extends Exception {
        private int code;
        private String msg;
    }
}
