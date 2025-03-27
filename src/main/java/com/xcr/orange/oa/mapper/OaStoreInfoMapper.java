package com.xcr.orange.oa.mapper;

import com.fiberhome.daml.gdk.spring.mapper.GDKMapper;
import com.fiberhome.daml.gdk.spring.mapper.Mapper;
import com.xcr.orange.oa.bean.StoreInfoDO;
import com.gdk.jdbc.annotation.Param;

import java.util.List;

/**
 * @Author xiaochaorou7
 * @Description 店铺信息表Mapper
 * @Date 2024/8/27
 */
@Mapper
public interface OaStoreInfoMapper extends GDKMapper<StoreInfoDO> {
    List<StoreInfoDO> test(@Param("storeId") Long storeId);
}