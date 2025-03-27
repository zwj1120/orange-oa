package com.xcr.orange.oa.core.db;

import com.xcr.orange.oa.util.GdkUtils;
import com.gdk.jdbc.config.DatabaseConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 数据库自动刷脚本配置类
 *
 * @author yjx
 */
@Configuration
public class FlywayConfig {

    /**
     * gdk数据源名
     */
    private static final String DATASOURCE_NAME = "exam";

    /**
     * 是否启用
     */
    @Value("${exam.flyway.enable}")
    private String flywayEnable;

    /**
     * 脚本路径
     */
    @Value("${exam.flyway.locations}")
    private String flywayLocations;

    /**
     * 版本表名
     */
    @Value("${exam.flyway.table}")
    private String flywayTable;

    /**
     * 基线版本
     */
    @Value("${exam.flyway.baseline-version}")
    private String flywayBaselineVersion;

    public FlywayConfig() {
    }

    @Bean
    public Flyway flyway() throws Exception {
        if (!Boolean.parseBoolean(this.flywayEnable)) {
            return null;
        }
        DatabaseConfiguration dbInfo = GdkUtils.getDbInfo(DATASOURCE_NAME);
        FluentConfiguration configuration = Flyway.configure()
                .table(this.flywayTable)
                .encoding("UTF-8")
                .cleanDisabled(true)
                //设置执行sql脚本前的版本为基线版本
                .baselineOnMigrate(true)
                //flyway默认单个sql文件为一个事务，设置group属性为true可将目录下所有待执行sql文件的执行过程设置为一个事务
                .group(true)
                //发生错误时所有已经执行的sql会被回滚
                .cleanOnValidationError(true)
                .locations(this.flywayLocations)
                //.callbacks(new DbCallback())
                .dataSource(dbInfo.getDriverUrl(), dbInfo.getUser(), dbInfo.getPassword());
        //设置基线版本
        if (StringUtils.isNotEmpty(this.flywayBaselineVersion)) {
            configuration.baselineVersion(this.flywayBaselineVersion);
        }
        Flyway flyway = configuration.load();
        flyway.migrate();
        return flyway;
    }

}
