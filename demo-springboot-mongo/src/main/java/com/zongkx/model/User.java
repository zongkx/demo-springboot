package com.zongkx.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author zongkxc
 */

@Data
@Document("test_user")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)

public class User {
    @Id
    private String id;
    private String name;
    private Date createTime;
}
