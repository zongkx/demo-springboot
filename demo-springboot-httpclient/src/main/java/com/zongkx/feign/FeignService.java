package com.zongkx.feign;

import feign.HeaderMap;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.Map;

/**
 * @author zongkxc
 */
public interface FeignService {

    @RequestLine("POST /post")
    @Headers("Content-Type: application/json")
    String list(String jsonNode, @HeaderMap Map<String, String> headers);


    @RequestLine("GET /get?name={name}")
    @Headers("Content-Type: application/json")
    String get(@Param("name") String name);
}
