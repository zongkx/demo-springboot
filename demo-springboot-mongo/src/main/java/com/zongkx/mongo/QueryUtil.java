package com.zongkx.mongo;

import com.zongkx.dto.PageDTO;
import com.zongkx.vo.PageReqVO;
import com.zongkx.vo.PageRespVO;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zongkxc
 */

@Component
public class QueryUtil {
    public static final String MAP_KEY_TEMP = "%s.%s";

    private static  MongoTemplate mongoTemplate ;

    public QueryUtil(MongoTemplate mongoTemplate1) {
        mongoTemplate = mongoTemplate1;
    }
    public static  <T> PageRespVO<T> page(PageDTO<T> dto) {
        Query query = initQuery(dto.getVo());
        PageReqVO pageReqVO = dto.getPageReqVO();
        PageRequest pageRequest = PageRequest.of(pageReqVO.getPageNum()-1,pageReqVO.getPageSize());
        long count = mongoTemplate.count(query,dto.getModelClass());
        return new PageRespVO<>(pageReqVO.getPageNum(),pageReqVO.getPageSize(),count,
                mongoTemplate.find(query.with(pageRequest),dto.getModelClass())
                        .stream().map( entity->BeanMapping.trans(entity,dto.getResultClass()))
                        .collect(Collectors.toCollection(ArrayList::new)));
    }

    @SneakyThrows// 该异常会被CommonExceptionHandler处理,故选择不抛出
    private static Query initQuery(Object vo)   {
        Query query = new Query();
        Criteria criteriaObj = new Criteria();
        Class<?> clazz = vo.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Class<?> superClass = clazz.getSuperclass();
        Field[] allFields;
        if(superClass!= null && !Objects.equals(superClass,Object.class)){
            Field[] fields1 = clazz.getSuperclass().getDeclaredFields();
            allFields = ArrayUtils.addAll(fields1, fields);
        }else {
            allFields = fields;
        }
        List<Criteria> list1 = new ArrayList<>();
        List<Sort.Order> sortList =new ArrayList<>();
        for (Field field : allFields) {
            String key = field.getName();
            field.setAccessible(true);
            Object val = field.get(vo);
            if(val == null){
                continue;
            }
            Class<?> type = field.getType();
            if (Objects.equals(type,Boolean.class) ){//布尔 date
                list1.add(Criteria.where(key).is(val));
            }
            if (Objects.equals(type,String.class) && StringUtils.hasLength(val.toString())){//string
                list1.add(Criteria.where(key).regex(val.toString()));
            }
            if(type.isEnum()){//枚举
                list1.add(Criteria.where(key).is(val));
            }
            if(isListString(field)){// list<String>
                List<String> l = (List)val;
                list1.add(Criteria.where(key).all(l.toArray()));
            }
            if(Objects.equals(type, Map.class)){ // map
                Map<String,Object> map = (Map)val;
                map.forEach((k,v)-> list1.add(Criteria.where(String.format(MAP_KEY_TEMP,key,k)).is(v)));
            }

        }
        if(!list1.isEmpty()){
            criteriaObj.andOperator(list1.toArray(new Criteria[0]));
        }
        query.addCriteria(criteriaObj);
        return query;
    }
    private static boolean isListString(Field field){
        if(Objects.equals(field.getType(),List.class) && field.getGenericType() instanceof ParameterizedType){
            Type t = Arrays.stream(((ParameterizedType) field.getGenericType()).getActualTypeArguments()).findFirst().orElse(null);
            return Objects.equals(t,String.class);
        }
        return false;
    }
}
