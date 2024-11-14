package com.zfd.bill2db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;



@Data
@ToString
@NoArgsConstructor
@TableName("transactions")
public class TransactionDO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Date createTime;
    private Date updateTime;
    private String source;
    private String username;
    private Date transTime;
    private String category;
    private String transToName;
    private String transToAccount;
    private String productDesc;
    private String transType;
    private Double amount;
    private String payMethod;
    private String transStatus;
    private String transOrderNo;
    private String merchantOrderNo;
    private String memo;
    private String tags;

}