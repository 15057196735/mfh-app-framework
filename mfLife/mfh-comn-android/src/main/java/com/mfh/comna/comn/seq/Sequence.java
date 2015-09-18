/*
 * 文件名称: Sequence.java
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

import com.mfh.comn.bean.IStringId;

import java.io.Serializable;

/**
 * 序列
 * @author <a href="mailto:luojt@zjcds.com">LuoJingtian</a> created on 2011-12-28
 * @since SHK BMP 1.0
 */
public class Sequence implements Serializable, IStringId{
    /** serialVersionUID */
    private static final long serialVersionUID = -1250838321899082256L;
    
    /** 序列名称 */
    private String id;
    
    /** 当前值 */
    private long sequenceValue = -1;//初始值
    
    /** 最小值 */
    private long minValue;
    
    /** 最大值 */
    private long maxValue = -1;//代表无限制
    
    /** 序列增长步长 */
    private int stepValue = 1;
    
    /** 达到最大值后是否从最小值开始循环 */
    private boolean cycle = false;
    
    /** 默认构造函数 */
    public Sequence() {
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Sequence{")
            .append("  sequenceName=").append(id)
            .append(", sequenceValue=").append(sequenceValue)
            .append(", minValue=").append(minValue)
            .append(", maxValue=").append(maxValue)
            .append(", stepValue=").append(stepValue)
            .append(", cycle=").append(cycle)
            .append('}');
        return sb.toString();
    }
    
    // -------------------------------- 以下为Getter/Setter方法 -------------------------------- //
        
    public void setId(String id) {
        this.id = id;
    }
    
    public long getSequenceValue() {
        return sequenceValue;
    }
    
    public void setSequenceValue(long sequenceValue) {
        this.sequenceValue = sequenceValue;
    }
    
    public long getMinValue() {
        return minValue;
    }
    
    public long getMaxValue() {
        return maxValue;
    }
    
    public int getStepValue() {
        return stepValue;
    }
    
    public void setMinValue(long minValue) {
        this.minValue = minValue;
    }
    
    public void setMaxValue(long maxValue) {
        this.maxValue = maxValue;
    }

    
    public void setStepValue(int stepValue) {
        this.stepValue = stepValue;
    }

    public boolean isCycle() {
        return cycle;
    }
    
    public void setCycle(boolean cycle) {
        this.cycle = cycle;
    }
    
    
}
