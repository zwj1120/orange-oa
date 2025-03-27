package com.xcr.orange.oa.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author xiaochaorou7
 * @Description
 * @Date 2024/9/3
 */
public class DbUtils {


    /**
     * 解决druid 连接池泄露问题
     *
     * @param sql 校验连接性sql
     * @return
     */
    public static Map<String, String> initDruidConfig(String sql) {
        Map<String, String> customize = new HashMap<>();
        // 超时连接强制回收
        customize.put("druid.testWhileIdle", "true");
        // 校验连接是否空闲的sql
        customize.put("druid.validationQuery", sql);
        // 每隔5秒检测是否有存活超过10秒的连接
        customize.put("druid.timeBetweenEvictionRunsMillis", "5000");
        // 空闲连接最小生存时间，10秒
        customize.put("druid.minEvictableIdleTimeMillis", "10000");
        // 空闲连接最大生存时间，11秒
        customize.put("druid.maxEvictableIdleTimeMillis", "11000");
        customize.put("druid.maxWait", "300000");
        return customize;
    }


    public static Connection getConnection(String ip, Integer port, String dbName, String userName, String pwd, String url, String driverStr) {
        String dbUrl = String.format(url, ip, port, dbName);
        Connection connection;
        try {
            Class.forName(driverStr);
            connection = DriverManager.getConnection(dbUrl, userName, pwd);
        } catch (Exception e) {
            throw new RuntimeException("创建连接失败", e);
        }
        return connection;
    }
}
