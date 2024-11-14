package com.zfd.bill2db.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author:zhitao.zzt
 * @Date:2024-11-13 16:56
 * @Description:
 **/


public class BillSource {

    private static Map<String, BillSource> sourceMap = new HashMap<>();


    public static BillSource ALIPAY = createBillSource("alipay");
    public static BillSource WECHAT = createBillSource("wechat");


    private static BillSource createBillSource(String source) {
        BillSource billSource = new BillSource(source);
        sourceMap.put(source, billSource);
        return billSource;
    }


    private String source;

    private BillSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }
}
