/*
 * 文件名称: Constant.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-10-13
 * 修改内容: 
 */
package com.mfh.comn.bean.msg;

import java.util.ArrayList;
import java.util.List;


/**
 * 消息类相关常量
 * @author zhangyz created on 2014-10-13
 */
public class MsgConstant {

    public static Integer MSG_KIND_0 = 0;//默认属于聊天类
    public static Integer MSG_KIND_1 = 1;//管理类

    /**
     * 消息业务类型
     */
    public static int MSG_BIZTYPE_NOTIFY = 0;//通知消息,同原来定义。BizMsgType.SYS
    public static int MSG_BIZTYPE_CHAT = 1;//个人之间聊天消息,其对应的会话类型为SESSION_TYPE_P2PCHART
    public static int MSG_BIZTYPE_ASK = 2;//客户咨询消息,例如业主通过微信、App发的消息
    public static int MSG_BIZTYPE_CS = 3;//客服响应消息，例如所有客服人员、机器人对客户咨询消息响应的消息，即管家会话中的消息
    public static int MSG_BIZTYPE_CMD = 4;//命令、菜单消息
    public static int MSG_BIZTYPE_REGISTER = 5;//客户关注渠道消息
    public static int MSG_BIZTYPE_UNREGISTER = 6;//客户取消关注渠道消息
    public static int MSG_BIZTYPE_SCANCODE = 7;//扫描二维码消息
    
    //下面两个相当于业务管理消息，其实无需发送到物理端点，到适配器一层即可。
    public static int MSG_BIZTYPE_BIND = 8;//端点绑定消息
    public static int MSG_BIZTYPE_UNBIND = 9;//端点解绑消息
    
    public static int MSG_BIZTYPE_ALIVE = 10;//心跳消息    
    public static int MSG_BIZTYPE_MANAGER = 100;// 系统内部控制/内部命令消息(备用,不同于上述业务上的命令消息)


    public static int MSG_BIZTYPE_MFPARGER_PEISONG_NOTIFY = 1002;//满分小伙伴送达通知
    public static int MSG_BIZTYPE_MFPARGER_RECEIVE_ORDER = 1003;//满分小伙伴接单通知
    public static int MSG_BIZTYPE_EVALUATE_ORDER = 1004;//用户确认收货,评价订单
    
    /**
     * 消息技术类型
     */
    public static final String MSG_TECHTYPE_RAW = "raw";//简单类型，即String
    public static final String MSG_TECHTYPE_TEXT = "text";//文本消息
    public static final String MSG_TECHTYPE_VOICE = "voice";//声音消息
    public static final String MSG_TECHTYPE_TUWEN = "tuwen";//图文
    public static final String MSG_TECHTYPE_IMAGE = "image";//图片消息
    public static final String MSG_TECHTYPE_EMOTION = "emotion";//表情/符号
    public static final String MSG_TECHTYPE_POS = "position";//位置消息
    public static final String MSG_TECHTYPE_VIDEO = "video";//视频消息
    public static final String MSG_TECHTYPE_CARD = "card";//名片消息
    public static final String MSG_TECHTYPE_LINK = "link";//链接消息    
    public static final String MSG_TECHTYPE_JSON = "json";//对象消息    
    public static final String MSG_TECHTYPE_TEMP = "templete";//类型就是：TemplateParam
    public static final String MSG_TECHTYPE_RESOURCE = "resource";//类型就是：ResourceParam

    /**
     * 会话类型范围。随业务类型而定，目前可分为三大类业务会话，物业、商家和满分，每种又可以分为三个变种。
     */    
    public static final Integer SESSION_TYPE_P2PCHART = 0;//两端会话,如好友会话、同事会话
    public static final Integer SESSION_TYPE_GROUP = 2;//群组会话       
    public static final Integer SESSION_TYPE_GUANJIA_UNBIND = 1;//客户关注后首先置入未绑定客服会话，为简单起见 不再如下面再未区别初始和临时团队。
    public static final Integer SESSION_TYPE_GUANJIA_NORMAL = 101;//已绑定客服团队会话-初始团队  //final Integer GUANJIA_TMP = 102;//已绑定客服团队会话-加入临时成员后的团队
    
    public static List<Integer> unBindKinds = new ArrayList<Integer>();
    public static List<Integer> bindKinds = new ArrayList<Integer>();
    
