package com.zongkx.dto;


import com.zongkx.vo.PageReqVO;
import lombok.Data;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 * @author zongkxc
 */
@Data
public class PageDTO<T> {
    private PageReqVO pageReqVO;
    private Object vo;
    private Criteria other;
    private Class<?> modelClass;
    private Class<T> resultClass;

    private PageDTO(Builder<T> builder) {
        this.modelClass = builder.modelClass;
        this.pageReqVO = builder.pageReqVO;
        this.other = builder.other;
        this.resultClass = builder.resultClass;
        this.vo = builder.vo;
    }
    public static<T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private PageReqVO pageReqVO;
        private Object vo;
        private Criteria other;
        private Class<?> modelClass;
        private Class<T> resultClass;
        public Builder() {
        }
        public Builder<T> pageReqVO(PageReqVO pageReqVO){
            this.pageReqVO = pageReqVO;
            return this;
        }
        public Builder<T> vo(Object vo){
            this.vo = vo;
            return this;
        }
        public Builder<T> other(String k1, Object v1){
            this.other = Criteria.where(k1).is(v1);
            return this;
        }
        public Builder<T> modelClass(Class<?> modelClass){
            this.modelClass = modelClass;
            return this;
        }
        public Builder<T> voClass(Class<T> resultClass){
            this.resultClass = resultClass;
            return this;
        }

        public PageDTO<T> build(){
            return new PageDTO<>(this);
        }
    }
}
