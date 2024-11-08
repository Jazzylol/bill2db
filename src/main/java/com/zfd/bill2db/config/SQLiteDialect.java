package com.zfd.bill2db.config;

import org.hibernate.dialect.DatabaseVersion;
import org.hibernate.dialect.SimpleDatabaseVersion;
import org.hibernate.dialect.DatabaseVersion.BaseVersion;
import org.hibernate.dialect.DatabaseVersion.SimpleVersion;
import org.hibernate.dialect.SimpleDatabaseVersion;
import org.hibernate.dialect.Dialect;

import java.sql.Types;

public class SQLiteDialect extends org.hibernate.dialect.SQLiteDialect {
    
    public SQLiteDialect() {
        super(DatabaseVersion.make(3, 45));
        
        // 注册数据类型映射
        getDefaultProperties().setProperty("hibernate.dialect", "com.zfd.bill2db.config.SQLiteDialect");
    }

    @Override
    public boolean supportsIdentityColumns() {
        return true;
    }

    @Override
    public boolean hasDataTypeInIdentityColumn() {
        return false;
    }

    @Override
    public String getIdentityColumnString() {
        return "integer";
    }

    @Override
    public String getIdentitySelectString() {
        return "select last_insert_rowid()";
    }
} 