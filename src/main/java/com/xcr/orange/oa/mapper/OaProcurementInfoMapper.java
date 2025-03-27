package com.xcr.orange.oa.mapper;

import com.fiberhome.daml.gdk.spring.mapper.GDKMapper;
import com.fiberhome.daml.gdk.spring.mapper.Mapper;
import com.xcr.orange.oa.bean.ProcurementInfoDO;
import com.gdk.jdbc.annotation.Param;

import java.util.List;

/**
 * @Author xiaochaorou7
 * @Description 采购信息表Mapper
 * @Date 2024/8/27
 */
@Mapper
public interface OaProcurementInfoMapper extends GDKMapper<ProcurementInfoDO> {
    List<ProcurementInfoDO> test(@Param("procurementId") Long procurementId);
}