package com.xcr.orange.oa.core.aspect;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Around("execution(public * com.xcr.orange.oa.controller.QaController.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed();
        }
        HttpServletRequest request = attributes.getRequest();
        LOGGER.info("请求url：{}", request.getRequestURL());
        String method = request.getMethod();
        LOGGER.info("请求方法：{}", method);
        List<String> list = new ArrayList<>();
        if ("GET".equals(method)) {
            Map<String, String[]> parameterMap = request.getParameterMap();
            if (MapUtils.isNotEmpty(parameterMap)) {
                for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                    LOGGER.info("请求参数：{}={}", entry.getKey(), StringUtils.join(entry.getValue()));
                }
            }
        } else {
            Object[] args = joinPoint.getArgs();
            if (args != null) {
                for (Object o : args) {
                    list.add(JSONUtil.toJsonStr(o));
                }
                LOGGER.info("请求参数：{}", StringUtils.join(list));
            }
        }
        Object result = joinPoint.proceed();
        LOGGER.info("请求返回：{}", ObjectUtil.isBasicType(result) ? result : JSONUtil.toJsonStr(result));
        return result;
    }

}
