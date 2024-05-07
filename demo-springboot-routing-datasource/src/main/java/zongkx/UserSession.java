package zongkx;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author zongkxc
 */

@Component
public class UserSession {
    public Map<String,String> map ;
    public String user;
    @PostConstruct
    public void init(){
        map.put("user1","ds1");
        map.put("user2","ds2");
    }

    public String get(){
        return map.get(user);
    }
}
