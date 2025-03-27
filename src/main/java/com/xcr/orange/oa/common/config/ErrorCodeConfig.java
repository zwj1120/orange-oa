package com.xcr.orange.oa.common.config;

import com.fiberhome.daml.common.extend.error.EnableErrorCode;
import com.fiberhome.daml.common.extend.validation.EnableValidate;
import com.fiberhome.daml.common.standard.validation.annotation.WebValidated;
import org.springframework.context.annotation.Configuration;

/**
 * <br> 配置错误码
 *
 * @author guanxingxian
 * @version 1.0
 * @date 2023/5/4 10:26
 * @since 1.8
 */
@Configuration
@EnableValidate(validate = WebValidated.class)
@EnableErrorCode(showDetailMessage = true, messageLocation = "classpath:/config/error-message.properties")
public class ErrorCodeConfig {
}
