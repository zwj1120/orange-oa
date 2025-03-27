package com.xcr.orange.oa.bean;

import com.gdk.jdbc.annotation.Column;
import com.gdk.jdbc.annotation.Id;
import com.gdk.jdbc.annotation.Table;
import com.gdk.jdbc.jpa.IdGeneratorType;

/**
 * @Author xiaochaorou7
 * @Description 店铺信息表
 * @Date 2024/8/27
 */
@Table("orange_oa_store_info")
public class StoreInfoDO {

    /**
     * 主键id
     */
    @Id(generator = IdGeneratorType.AUTO_INCREMENT)
    @Column(name = "id")
    private Long id;

    /**
     * 店铺名称
     */
    @Column(name = "store_name")
    private String storeName;

    /**
     * 店铺类别 0:甜品店 1:咖啡厅 2:面包房 3:其他
     */
    @Column(name = "store_type")
    private Integer storeType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Integer getStoreType() {
        return storeType;
    }

    public void setStoreType(Integer storeType) {
        this.storeType = storeType;
    }
}