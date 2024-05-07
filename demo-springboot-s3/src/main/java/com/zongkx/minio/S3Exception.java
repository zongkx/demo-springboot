package com.zongkx.minio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author zongkxc
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class S3Exception extends Exception {
    private int code;
    private String message;
    private Exception e;
    public S3Exception(Exception e){
        this.e = e;
    }
}
