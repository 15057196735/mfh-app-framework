/*
 * 文件名称: SequenceService.java
 * 版权信息: Copyright 2001-2011 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: LuoJingtian
 * 修改日期: 2011-12-28
 * 修改内容: 
 */
package com.mfh.comna.comn.seq;

/**
 * 序列业务接口
 * @author <a href="mailto:luojt@zjcds.com">LuoJingtian</a> created on 2011-12-28
 * @since SHK BMP 1.0
 */
public interface SequenceService {
    /**
     * 获取随机的UUID作为序列
     * @return UUID
     * @author LuoJingtian created on 2012-1-16 
     * @since SHK BMP 1.0
     */
    String getUUID();
    
    /**
     * 获取序列
     * @param sequeceName 序列名称
     * @author LuoJingtian created on 2011-12-28 
     * @since SHK BMP 1.0
     */
    String getNextSeqStringValue(String sequeceName);
    
    /**
     * 获取字符序列
     * @param sequeceName 序列名称
     * @param length 序列长度
     * @author LuoJingtian created on 2012-1-16 
     * @since SHK BMP 1.0
     */
    String getNextSeqStringValue(String sequeceName, int length);
    
    /**
     * 获取长整形序列
     * @param sequeceName 序列名称
     * @author LuoJingtian created on 2011-12-28 
     * @since SHK BMP 1.0
     */
    long getNextSeqLongValue(String sequeceName);
    
    /**
     * 获取整形序列
     * @param sequeceName 序列名称
     * @author LuoJingtian created on 2011-12-28 
     * @since SHK BMP 1.0
     */
    int getNextSeqIntValue(String sequeceName);
    
    /**
     * 检查指定的序列名是否存在，若不存在，则创建。
     * @param seqName
     * @param minValue 最小值，应大于等于0
     * @param maxValue 最大值, 为空不指定
     * @author zhangyz created on 2012-8-13
     */
    void checkSequence(String seqName, long minValue, Long maxValue);
}
