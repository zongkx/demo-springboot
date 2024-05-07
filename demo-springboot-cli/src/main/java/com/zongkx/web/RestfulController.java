package com.zongkx.web;

import com.google.common.collect.Lists;
import com.zongkx.annotation.AccessPermission;
import com.zongkx.vo.PageRequest;
import com.zongkx.vo.PageResponse;
import com.zongkx.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author raynorkxc
 * @Description
 * @create 2021/5/31  17:23
 */
@RestController
@RequestMapping("/rest")
@Slf4j
public class RestfulController {


    //分页查询
    @GetMapping
    public PageResponse<UserVO> get(UserVO userVO, @Validated PageRequest page){
        log.info(userVO.toString());
        return new PageResponse<>(page.getPageNum(),page.getPageSize(),
                2L, Lists.newArrayList(userVO));
    }
    //根据id获取
    @AccessPermission(type = "data",path = "path.id" )
    @GetMapping("/{id}")
    public UserVO getOne(@PathVariable String id,HttpServletRequest request){
        System.out.println(request.getAttribute("test"));
        log.info(id);
        return new UserVO();
    }
    //新增
    @PostMapping
    @AccessPermission(type = "data",path = "body.name",voClass = UserVO.class)
    public Object add(@RequestBody UserVO user, HttpServletRequest request){
        log.info(user.toString());
        log.info(request.getParameter("my"));
        return user;
    }
    //补丁(更新部分属性)
    @PatchMapping("/{id}")
    public boolean updatePartProperty(@RequestBody UserVO user,@PathVariable String id){
        log.info(user.toString(),id);
        return true;
    }
    //修改(更新全部属性)
    @PutMapping("/{id}")
    public boolean updateAllProperty(@RequestBody UserVO user,@PathVariable String id){
        log.info(user.toString(),id);
        return true;
    }
}
