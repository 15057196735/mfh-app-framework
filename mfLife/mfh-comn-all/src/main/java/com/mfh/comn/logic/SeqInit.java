package com.mfh.comn.logic;

import org.apache.commons.lang3.StringUtils;
import com.mfh.comn.bean.Pair;
import com.mfh.comn.config.IConfiguration;
import com.mfh.comn.utils.UuidUtil;

/**
 * 序列、uuid等的初始化
 * 
 * @author zhangyz created on 2012-8-13
 * @since Framework 1.0
 */
public class SeqInit {
    /**
     * 单位是万
     * 
     * @author zhangyz created on 2013-6-7
     * @since Framework 1.0
     */
    public enum SeqArea{
        one, ten, hundred, thousand, wan, shiWan, million, qianWan, str
    }
    
    public static String CENTER_DB_ID = "0000";// 服务端数据库标示
    public static String UCONFIG_GLP_DBID_CHAR = "db.idchar";//本地(客户端/手机端/服务器端)数据库编号参数
    public static String UCONFIG_GLP_DBID_P0 = "db.idp0";//本地数据库起始编号1参数
    public static String UCONFIG_GLP_DBID_P1 = "db.idp1";//本地数据库起始编号2参数
    public static String UCONFIG_GLP_DBID_P2 = "db.idp2";
    public static String UCONFIG_GLP_DBID_P3 = "db.idp3";
    public static String UCONFIG_GLP_DBID_P4 = "db.idp4";
    public static String UCONFIG_GLP_DBID_P5 = "db.idp5";
    public static String UCONFIG_GLP_DBID_P6 = "db.idp6";
    public static String UCONFIG_GLP_DBID_P7 = "db.idp7";

    //序列返回
    public static final int AREA_ID_one = 10000;//一万容量
    public static final int AREA_ID_ten = 100000;//十万容量
    public static final int AREA_ID_hundred = 1000000;//百万容量
    public static final long AREA_ID_thousand = 10000000;//千万容量 
    public static final long AREA_ID_wan = 100000000;//亿容量 
    public static final long AREA_ID_shiWan = 1000000000;//十亿容量 
    public static final long AREA_ID_million = 10000000000L;//百亿容量 
    public static final long AREA_ID_qianWan = 100000000000L;//千亿容量 
    
    //本地序列的统一初始值，用于分布式数据库
    public static int LOCAL_ID_one;//一万容量的本地库序列起始值
    public static int LOCAL_ID_ten;//十万容量的本地库序列起始值
    public static int LOCAL_ID_hundred;//百万容量的本地库序列起始值
    public static long LOCAL_ID_thousand;//千万容量的本地库序列起始值
    public static long LOCAL_ID_wan;//亿容量的本地库序列起始值
    public static long LOCAL_ID_shiWan;//十亿容量的本地库序列起始值
    public static long LOCAL_ID_million;//百亿容量的本地库序列起始值
    public static long LOCAL_ID_qianWan;//千亿容量的本地库序列起始值
    
    /**
     * 初始化本地数据库编号
     * 
     * @author zhangyz created on 2012-11-3
     */
    public static void initUuid(IConfiguration uconfig){
        //初始化本地服务对应的数据库全局唯一ID
        String glpDbId = uconfig.getString(UCONFIG_GLP_DBID_CHAR, CENTER_DB_ID);
        com.mfh.comn.utils.UuidUtil.LOCAL_ID = glpDbId;
    }
    
    /**
     * 初始化序列号
     * 
     * @author zhangyz created on 2012-9-1
     */
    public static void init(IConfiguration uconfig){        
        if (StringUtils.isBlank(UuidUtil.LOCAL_ID))
            initUuid(uconfig);
        
        LOCAL_ID_one = Integer.parseInt(uconfig
            .getString(UCONFIG_GLP_DBID_P0, "10000"));//默认从1万开始
        
        LOCAL_ID_ten = Integer.parseInt(uconfig
            .getString(UCONFIG_GLP_DBID_P1, "100000"));//默认从10万开始
        
        LOCAL_ID_hundred = Integer.parseInt(uconfig
            .getString(UCONFIG_GLP_DBID_P2, "1000000"));//默认从1百万开始
        
        LOCAL_ID_thousand = Integer.parseInt(uconfig
            .getString(UCONFIG_GLP_DBID_P3, "10000000"));//默认从1千万开始
        
        LOCAL_ID_wan = Integer.parseInt(uconfig
            .getString(UCONFIG_GLP_DBID_P4, "100000000")); //默认从1亿开始
        
        LOCAL_ID_shiWan = Long.parseLong(uconfig
            .getString(UCONFIG_GLP_DBID_P5, "1000000000")); //默认从10亿开始
        
        LOCAL_ID_million = Long.parseLong(uconfig
            .getString(UCONFIG_GLP_DBID_P6, "10000000000")); //默认从100亿开始
        
        LOCAL_ID_qianWan = Long.parseLong(uconfig
            .getString(UCONFIG_GLP_DBID_P7, "100000000000")); //默认从1000亿开始
    }
    
    /**
     * 根据指定的键值范围得出序列起始值
     * @param seqArea
     * @return v1:start; v2:end. 若都为null代表使用uuid
     * @author zhangyz created on 2013-6-15
     */
    public static Pair<Long, Long> getSeqLong(SeqArea seqArea) {
        Long seqStart = (long)SeqInit.LOCAL_ID_hundred;
        Long seqLong = null, seqEnd = null;
        if (seqArea != null) {
            if (seqArea.equals(SeqArea.qianWan)) {
                seqLong = SeqInit.AREA_ID_qianWan;
                seqStart = SeqInit.LOCAL_ID_qianWan;
            }
            else if (seqArea.equals(SeqArea.million)) {
                seqLong = SeqInit.AREA_ID_million;
                seqStart = SeqInit.LOCAL_ID_million;
            }
            else if (seqArea.equals(SeqArea.shiWan)) {
                seqLong = SeqInit.AREA_ID_shiWan;
                seqStart = SeqInit.LOCAL_ID_shiWan;
            }
            else if (seqArea.equals(SeqArea.wan)) {
                seqLong = SeqInit.AREA_ID_wan;
                seqStart = SeqInit.LOCAL_ID_wan;
            }
            else if (seqArea.equals(SeqArea.str)) {
                seqLong = null;
                seqStart = null;
            }
            else if (seqArea.equals(SeqArea.thousand)) {
                seqLong = SeqInit.AREA_ID_thousand;
                seqStart = SeqInit.LOCAL_ID_thousand;
            }
            else if (seqArea.equals(SeqArea.hundred)) {
                seqLong = (long)SeqInit.AREA_ID_hundred;
                seqStart = (long)SeqInit.LOCAL_ID_hundred;
            }
            else if (seqArea.equals(SeqArea.ten)) {
                seqLong = (long)SeqInit.AREA_ID_ten;
                seqStart = (long)SeqInit.LOCAL_ID_ten;
            }
            else if (seqArea.equals(SeqArea.one)) {
                seqLong = (long)SeqInit.AREA_ID_one;
                seqStart = (long)SeqInit.LOCAL_ID_one;
            }
            else
                throw new RuntimeException("不支持的容量范围类型:" + seqArea);
            
            if (seqLong != null) {
                seqEnd = seqStart + seqLong;
            }
            return new Pair<Long, Long> (seqStart, seqEnd);
        }   
        else
            return new Pair<Long, Long>(null, null);
    }
}
