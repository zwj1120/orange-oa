package com.xcr.orange.oa.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @file ResourceUtils
 * @version: 1.0
 * @Description: Resource工具类
 * 代码目的，作用，如何工作
 * @Author: Administrator
 * @Date: 2020/12/9 14:23
 * 本代码要注意的事项、备注事项等。
 **/
public class ResourceUtils {


    public static InputStream getResourceAsStream(String fileName) throws IOException {
        if (StringUtils.isEmpty(fileName)) {
            throw new IllegalArgumentException("文件名为空");
        }
        InputStream inputStream;
        File file = new File(fileName);
        if (file.exists()) {
            inputStream = Files.newInputStream(file.toPath());
        } else {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        }
        if (inputStream == null) {
            throw new IOException(String.format("文件【%s】不存在", fileName));
        }
        return inputStream;
    }



}
