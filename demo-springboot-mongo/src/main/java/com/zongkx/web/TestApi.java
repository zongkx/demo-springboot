package com.zongkx.web;

import com.zongkx.dto.PageDTO;
import com.zongkx.model.User;
import com.zongkx.mongo.QueryUtil;
import com.zongkx.vo.PageReqVO;
import com.zongkx.vo.PageRespVO;
import com.zongkx.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author zongkxc
 */
@RequiredArgsConstructor
@RestController
public class TestApi {
    private final MongoTemplate mongoTemplate;
    @PostMapping("/1")
    public PageRespVO<UserVO> page( PageReqVO pageReqVO, @RequestBody UserVO userVO){
        // 模拟一些数据
        mongoTemplate.save(new User().setId("1").setName("Paul").setCreateTime(new Date()));
        mongoTemplate.save(new User().setId("2").setName("Eve").setCreateTime(new Date()));
        mongoTemplate.save(new User().setId("3").setName("Bob").setCreateTime(new Date()));

        PageDTO<UserVO> build = PageDTO.<UserVO>builder()
                .pageReqVO(pageReqVO)
                .modelClass(User.class)
                .voClass(UserVO.class)
                .vo(userVO)
                .build();
        return QueryUtil.page(build);
    }
}
