package com.zongkx.mock;
import lombok.RequiredArgsConstructor;

import java.util.Random;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zongkx.proxy.DuckDBDataSource;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class UserApi {
    private final JdbcTemplate duckJdbcTemplate;
    private final UserRepo userRepo;


    @GetMapping("1")
    public Object test() {
        return duckJdbcTemplate.queryForList("select version();");
    }

    @GetMapping("2")
    public Object test2() {
        User user = new User(IdUtil.getSnowflakeNextId(), new Random(200).toString());
        userRepo.save(user);
        System.out.println(userRepo.version());
        return userRepo.findAll();
    }

    @GetMapping("3")
    @DuckDBDataSource
    public Object test3() {
        User user = new User(IdUtil.getSnowflakeNextId(), new Random(200).toString());
        userRepo.save(user);
       // System.out.println(userRepo.version());
        return userRepo.findAll();
    }

}
