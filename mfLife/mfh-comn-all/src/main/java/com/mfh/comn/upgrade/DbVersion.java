package com.mfh.comn.upgrade;

import java.util.HashMap;
import java.util.Map;

/**
 * 直接在程序里面定义了数据库域的当前最新版本，因为配置文件放在asserts目录下，好像升级不了。
 * Created by Administrator on 14-7-1.
 */
public class DbVersion {
    private static Map<String, Integer> domainVersion = new HashMap<>();

    /**
     * 设置数据库版本号
     * @param domain
     * @param version
     */
    public static void setDomainVersion(String domain, Integer version) {
        domainVersion.put(domain, version);
    }

    /**
     * 获取指定数据库域的最新版本
     * @param domain
     * @return
     */
    public static Integer getDomainVersion(String domain) {
        return domainVersion.get(domain);
    }
}
