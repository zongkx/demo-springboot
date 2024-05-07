package com.zongkx.conf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zongkxc
 */

@Component
public class JacksonUtil {
    private static ObjectMapper objectMapper ;

    public JacksonUtil(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,true);//忽略反序列化的未知属性,等同于注解 @JsonIgnoreProperties(true)

    }
    // 序列化为 String
    public static String toStr(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
    // 反序列化为 java bean,也可以是 map等集合
    public static<T> T toBean(String json,Class<T> beanClass) throws JsonProcessingException {
        return objectMapper.readValue(json,beanClass);
    }
    // 反序列化为 List<Bean>,也可以是List<Map>
    public static<T> List<T> toListBean(String json,Class<T> tClass) throws JsonProcessingException {
        return objectMapper.readValue(json, new TypeReference<ArrayList<T>>() {});
    }

    // 反序列化为 Map<String,Bean>
    public static <T> HashMap<String, T> toMapBean(String json, Class<T> tClass) throws JsonProcessingException {
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        MapType mapType = typeFactory.constructMapType(HashMap.class,String.class, tClass);
        HashMap<String, T> map = objectMapper.readValue(json, mapType);
        return map;
    }

    // List<Map> 根据map中的key分组 转换为 Map<List>
    public static Map<String,List<String>> listMapGroupToList(List<HashMap<String, String>> listMap){
        Map<String, List<Map.Entry<String, String>>> collect = listMap.stream()
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.groupingBy(a -> a.getKey()));
        Map<String,List<String>> map = new HashMap<>();
        collect.forEach((k,v)-> map.put(k,v.stream().map(a->a.getValue()).collect(Collectors.toList())));
        return map;
    }


    // 特殊转换 (此处对objectMapper 最好使用新的实例,避免污染bean)
    public static<T> T toBean(String json, Class<T> tClass, PropertyNamingStrategy strategy) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(strategy);
        T t = objectMapper.readValue(json, tClass);
        return t;
    }

}
