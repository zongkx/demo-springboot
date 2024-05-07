import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;

/**
 * @author zongkxc
 */

@Slf4j
public class JacksonTests {

    // 序列化：
    @Test
    @SneakyThrows        
    public void ah1 (){
        ObjectMapper objectMapper =new ObjectMapper();
        //忽略值为null的属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //自定义时间日期格式
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));



    }
    // 反序列化
    @Test
    @SneakyThrows
    public void ah2 (){
        ObjectMapper objectMapper =new ObjectMapper();
        //忽略json中存在但Java对象不存在的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }
}
