package com.zongkx.proxy;

import lombok.RequiredArgsConstructor;
import org.hibernate.resource.jdbc.spi.StatementInspector;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class DuckDBInterceptor implements StatementInspector {
    @Override
    public String inspect(String s) {
        if ("duckdb".equals(DuckDBThreadLocal.getRoutingDataSource())) {
            // 正则表达式提取所有FROM/JOIN后面的和AS之间表名
            String regex = "(?:FROM|JOIN|from|join)\\s+([\\w.]+)\\s+(?:AS\\s+)?[\"']?[\\w.]+[\"']?";
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(s);
            Set<String> tables = new HashSet<>();
            while (matcher.find()) {
                String tableName = matcher.group(1);
                tables.add(tableName);
            }
            for (String table : tables) {
                exec(table);
            }
        }
        return s;
    }

    private void exec(String table) {
    }
}

