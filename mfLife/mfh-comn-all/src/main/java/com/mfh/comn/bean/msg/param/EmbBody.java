/*
 * 文件名称: JsonParam.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-10-25
 * 修改内容: 
 */
package com.mfh.comn.bean.msg.param;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 技术类型为json时传递的数据基类(MsgConstant.MSG_TECHTYPE_JSON)
 * @author zhangyz created on 2014-10-25
 */
public interface EmbBody extends java.io.Serializable{
    public static String SIGN_LINE = "\n----------\n";
    
    /**
     * 消息桥消息体为json格式时的具体序列化类型-class名字
     * @return
     * @author zhangyz created on 2014-10-25
     */
    //public String hintEmbClassType();

    /**
     * 附加签名
     * @param sign
     * @author zhangyz created on 2014-10-25
     */
    public void attachSignName(String name);
    
    /**
     * 是否已经附加签名
     * @return
     * @author zhangyz created on 2014-12-2
     */
    public boolean haveSignName();
    
    /**
     * 获取消息技术类型
     * @return
     * @author zhangyz created on 2014-10-30
     */
    @JSONField(serialize=false)
    public String getType();
}
