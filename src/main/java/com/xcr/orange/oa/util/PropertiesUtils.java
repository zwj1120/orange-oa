package com.xcr.orange.oa.util;

import org.apache.commons.lang3.StringUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * properties配置文件工具类
 *
 * @author nli
 * @version 1.0
 * @date 2019/9/26 14:30
 **/
public class PropertiesUtils {

    private static final Pattern PATTERN = Pattern.compile("\\$\\{([^}]+)}");

    /**
     * 无默认值的获取配置信息.
     *
     * @param key 参数
     * @return 值
     */
    public static String getProperty(Properties properties, String key) {
        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException(String.format("参数【%s】不能为空!", key));
        }
        String value = format(properties, key.trim());
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException(String.format("参数【%s】未配置!", key));
        }
        return value.trim();//NOSONAR
    }

    /**
     * 有默认值的获取配置信息.
     *
     * @param key          参数
     * @param defaultValue 默认值
     * @return 值
     */
    public static String getProperty(Properties properties, String key, String defaultValue) {
        String value = format(properties, key.trim());
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        return value.trim();//NOSONAR
    }

    private static String format(Properties properties, String name) {
        String value = properties.getProperty(name);
        if (value == null) {
            return null;
        }
        Matcher matcher = PATTERN.matcher(value);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String matcherKey = matcher.group(1);
            String matcherValue = getProperty(properties, matcherKey);
            matcher.appendReplacement(buffer, matcherValue);
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    public static void saveProperties(Properties properties, String filePath) {
        try (OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(Paths.get(filePath)), StandardCharsets.UTF_8)) {
            properties.store(writer, null);
        } catch (IOException e) {
            throw new RuntimeException(MessageFormat.format("配置文件写入失败【{0}】", filePath), e);
        }
    }

}
