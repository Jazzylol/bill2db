package com.zfd.bill2db.service;

import com.alibaba.fastjson.JSON;
import com.opencsv.CSVReader;
import com.zfd.bill2db.constants.BillSource;
import com.zfd.bill2db.entity.TransactionDO;
import com.zfd.bill2db.mapper.TransactionMapper;
import com.zfd.bill2db.utils.BillUtils;
import com.zfd.bill2db.utils.DateUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * @Author:zhitao.zzt
 * @Date:2024-11-11 11:20
 * @Description:
 **/
@Slf4j
@Service
public class TransactionService {


    @Resource
    private TransactionMapper transactionMapper;


    public boolean bill2db(File file, String tags) {

        if (file == null || !file.exists()) {
            log.warn("try convert bill to db,found bill file is null");
            return false;
        }

        String type = BillUtils.detectedBillType(file);

        boolean result = false;
        if (BillSource.WECHAT.getSource().equals(type)) {
            result = parseWechat(file, tags);
        } else if (BillSource.ALIPAY.getSource().equals(type)) {
            result = parseAlipay(file, tags);
        }
        return result;
    }

    /**
     * 解析alipay账单
     * @param file
     * @param tags
     * @return
     */
    private boolean parseAlipay(File file, String tags) {

        CSVReader csvReader = null;
        try {
            String encoding = BillUtils.detectedCsvEncoding(file);
            if (StringUtils.isBlank(encoding)) {
                log.warn("parseAlipay fail,encoding is null");
                return false;
            }

            csvReader = new CSVReader(new InputStreamReader(new FileInputStream(file), encoding));

            List<String[]> recordArr = csvReader.readAll();
            int beginLine = -1;
            String username = null;

            for (int i = 0; i < recordArr.size(); i++) {
                String[] records = recordArr.get(i);
                if (records.length == 0) {
                    continue;
                }
                String joinString = String.join("", records);

                if (joinString.startsWith("姓名：")) {
                    username = records[records.length - 1].replace("姓名：", "");
                }
                if (joinString.contains("支付宝（中国）网络技术有限公司  电子客户回单")) {
                    beginLine = i;
                    break;
                }
            }
            if (beginLine == -1) {
                log.warn("parseAlipay fail,beginLine is not found");
                return false;
            }

            for (int i = beginLine + 2; i < recordArr.size(); i++) {
                String[] records = recordArr.get(i);
                if (records.length != 13) {
                    continue;
                }
                TransactionDO transactionDO = new TransactionDO();

                String time = records[0];
                String category = records[1];
                String transToName = records[2];
                String transToAccount = records[3];
                String productDesc = records[4];
                String transType = records[5];
                String amount = records[6];
                String payMethod = records[7];
                String transStatus = records[8];
                String transOrderNo = records[9];
                String merchantOrderNo = records[10];
                String memo = records[11];


                transactionDO.setCreateTime(new Date());
                transactionDO.setUpdateTime(new Date());
                transactionDO.setCategory(category);
                transactionDO.setSource(BillSource.ALIPAY.getSource());
                transactionDO.setUsername(username);
                transactionDO.setTransToName(transToName);
                transactionDO.setTransToAccount(transToAccount);
                transactionDO.setProductDesc(productDesc);
                transactionDO.setTransType(transType);
                transactionDO.setAmount(Double.parseDouble(amount));
                transactionDO.setPayMethod(payMethod);
                transactionDO.setTransStatus(transStatus);
                transactionDO.setTransOrderNo(transOrderNo);
                transactionDO.setMerchantOrderNo(merchantOrderNo);
                transactionDO.setMemo(memo);
                transactionDO.setTags(tags);

                Date parse = DateUtils.parse(time, DateUtils.PATTERN_STAND);

                transactionDO.setTransTime(parse);

                transactionMapper.insert(transactionDO);

            }


        } catch (Exception e) {
            log.error("parseAlipay exception ", e);
            return false;
        } finally {
            if (csvReader != null) {
                try {
                    csvReader.close();
                } catch (Exception e) {
                    //pass
                }
            }
        }
        return true;
    }


    /**
     * 解析微信账单
     * @param file
     * @param tags
     * @return
     */
    private boolean parseWechat(File file, String tags) {

        CSVReader csvReader = null;

        try {
            String encoding = BillUtils.detectedCsvEncoding(file);

            if (StringUtils.isBlank(encoding)) {
                log.warn("parseWechat fail,encoding is null");
                return false;
            }
            csvReader = new CSVReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            List<String[]> recordArr = csvReader.readAll();


            int beginLine = -1;

            String username = null;
            for (int i = 0; i < recordArr.size(); i++) {
                String[] records = recordArr.get(i);
                if (records.length == 0) {
                    continue;
                }
                String joinString = String.join("", records);

                if (joinString.startsWith("微信昵称：[")) {
                    username = records[0].replace("微信昵称：[", "").replace("]", "");
                }
                if (joinString.contains("微信支付账单明细列表")) {
                    beginLine = i;
                    break;
                }
            }

            if (beginLine == -1) {
                log.warn("parseWechat fail,beginLine is not found");
                return false;
            }


            for (int i = beginLine + 2; i < recordArr.size(); i++) {

                String[] records = recordArr.get(i);

                if (records.length != 11) {
                    continue;
                }

                TransactionDO transactionDO = new TransactionDO();

                String time = records[0];
                String category = records[1];

                String transToName = records[2];
                String transToAccount = null;
                String productDesc = records[3];
                String transType = records[4];
                String amount = records[5];
                String payMethod = records[6];
                String transStatus = records[7];
                String transOrderNo = records[8];
                String merchantOrderNo = records[9];
                String memo = records[10];


                transactionDO.setCreateTime(new Date());
                transactionDO.setUpdateTime(new Date());
                transactionDO.setSource(BillSource.WECHAT.getSource());
                transactionDO.setUsername(username);
                transactionDO.setCategory(category);
                transactionDO.setTransToName(transToName);
                transactionDO.setTransToAccount(transToAccount);
                transactionDO.setProductDesc(productDesc);
                transactionDO.setTransType(transType);
                transactionDO.setAmount(Double.parseDouble(amount.replace("¥", "")));
                transactionDO.setPayMethod(payMethod);
                transactionDO.setTransStatus(transStatus);
                transactionDO.setTransOrderNo(transOrderNo);
                transactionDO.setMerchantOrderNo(merchantOrderNo);
                transactionDO.setMemo(memo);
                transactionDO.setTags(tags);


                Date parse = DateUtils.parse(time, DateUtils.PATTERN_STAND);

                transactionDO.setTransTime(parse);

                transactionMapper.insert(transactionDO);
            }


        } catch (Exception e) {
            log.error("parseWechat exception ", e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public static void main(String[] args) throws Exception {
        CSVReader csvReader = new CSVReader(new InputStreamReader(new FileInputStream("/Users/zhitao.zhang/tmp/ali.csv"), BillUtils.detectedCsvEncoding(new File("/Users/zhitao.zhang/tmp/ali.csv"))));

        String username = null;

        List<String[]> recordArr = csvReader.readAll();
        for (int i = 0; i < recordArr.size(); i++) {
            String[] records = recordArr.get(i);
            System.out.println(JSON.toJSONString(records));

            if (records.length == 0) {
                continue;
            }
            String joinString = String.join("", records);

            if (joinString.startsWith("姓名：")) {
                username = records[records.length - 1].replace("姓名：", "");
            }
        }

        System.out.println("username:" + username);


    }


}
