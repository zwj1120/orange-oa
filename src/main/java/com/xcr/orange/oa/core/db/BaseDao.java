package com.xcr.orange.oa.core.db;

import com.xcr.orange.oa.util.GdkUtils;
import com.gdk.jdbc.DBFactory;
import com.gdk.jdbc.JdbcHandler;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 基础dao
 *
 * @author Administrator
 */
@Repository
public class BaseDao {

    /**
     * 探查工具连接数据库
     */
    public JdbcHandler examJdbcHandler = DBFactory.create("oa");

    /**
     * 查询指定序列下个值
     */
    private static final String SQL_GET_NEXT_SEQ_VAL = "SELECT nextval(:SEQ_NAME) ";

    protected static final int INITIAL_CAPACITY_2 = 2;

    /**
     * 保存对象，返回带id的bean
     *
     * @param obj 对象
     * @param <T> 类型
     * @return 带id的bean
     */
    public <T> T save(T obj) {
        return examJdbcHandler.save(obj);
    }

    /**
     * 保存对象，返回带id的bean
     *
     * @param <T> 类型
     * @param obj 对象
     */
    public <T> void update(T obj) {
        examJdbcHandler.update(obj);
    }


    /**
     * 根据主键ID删除记录
     *
     * @param <T>   类型
     * @param clazz 类名
     * @param obj   对象
     */
    public <T> void deleteById(Class<T> clazz, Object obj) {
        examJdbcHandler.deleteById(clazz, obj);
    }

    /**
     * 根据过滤条件删除记录
     *
     * @param clazz         类名
     * @param whereCriteria 过滤条件
     * @param params        参数
     * @param <T>           类型
     */
    public <T> void delete(Class<T> clazz, String whereCriteria, Object... params) {
        examJdbcHandler.delete(clazz, whereCriteria, params);
    }

    /**
     * 批量添加数据
     *
     * @param beanList bean对象列表
     * @param idFlag   是否包含id
     * @param <T>      类型
     * @throws SQLException sql异常
     */
    public <T> void batchInsert(List<T> beanList, boolean idFlag) throws SQLException {
        GdkUtils.batchInsert(examJdbcHandler, beanList, idFlag);
    }

    /**
     * 删除表
     *
     * @param tableName 表名
     * @throws SQLException sql异常
     */
    public void deleteTable(String tableName) throws SQLException {
        examJdbcHandler.executeUpdate("DROP TABLE IF EXISTS  " + tableName);
    }

    /**
     * 获取指定序列下个值
     *
     * @param seqName 序列名
     * @return 序列值
     * @throws SQLException sql异常
     */
    public int nextSeq(String seqName) throws SQLException {
        Map<String, Object> param = GdkUtils.newParam("SEQ_NAME", seqName);
        return examJdbcHandler.queryForInteger(SQL_GET_NEXT_SEQ_VAL, param);
    }

    /**
     * 查询指定表的总量
     *
     * @param tableName 表名
     * @return 总量
     * @throws SQLException sql异常
     */
    public long count(String tableName) throws SQLException {
        return examJdbcHandler.queryForLong("SELECT COUNT(*) FROM " + tableName);
    }

    public <T> T queryForBean(Class<T> clazz, String sql, Map<String, Object> param) throws SQLException {
        return examJdbcHandler.queryForBean(clazz, sql, param);
    }

    /**
     * 判断样例表是否存在
     *
     * @param tableName
     * @return true 存在, false不存在
     */
    public boolean checkTableIsExist(String tableName) {
        boolean flag;
        String sql = String.format("select 1 from %s limit 1", tableName);
        try {
            examJdbcHandler.queryForList(String.class, sql);
            flag = true;
        } catch (SQLException throwables) {
            flag = false;
        }
        return flag;
    }



}
