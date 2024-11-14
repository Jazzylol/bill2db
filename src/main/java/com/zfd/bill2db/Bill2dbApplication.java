package com.zfd.bill2db;

import com.zfd.bill2db.mapper.TransactionMapper;
import jakarta.annotation.Resource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("com.zfd.bill2db.mapper")
@SpringBootApplication
public class Bill2dbApplication {



    public static void main(String[] args) {
        SpringApplication.run(Bill2dbApplication.class, args);
    }

}
