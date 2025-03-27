package com.xcr.orange.oa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Author xiaochaorou7
 * @Description
 * @Date 2024/5/31
 */
@Service
public class OaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OaService.class);

    /**
     * 获取个人排行榜前十
     * 名称、所属地市、所属部门、得分、用时，答题数、答对数
     *
     * @return 排行结果
     */
    public String test() {
        return "测试成功";
    }

}
