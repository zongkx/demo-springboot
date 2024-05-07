import io.lettuce.core.RedisClient;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * @author zongkxc
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RedisTests {
    RedisClient client;
    @BeforeAll
    public void  ah(){
         client = RedisClient.create("redis://39.97.243.43:6379");
    }
    @Test
    @SneakyThrows
    public void ah1 (){
        // 同一个用户1分钟内连续登陆3次失败，则需要等到2分钟才能登陆
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisStringCommands<String, String> sync = connection.sync();
        SetArgs timeout = SetArgs.Builder.nx().ex(60);

        String keyBan = "user1_prohibit";
        String ban = sync.get(keyBan);
        if(StringUtils.hasLength(ban)){
            String getdel = sync.get(keyBan);
            System.out.println(getdel);
            System.out.println("禁止登录:"+ban);
        }else{
            //模拟登录失败
            String val = sync.get("user1");
            if(Objects.equals(val,"3")){
                sync.set(keyBan,"ban",SetArgs.Builder.nx().ex(120));
                System.out.println("您被禁止了!");
            }else{
                String val2 = StringUtils.hasLength(val)?String.valueOf( Integer.parseInt(val)+1):"1";
                sync.set("user1",val2);
                System.out.println("重新登录!");
            }

        }

    }

}
