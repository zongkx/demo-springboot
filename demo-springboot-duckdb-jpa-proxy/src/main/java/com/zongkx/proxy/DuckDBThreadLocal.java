package com.zongkx.proxy;


public class DuckDBThreadLocal {
    public static final ThreadLocal<String> threadLocalDataSource = new ThreadLocal<>();


    public static String getRoutingDataSource() {
        String dataSourceType = threadLocalDataSource.get();
        if (dataSourceType == null) {
            threadLocalDataSource.set("default");
            return getRoutingDataSource();
        }
        return dataSourceType;
    }

    public static void setRoutingDataSource(String dataSource) {
        if (dataSource == null) {
            throw new NullPointerException();
        }
        threadLocalDataSource.set(dataSource);
    }

    public static void removeRoutingDataSource() {
        threadLocalDataSource.remove();
    }

}
