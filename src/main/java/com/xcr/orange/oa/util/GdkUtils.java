package com.xcr.orange.oa.util;


import cn.hutool.core.collection.CollectionUtil;
import com.fiberhome.daml.common.standard.error.code.ErrorCode;
import com.fiberhome.daml.common.standard.exception.BusinessException;
import com.xcr.orange.oa.common.annotation.BatchColumn;
import com.gdk.jdbc.BatchPreparedStatementSetter;
import com.gdk.jdbc.JdbcHandler;
import com.gdk.jdbc.annotation.Column;
import com.gdk.jdbc.annotation.ColumnMap;
import com.gdk.jdbc.annotation.Id;
import com.gdk.jdbc.annotation.Table;
import com.gdk.jdbc.config.CompositeDatabaseConfiguration;
import com.gdk.jdbc.config.ConfigurationManager;
import com.gdk.jdbc.config.DatabaseConfiguration;
import com.gdk.jdbc.transaction.TransactionCallback;
import com.gdk.jdbc.transaction.TransactionController;
import com.gdk.util.Assert;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * @author: ShiYu
 * @createtime: 2019/3/11 15:27
 * @description:
 */
public final class GdkUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(GdkUtils.class);

    private static final int NUMBER_TWO = 2;

    /**
     * hashmap默认负载因子
     */
    public static final double DEFAULT_LOAD_FACTOR = 0.75;

    private GdkUtils() {

    }

    /**
     * 默认的批量提交参数
     */
    public static final int DEFAULT_BATCH_NUM = 5000;


    /**
     * 批量添加数据，将beanlist数据批量注册到数据库中
     *
     * @param beanList bean对象列表
     * @throws SQLException
     */
    public static <T> void batchInsert(JdbcHandler jdbcHandler, List<T> beanList, boolean idFlag) throws SQLException {
        if (CollectionUtil.isEmpty(beanList)) {
            //TODO 参数异常
            throw new BusinessException(ErrorCode.of(ErrorCode.ErrorType.B0000));
        }
        T obj = CollectionUtil.getFirst(beanList);
        jdbcHandler.batchUpdate(getInsertSql(obj, idFlag), beanList, new BatchPreparedStatementSetter<T>() {
            @Override
            public void setValues(PreparedStatement preparedStatement, T obj) throws SQLException {
                GdkUtils.setPreparedStatementValues(preparedStatement, obj, getParamFieldNames(obj, idFlag));
            }
        }, GdkUtils.DEFAULT_BATCH_NUM);
    }


    /**
     * 根据bean对象生成添加单表的sql语句
     *
     * @param obj    bean对象
     * @param idFlag true 生成的sql包含id,false则不包含id,此参数用于解决id自动生成无需添加id问题
     * @return INSERT语句
     */
    private static String getInsertSql(Object obj, boolean idFlag) {

        //解析表名
        if (!obj.getClass().isAnnotationPresent(Table.class)) {
            //TODO 解析不到表名
            throw new BusinessException(ErrorCode.of(ErrorCode.ErrorType.B0000));
        }
        Table table = obj.getClass().getAnnotation(Table.class);
        String tableName = table.value();
        if (StringUtils.isBlank(tableName)) {
            //TODO 解析不到表名
            throw new BusinessException(ErrorCode.of(ErrorCode.ErrorType.B0000));
        }


        //解析字段
        List<String> fieldNames = getParamFieldNames(obj, idFlag);

        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ").append(tableName).append("(");
        StringBuilder paramBuilder = new StringBuilder("(");
        for (String ename : fieldNames) {
            sqlBuilder.append("\"").append(ename).append("\"").append(",");
            paramBuilder.append("?").append(",");
        }
        sqlBuilder.deleteCharAt(sqlBuilder.lastIndexOf(",")).append(") ").append("values ");
        paramBuilder.deleteCharAt(paramBuilder.lastIndexOf(",")).append(")");
        return sqlBuilder.append(paramBuilder).toString();

    }

    @NotNull
    private static List<String> getParamFieldNames(Object obj, boolean idFlag) {
        Field[] fields = obj.getClass().getDeclaredFields();
        //解析字段列表
        List<String> fieldNames = new ArrayList<>();
        for (Field field : fields) {
            //生成的sql包含id
            Id id = field.getAnnotation(Id.class);
            if (null != id) {
                if (idFlag) {
                    fieldNames.add("id");
                }
                continue;
            }
            //判断该字段是否是参数
            Column batchColumn = field.getAnnotation(Column.class);
            if (null != batchColumn && batchColumn.insertable() && batchColumn.updatable()) {
                fieldNames.add(batchColumn.name());
            } else {
                //无注解使用默认字段名
                fieldNames.add(field.getName());
            }
        }

        if (fieldNames.isEmpty()) {
            //TODO 解析不到字段
            throw new BusinessException(ErrorCode.of(ErrorCode.ErrorType.B0000));
        }
        return fieldNames;
    }

    /**
     * 批量设置preparedStatement
     *
     * @param preparedStatement preparedStatement 对象
     * @param obj               bean对象
     * @param paramNames        参数名列表，对应bean对象中@column注解的属性名，按照preparedStatement参数顺序传入
     * @throws SQLException
     */
    public static void setPreparedStatementValues(PreparedStatement preparedStatement, Object obj, List<String> paramNames) throws SQLException {
        if (CollectionUtil.isEmpty(paramNames)) {
            return;
        }

        Field[] fields = obj.getClass().getDeclaredFields();
        if (null == fields) {
            return;
        }
        //解析字段
        Map<String, Object> paramMap = new HashMap<>();
        for (Field field : fields) {

            //判断该字段是否是参数
            Column batchColumn = field.getAnnotation(Column.class);
            if (null != batchColumn) {
                field.setAccessible(true);
                try {
                    paramMap.put(StringUtils.upperCase(batchColumn.name()), field.get(obj));
                } catch (IllegalAccessException e) {
                    //TODO
                }
            }

            if (null != field.getAnnotation(Id.class) && CollectionUtil.contains(paramNames, "id")) {
                field.setAccessible(true);
                try {
                    paramMap.put("ID", field.get(obj));
                } catch (IllegalAccessException e) {
                    //TODO
                }

            }
        }


        for (int i = 0; i < paramNames.size(); i++) {
            preparedStatement.setObject(i + 1, paramMap.get(StringUtils.upperCase(paramNames.get(i))));
        }
    }


    /**
     * 构造gdk动态参数map
     *
     * @param args 按照参数名，参数值，参数名，参数值顺序传入
     * @return 构造后的参数
     */
    public static Map<String, Object> newParam(Object... args) {
        int argLength = args.length;
        if (argLength % NUMBER_TWO != 0) {
            throw new IllegalArgumentException("param length must be even");
        }
        int mapCapacity = (int) Math.ceil(argLength / DEFAULT_LOAD_FACTOR);
        Map paramMap = new HashMap(mapCapacity);
        for (int i = 0; i < argLength; i += NUMBER_TWO) {
            paramMap.put(args[i], args[i + 1]);
        }
        return paramMap;
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static int currentTime() {
        return (int) (System.currentTimeMillis());
    }

    /**
     * 获取批量执行sql的参数,使用此方法获取参数时，必须保证SQL中参数顺序和BEAN中定义的属性顺序相同,并且参数字段使用了
     * BatchColumn注解方能使用
     *
     * @param list   数据列表
     * @param tClass 数据bean类型
     * @param <T>    数据bean
     * @return
     */
    public static <T> Object[][] getParams(List<T> list, Class<T> tClass) {
        Field[] fields = tClass.getDeclaredFields();
        if (null == fields) {
            return new Object[][]{};
        }
        List<Field> fieldList = getClassFields(tClass);
        Object[][] params = new Object[list.size()][fieldList.size()];
        reformParams(list, fieldList, params);
        return params;
    }

    /**
     * 获取批量执行sql的参数,使用此方法获取参数时，必须保证SQL中参数顺序和BEAN中定义的属性顺序相同,并且参数字段使用了
     * BatchColumn注解方能使用
     *
     * @param t      数据信息
     * @param tClass 数据bean类型
     * @param <T>    数据bean
     * @return
     */
    public static <T> Object[] getParam(T t, Class<T> tClass) {
        List<Field> fieldList = getClassFields(tClass);
        Object[] params = new Object[fieldList.size()];
        reformParam(t, fieldList, params);
        return params;
    }

    private static <T> List<Field> getClassFields(Class<T> tClass) {
        Field[] fields = tClass.getDeclaredFields();
        if (null == fields) {
            return Collections.emptyList();
        }
        List<Field> fieldList = new ArrayList<>();
        for (Field field : fields) {
            //判断该字段是否是参数
            BatchColumn batchColumn = field.getAnnotation(BatchColumn.class);
            if (null != batchColumn) {
                fieldList.add(field);
            }
        }
        return fieldList;
    }

    public static <T> Map<String, Object> getUpdateParam(T t, Class<T> tClass) {
        Map<String, Object> params = new HashMap<>();
        List<Field> fieldList = getClassFields(tClass);
        for (Field field : fieldList) {
            ColumnMap columnMap = field.getAnnotation(ColumnMap.class);
            Object value = null;
            try {
                field.setAccessible(true);
                value = field.get(t);
                if (null == value) {
                    continue;
                }
                if (null != columnMap) {
                    params.put(columnMap.alias()[0], value);
                } else {
                    params.put(StringUtils.upperCase(field.getName()), value);
                }
            } catch (IllegalAccessException e) {
                LOGGER.error("parse sql param error", e);
                //TODO
                throw new BusinessException(ErrorCode.of(ErrorCode.ErrorType.B0000));

            }
        }
        return params;
    }

    /**
     * 执行一个单数据源普通事务处理逻辑,
     * 解决gdk3自带的事务模板存在无法向外部抛出异常问题
     * <p>
     * <p>
     * * // 需要返回值的事务执行回调
     * GdkUtils.executeWithTransaction(new TransactionCallback<Object>() {
     * <code>@Override</code>
     * public Object doInTransaction() throws Exception {
     * // 业务代码...
     * // 如果业务代码出现异常，不要内部完全捕获掉，要同时向上抛出
     * // 以便能检测到异常并进行回滚。
     * <p>
     * return null;
     * }
     * });
     * </pre>
     * <pre>
     * // 不需要返回值的事务执行回调
     * GdkUtils.executeWithTransaction(new TransactionCallbackWithoutResult() {
     * <code>@Override</code>
     * public void doInTransactionWithoutResult() throws Exception {
     * // 业务代码...
     * // 如果业务代码出现异常，不要内部完全捕获掉，要同时向上抛出
     * // 以便能检测到异常并进行回滚。
     * }
     * });
     * </pre>
     *
     * @param transactionCallback 事务执行回调
     * @return 事务执行结束后可能返回的自定义结果
     * @throws Exception
     */
    public static <T> T executeWithTransaction(TransactionCallback<T> transactionCallback) throws Exception {
        Assert.notNull(transactionCallback, "Argument `TransactionCallback` is required.");
        T result;
        try {
            TransactionController.beginTransaction();
            result = transactionCallback.doInTransaction();
            TransactionController.commitTransaction();
        } finally {
            if (TransactionController.hasBegined()) {
                TransactionController.closeTransaction();
            } else {
                LOGGER.error("Failed to rollback the transactional Connection, because cannot find any `Connection` in current transaction!");
            }
        }
        return result;
    }

    //获取参数值
    private static <T> void reformParams(List<T> list, List<Field> fieldList, Object[][] params) {
        for (int i = 0; i < list.size(); i++) {
            T obj = list.get(i);
            for (int j = 0; j < fieldList.size(); j++) {
                Field field = fieldList.get(j);
                field.setAccessible(true);
                try {
                    params[i][j] = field.get(obj);

                } catch (IllegalAccessException e) {
                    LOGGER.error("parse sql param error", e);
                    //TODO
                    throw new BusinessException(ErrorCode.of(ErrorCode.ErrorType.B0000));
                }
            }
        }
    }

    //获取参数值
    private static <T> void reformParam(T t, List<Field> fieldList, Object[] params) {
        for (int j = 0; j < fieldList.size(); j++) {
            Field field = fieldList.get(j);
            field.setAccessible(true);
            try {
                params[j] = field.get(t);
            } catch (IllegalAccessException e) {
                //TODO
                LOGGER.error("parse sql param error", e);
                throw new BusinessException(ErrorCode.of(ErrorCode.ErrorType.B0000));

            }
        }
    }

    /**
     * 获取批量执行sql的参数,使用此方法获取参数时，必须保证SQL中参数顺序和BEAN中定义的属性顺序相同方能使用
     *
     * @param list   数据列表
     * @param tClass 数据bean类型
     * @param <T>    数据bean
     * @return
     */
    public static <T> Object[][] getParams(List<T> list, Class<T> tClass, String[] filterFields) {
        Field[] fields = tClass.getDeclaredFields();
        if (null == fields) {
            return new Object[][]{};
        }
        List<Field> fieldList = new ArrayList<>();
        for (Field field : fields) {
            if ("private".equals(Modifier.toString(field.getModifiers()).trim())) {
                String fieldName = field.getName();
                if (ArrayUtils.isNotEmpty(filterFields) && ArrayUtils.contains(filterFields, fieldName)) {
                    continue;
                }
                fieldList.add(field);
            }
        }
        Object[][] params = new Object[list.size()][fieldList.size()];
        reformParams(list, fieldList, params);
        return params;
    }

    /**
     * 获取GDK的datasource的数据库连接信息
     *
     * @return
     * @throws Exception
     */
    public static DatabaseConfiguration getDbInfo(String dataSourceName) throws Exception {
        Map<String, CompositeDatabaseConfiguration> configurations = ConfigurationManager.getManager().getCompositeConfigurations();
        cn.hutool.core.lang.Assert.notEmpty(configurations, "datasource.xml中的连接属性不存在，请检查");
        cn.hutool.core.lang.Assert.isTrue(configurations.containsKey(dataSourceName), "database.xml中数据库oa链接属性不存在，请检查");
        for (Map.Entry<String, CompositeDatabaseConfiguration> entry : configurations.entrySet()) {
            String key = entry.getKey();
            if (!StringUtils.equalsIgnoreCase(key, dataSourceName)) {
                continue;
            }
            CompositeDatabaseConfiguration configuration = configurations.get(key);
            return configuration.getMasterConfiguration();
        }
        throw new IllegalArgumentException("数据源配置错误");
    }

}