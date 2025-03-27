package com.xcr.orange.oa.bean;

import com.gdk.jdbc.annotation.Column;
import com.gdk.jdbc.annotation.Id;
import com.gdk.jdbc.annotation.Table;
import com.gdk.jdbc.jpa.IdGeneratorType;

/**
 * @Author xiaochaorou7
 * @Description 采购信息表
 * @Date 2024/8/27
 */
@Table("orange_oa_procurement_info")
public class ProcurementInfoDO {

    /**
     * 主键id
     */
    @Id(generator = IdGeneratorType.AUTO_INCREMENT)
    @Column(name = "id")
    private Long id;

    /**
     * 店铺id
     */
    @Column(name = "store_id")
    private Integer storeId;

    /**
     * 人员id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 商品名称
     */
    @Column(name = "goods_name")
    private String goodsName;

    /**
     * 商品数量
     */
    @Column(name = "goods_num")
    private Integer goodsNum;

    /**
     * 商品总价
     */
    @Column(name = "goods_total_price")
    private Integer goodsTotalPrice;

    /**
     * 年份
     */
    @Column(name = "year")
    private Integer year;

    /**
     * 采购时间
     */
    @Column(name = "procurement_time")
    private Long procurementTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public Integer getGoodsTotalPrice() {
        return goodsTotalPrice;
    }

    public void setGoodsTotalPrice(Integer goodsTotalPrice) {
        this.goodsTotalPrice = goodsTotalPrice;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Long getProcurementTime() {
        return procurementTime;
    }

    public void setProcurementTime(Long procurementTime) {
        this.procurementTime = procurementTime;
    }
}