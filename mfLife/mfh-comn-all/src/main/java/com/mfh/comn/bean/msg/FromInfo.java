/*
 * 文件名称: FromInfo.java
 * 版权信息: Copyright 2013-2015 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2015-3-19
 * 修改内容: 
 */
package com.mfh.comn.bean.msg;


/**
 * 发起方地址信息
 * @author zhangyz created on 2015-3-19
 */
@SuppressWarnings("serial")
public class FromInfo implements java.io.Serializable {
    
    private Long guid; //来源端号,不能为空
    private PhysicalPoint pp = null; //来源渠道物理端点信息,不能为空 
    
    public FromInfo() {
        super();
    }    
    
    public FromInfo(PhysicalPoint pp) {
        super();
        this.pp = pp;
    }

    public FromInfo(Long guid) {
        super();
        this.guid = guid;
    }

    public FromInfo(Long guid, PhysicalPoint fp) {
        super();
        this.guid = guid;
        this.pp = fp;
    }

    public Long getGuid() {
        return guid;
    }
    
    public void setGuid(Long guid) {
        this.guid = guid;
    }
    
    public PhysicalPoint getPp() {
        return pp;
    }
    
    public void setPp(PhysicalPoint pp) {
        this.pp = pp;
    }

    /**
     * 是否来源者信息都具备，物理和逻辑端点都有.
     * 其实业务层借此想判断是否已经绑定过
     * @return
     * @author zhangyz created on 2014-10-29
     */
    public boolean haveAllFrom() {
        if (this.guid != null)
            return true;
        else
            return false;
    }
    
    /**
     * 发送者信息是否为空
     * @return
     * @author zhangyz created on 2015-3-20
     */
    public boolean haveBlank() {
        if (guid == null && (pp == null || pp.getCpt() == null))
            return true;
        else
            return false;
    }
}
