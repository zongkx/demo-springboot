/**
 * @author zongkxc
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.zongkx.conf.JacksonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;


/**
 * @author zongkxc
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JsonTests {
    private JacksonUtil jacksonUtil;
    @BeforeAll
    public void before() {
        jacksonUtil = new JacksonUtil(new ObjectMapper());
    }


    @Test
    @SneakyThrows
    public void a1() {

        String s = "{\n" +
                "\"name\":\"Paul\"\n" +
                "}";
        System.out.println(jacksonUtil.toBean(s, HashMap.class));
    }
    @Test
    @SneakyThrows
    public void a2 (){
        String s = "[{\n" +
                "\"name\":\"Paul\"\n" +
                "}]";
        List list = jacksonUtil.toBean(s, List.class);
        System.out.println(list);
    }
    @Test
    @SneakyThrows        
    public void a3 (){
        String s = "{\"u1\":{\"name\":\"Paul\"},\"u2\":{\"name\":\"Eve\"}}";
        HashMap<String, User> stringUserHashMap = jacksonUtil.toMapBean(s, User.class);
        System.out.println(stringUserHashMap);
        HashMap<String, HashMap> stringUserHashMap1 = jacksonUtil.toMapBean(s, HashMap.class);
        System.out.println(stringUserHashMap1);
    }

    @Test
    @SneakyThrows
    public void a4 (){
        HashMap<String, String > map1 = new HashMap<>();
        map1.put("name","Paul");
        map1.put("age","12");

        HashMap<String, String > map2 = new HashMap<>();
        map2.put("name","Eve");
        map2.put("age","13");

        Map<String, List<String>> stringListMap = JacksonUtil.listMapGroupToList(new ArrayList<>(Arrays.asList(map1, map2)));
        System.out.println(stringListMap);
    }
    @Test
    @SneakyThrows        
    public void a5 (){
        String s = "{\"user_name\":\"bflee\",\"id_number\":\"123456\"}";
        System.out.println(jacksonUtil.toBean(s, User1.class, PropertyNamingStrategies.SNAKE_CASE));

    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User{
        private String name;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class User1 {
        private String userName;
        private String idNumber;
    }
}