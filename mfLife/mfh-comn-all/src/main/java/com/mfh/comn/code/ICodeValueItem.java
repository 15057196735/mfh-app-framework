/*
 * 文件名称: ICodeValueItem.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-3-19
 * 修改内容: 
 */
package com.mfh.comn.code;


/**
 * 编码项接口
 * T: 编码值类型
 * @return
 * @author zhangyz created on 2012-5-29
 */
public interface ICodeValueItem<T> extends ICodeItem<T>{    
    public static int NODE_VIEW_OK = 0;//已经可见
    public static int NODE_VIEW_NO = -1;//需要进一步确认。
    public static int NODE_VIEW_CONTINUE = 1;//需要进一步确认（子节点有可见的）
    
    /**
     * 该编码串在整个编码串中是哪一层，一般是levelName
     * @return
     * @author zhangyz created on 2013-1-25
     */
    public String getKind();
    
    
    public void setKind(String levelName);
    
    /**
     * 获取对应的统一编码串
     * @return
     * @author zhangyz created on 2012-7-6
     */
    public UnionCode getUnionCode();

}
