package com.xcr.orange.oa.common.config;

import com.fiberhome.daml.common.extend.error.EnableErrorCode;
import com.fiberhome.daml.gdk.spring.jdbc.JdbcHandler;
import com.fiberhome.daml.gdk.spring.mapper.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * GDK
 *
 * @author ZhangDong X7450
 * @date 2023-03-27 09:11
 */
@Configuration
@MapperScan(basePackages = "com.xcr.orange.oa")
@JdbcHandler(name = "oa")
@EnableErrorCode(showDetailMessage = true)
public class GdkConfiguration {

}
