package com.zfd.bill2db.utils;

import com.alibaba.fastjson.JSON;
import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @Author:zhitao.zzt
 * @Date:2024-11-13 11:23
 * @Description:
 **/
@Slf4j
public class BillUtils {


    public static String detectedCsvEncoding(File file) {
        if (file == null) {
            return null;
        }
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new InputStreamReader(new FileInputStream(file), "GBK"));
            Iterator<String[]> iterator = csvReader.iterator();
            while (iterator.hasNext()) {
                String[] next = iterator.next();
                if (JSON.toJSONString(next).contains("支出")) {
                    return "GBK";
                }
            }

            csvReader = new CSVReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            iterator = csvReader.iterator();
            while (iterator.hasNext()) {
                String[] next = iterator.next();
                if (JSON.toJSONString(next).contains("支出")) {
                    return "UTF-8";
                }
            }
        } catch (Exception e) {
            log.error("detectedCsvEncoding error", e);
        } finally {
            if (csvReader != null) {
                try {
                    csvReader.close();
                } catch (Exception e) {
                    //pass
                }
            }
        }
        return null;
    }

    public static String detectedBillType(File file) {
        if (file == null || !file.exists() || !file.getName().endsWith(".csv")) {
            return null;
        }


        String encoding = detectedCsvEncoding(file);

        if (StringUtils.isBlank(encoding)) {
            return null;
        }
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new InputStreamReader(new FileInputStream(file), encoding));
            Iterator<String[]> iterator = csvReader.iterator();
            while (iterator.hasNext()) {
                String[] next = iterator.next();
                String text = JSON.toJSONString(next);
                if (text.contains("微信支付账单明细")) {
                    return "wechat";
                } else if (text.contains("支付宝（中国）网络技术有限公司  电子客户回单")) {
                    return "alipay";
                }
            }
        } catch (Exception e) {
            log.error("detectedBillType error", e);
        } finally {
            if (csvReader != null) {
                try {
                    csvReader.close();
                } catch (Exception e) {
                    //pass
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        detectedCsvEncoding(new File("/Users/zhitao.zhang/tmp/ali.csv"));
    }


}
