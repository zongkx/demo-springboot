package com.zongkx.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zongkx.vo.Result;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import springfox.documentation.oas.web.OpenApiControllerWebMvc;
import springfox.documentation.swagger.web.ApiResourceController;

/**
 * @author zongkxc
 * @Description
 * @create 2021/6/4  14:43
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class ResponseAdvice  implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (MediaType.IMAGE_JPEG.getType().equalsIgnoreCase(selectedContentType.getType())) {
            return body;
        }
        if (body instanceof Result) {
            return body;
        }
        Class<?> c = returnType.getDeclaringClass();
        if(c == ApiResourceController.class || c == OpenApiControllerWebMvc.class){
            return body;
        }
        if(body instanceof String){
            return objectMapper.writeValueAsString(Result.ok(body));
        }
        return Result.ok(body);
    }

}
