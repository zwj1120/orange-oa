package com.xcr.orange.oa.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class SystemProperties {

    private static final Logger LOG = LoggerFactory.getLogger(SystemProperties.class);

    /**
     * 配置文件
     */
    private static final String SCOUT_CONFIG_PROPERTIES = "config/oa/oa-spring.properties";

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStreamReader inputStreamReader = new InputStreamReader(
                ResourceUtils.getResourceAsStream(SCOUT_CONFIG_PROPERTIES), StandardCharsets.UTF_8)) {
            PROPERTIES.load(inputStreamReader);
        } catch (IOException e) {
            LOG.error("配置文件加载失败", e);
            throw new RuntimeException("配置文件加载失败", e);
        }
    }

    //public static final String EXAM_NAME = "exam.name";
    //
    //public static final String EXAM_NAME_STR = PropertiesUtils.getProperty(PROPERTIES, EXAM_NAME);

}
