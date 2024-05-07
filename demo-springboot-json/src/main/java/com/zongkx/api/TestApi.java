package com.zongkx.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zongkxc
 */

@RestController
public class TestApi {
    @GetMapping("/1")
    public String ok(){
        return "ok";
    }
}