    //客户绑定类型
    public static final Integer CUSTOM_TYPE_UNBIND = 0;//未绑定,等同于GUANJIA_UNBIND
    public static final Integer CUSTOM_TYPE_BIND = 1;  //已关联,等同于GUANJIA_NORMAL
    public static final Integer CUSTOM_TYPE_UNRELATION = 2; //未绑定或已绑定未关联(还未成为会员，包括unbind和 binded但extparam为空)
    
    //会话业务类型--内置的
    public static final Integer SBY_GUANJIA_NORMAL = 0;//初始会话
    public static final Integer SBY_GUANJIA_EXTEND = 1;//扩展会话,-加入临时成员后的团队    
    
    public static final Integer SBY_P2P_FRIEND = 0;//好友会话
    public static final Integer SBY_P2P_WORKER = 1;//同事会话
    public static final Integer SBY_P2P_MACHINE = 2;//客服机器与粉丝的会话
    
    public static final Integer SBY_GROUP_NORMAL = 0;//普通好友群组会话,无须tag_one
    public static final Integer SBY_GROUP_COMPANY = 1;//同事群组会话,其tag_one是公司编号或部门编号
    public static final Integer SBY_GROUP_SERVICE = 2;//服务群组会话，其tag_one是订单号，如针对订单,该群中的消息业务类型都是（MSG_BIZTYPE_ASK=2）
    
    /*public static final Integer SHANGJIA_UNBIND = 10;//客户关注后首先置入商家未绑定客服会话。
    public static final Integer SHANGJIA_NORMAL = 11;//商家客服团队会话-初始团队
    public static final Integer SHANGJIA_TMP = 12;//商家客服团队会话-加入临时成员后的团队
    
    public static final Integer MFH_UNBIND = 13;//客户关注后首先置入满分未绑定客服会话。
    public static final Integer MFH_NORMAL = 14;//满分客服团队会话-初始团队
    public static final Integer MFH_TMP = 15;//满分客服团队会话-加入临时成员后的团队
*/    
    static {
        unBindKinds.add(SESSION_TYPE_GUANJIA_UNBIND);        
        bindKinds.add(SESSION_TYPE_GUANJIA_NORMAL);
        //bindKinds.add(GUANJIA_TMP);
    }
    
    /**
     * 是否属于未绑定类会话
     * @param st
     * @return
     * @author zhangyz created on 2014-11-14
     */
    public static Boolean isUnbind(Integer st) {
        return unBindKinds.contains(st);
    }
    
    /**
     * 是否属于已绑定类会话
     * @param st
     * @return
     * @author zhangyz created on 2014-11-14
     */
    public static Boolean isbind(Integer st) {
        return bindKinds.contains(st);
    }
    
    private static Boolean isAllBindInner(List<Integer> sts, List<Integer> sessionKinds) {
        Boolean ret = null;
        for (Integer st : sts) {
            boolean bTmp = sessionKinds.contains(st);
            if (ret == null)
                ret = bTmp;
            else {
                if (!(ret && bTmp))
                    throw new RuntimeException("传入的会话类型不属于同一类!");
            }            
        }
        return ret;
    }
    
    /**
     * 获取一致的会话类型大类
     * @param sts
     * @return 1:已绑定类； 0:未绑定类, null: 普通类，无客服含义
     * @throws //若两种都包含，抛出异常
     * @author zhangyz created on 2014-11-14
     */
    public static Integer isAllBind(List<Integer> sts) {
        Boolean ret = isAllBindInner(sts, bindKinds);
        if (ret == null)
            ret = isAllBindInner(sts, unBindKinds);
        else
            return ret ? CUSTOM_TYPE_BIND : CUSTOM_TYPE_UNBIND;            
        if (ret == null)
            return null;
        else
            return ret ? CUSTOM_TYPE_UNBIND : CUSTOM_TYPE_BIND;
    }
    
    /**
     * 判断bind类型，若没有则返回null
     * @param sts
     * @return
     * @author zhangyz created on 2015-1-22
     */
    public static Integer getBind(Integer sts) {
        if (bindKinds.contains(sts))
            return CUSTOM_TYPE_BIND;        
        if (unBindKinds.contains(sts))
            return CUSTOM_TYPE_UNBIND;        
        return null;
    }
    
    //所有业务定义的特殊类型会话从下面值开始
    public static final Integer BASE_BIZ_TYPE = 1000;
    
    public static class MsgTitle{
    	public static final String SYS = "系统通知";
    }
    
}
