package com.zongkx.web;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author zongkxc
 */

@Slf4j
@RestController
public class TestApi {


    // 模拟get请求
    @GetMapping("/get")
    public String getToken(String name){
        log.info(name);
        return "test";
    }

    // 模拟 post 请求, 附带token / request body
    @PostMapping("/post")
    public String list(@RequestBody JsonNode body, @RequestHeader(name = "token") String token)  {
        log.info(body.asText(),token);
        return token;

    }
}
