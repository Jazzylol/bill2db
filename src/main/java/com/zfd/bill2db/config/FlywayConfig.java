package com.zfd.bill2db.config;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.Flyway;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @Author:zhitao.zzt
 * @Date:2024-11-13 19:01
 * @Description:
 **/

@Configuration
public class FlywayConfig implements ApplicationRunner {
    @Resource
    private DataSourceProperties dataSourceProperties;
    @Resource
    private Flyway flyway;

    @Bean
    @DependsOn("dataSource")
    public Flyway flywayForMySQL() throws Exception{
        if (StringUtils.contains(dataSourceProperties.getUrl(), "mysql")) {
            return Flyway.configure()
                    .driver(StringUtils.isNotBlank(dataSourceProperties.getDriverClassName()) ? dataSourceProperties.getDriverClassName() : "com.mysql.cj.jdbc.Driver")
                    .dataSource(dataSourceProperties.getUrl(), dataSourceProperties.getUsername(), dataSourceProperties.getPassword())
                    .locations("classpath:db/mysql")
                    .load();
        } else if (StringUtils.contains(dataSourceProperties.getUrl(), "sqlite")) {
            return Flyway.configure()
                    .driver(StringUtils.isNotBlank(dataSourceProperties.getDriverClassName()) ? dataSourceProperties.getDriverClassName() : "org.sqlite.JDBC")
                    .dataSource(dataSourceProperties.getUrl(), "", "")
                    .locations("classpath:db/sqlite")
                    .load();
        } else {
            return Flyway.configure()
                    .driver("org.sqlite.JDBC")
                    .dataSource("jdbc:sqlite:/app/bill.db", "", "")
                    .locations("classpath:db/sqlite")
                    .load();
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        flyway.migrate();
    }
}
