package com.xcr.orange.oa.mapper;

import com.fiberhome.daml.gdk.spring.mapper.GDKMapper;
import com.fiberhome.daml.gdk.spring.mapper.Mapper;
import com.xcr.orange.oa.bean.UserInfoDO;

/**
 * @Author xiaochaorou7
 * @Description
 * @Date 2024/5/9
 */
@Mapper
public interface OaUserInfoMapper extends GDKMapper<UserInfoDO> {
    Integer test();
}
