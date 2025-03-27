package com.xcr.orange.oa.bean;

import com.gdk.jdbc.annotation.Column;
import com.gdk.jdbc.annotation.Id;
import com.gdk.jdbc.annotation.Table;
import com.gdk.jdbc.jpa.IdGeneratorType;

/**
 * @Author xiaochaorou7
 * @Description 用户信息表
 * @Date 2024/8/27
 */
@Table("orange_oa_user_info")
public class UserInfoDO {

    /**
     * 主键id
     */
    @Id(generator = IdGeneratorType.AUTO_INCREMENT)
    @Column(name = "id")
    private Long id;

    /**
     * 人员名称
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 人员类别 0:老板 1:管理层 2:甜品师 3:咖啡师 4:面包师 5:面包中工 6:前台 7:兼职 8:学徒 9:其他
     */
    @Column(name = "user_type")
    private Integer userType;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Long createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
