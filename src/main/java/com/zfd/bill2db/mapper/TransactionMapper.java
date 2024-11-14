package com.zfd.bill2db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zfd.bill2db.entity.TransactionDO;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Map;

/**
 * @Author:zhitao.zzt
 * @Date:2024-11-11 10:40
 * @Description:
 **/
public interface TransactionMapper extends BaseMapper<TransactionDO> {

    @Update("TRUNCATE TABLE transactions")
    void truncateTable();

    @Select("SHOW CREATE TABLE transactions")
    Map<String, Object> getCreateTableSQL4Mysql();

    @Select("SELECT sql FROM sqlite_master WHERE type='table' AND name='transactions'")
    Map<String, Object> getCreateTableSQL4Sqlite();

}
