package com.xcr.orange.oa.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author zywan
 * @date 2022/3/15 15:38
 * Version : 1.0
 * Description : 代码目的，作用，如何工作
 * Notice : 本代码需要注意事项、备注事项
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {
    public static ApplicationContext applicationContext;//NOSONAR

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;//NOSONAR
    }

    public static <T> T getBean(Class<T> c) {
        return applicationContext.getBean(c);
    }

    public static <T> T getBean(String name, Class<T> c) {
        return applicationContext.getBean(name, c);
    }
}
