package com.zongkx.proxy;


import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

@Data
public class DataBase {
    private String name;
    private String ip;
    private String port;
    private String userName;
    private String password;

    public DataBase(DataSourceProperties d) {
        String url = d.getUrl();
        Pattern pattern = Pattern.compile("jdbc:mysql://(?<ip>[^:/]+)(:(?<port>\\d+))?/(?<dbname>\\w+)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            String ip = matcher.group(1);
            String port = matcher.group("port") != null ? matcher.group("port") : ""; // 默认端口3306
            String dbname = matcher.group("dbname");
            this.ip = ip;
            this.port = port;
            this.name = dbname;
        }  
        this.userName = d.getUsername();
        this.password = d.getPassword();
    }
}