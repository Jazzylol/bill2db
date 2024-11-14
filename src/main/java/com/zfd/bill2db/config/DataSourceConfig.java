package com.zfd.bill2db.config;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Configuration
public class DataSourceConfig {

    public static boolean useMysql = false;

    @Resource
    private DataSourceProperties dataSourceProperties;

    @Bean
    public DataSource dataSource() throws Exception {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        if (StringUtils.contains(dataSourceProperties.getUrl(), "mysql")) {
            // 创建 MySQL 数据库
            Connection connection = DriverManager.getConnection(dataSourceProperties.getUrl(), dataSourceProperties.getUsername(), dataSourceProperties.getPassword());
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS bill DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;");
            connection.close();

            dataSource.setDriverClassName(StringUtils.isNotBlank(dataSourceProperties.getDriverClassName()) ? dataSourceProperties.getDriverClassName() : "com.mysql.cj.jdbc.Driver");
            dataSource.setUrl(dataSourceProperties.getUrl());
            dataSource.setUsername(dataSourceProperties.getUsername());
            dataSource.setPassword(dataSourceProperties.getPassword());
            useMysql = true;

        } else if (StringUtils.contains(dataSourceProperties.getUrl(), "sqlite")) {
            File file = new File(dataSourceProperties.getUrl().replace("jdbc:sqlite:", ""));
            if (!file.exists()) {
                file.createNewFile();
            }
            dataSource.setDriverClassName(StringUtils.isNotBlank(dataSourceProperties.getDriverClassName()) ? dataSourceProperties.getDriverClassName() : "org.sqlite.JDBC");
            dataSource.setUrl(dataSourceProperties.getUrl());
        } else {
            File file = new File("/app/bill.db");
            if (!file.exists()) {
                file.createNewFile();
            }
            dataSource.setDriverClassName("org.sqlite.JDBC");
            dataSource.setUrl("jdbc:sqlite:/app/bill.db");
        }


        return dataSource;
    }

}
