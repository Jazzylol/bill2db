package com.zfd.bill2db.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author:zhitao.zzt
 * @Date:2024-11-13 17:19
 * @Description:
 **/
public class OsUtils {


    /**
     * 根据不同的操作系统来获取临时文件目录
     * @return
     */
    public static String getTmpPath() {
        String path = System.getProperty("java.io.tmpdir");


        if (StringUtils.isNotBlank(path)) {
            return path;
        }
        String osName = System.getProperty("os.name");
        if (StringUtils.isBlank(osName)) {
            return "./";
        }
        if (osName.toLowerCase().contains("mac") || osName.toLowerCase().contains("linux")) {
            path = "/tmp";
        }
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            path = "C:\\tmp";
        }
        return path;
    }

}
