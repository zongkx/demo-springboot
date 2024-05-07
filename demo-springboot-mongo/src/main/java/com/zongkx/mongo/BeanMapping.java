package com.zongkx.mongo;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**

 */
public class BeanMapping {

    static final MapperFactory mapperFactory;

    static {
        mapperFactory = new DefaultMapperFactory.Builder().build();
    }


    public static <S, T> T trans(S s, Class<T> target) {
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        return mapperFacade.map(s, target);
    }



}
