/*
 * 文件名称: TemplateIdConst.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: jguo
 * 修改日期: 2014-11-11
 * 修改内容: 
 */
package com.mfh.comn.bean.msg;

import java.util.HashMap;
import java.util.Map;

/**
 * 模板消息常量定义
 * @author jguo created on 2014-11-11
 */
public class TemplateIdConst {
	
	public static Long SC_ORDER = 1000L;//订单模板
	public static String  SC_ORDER_TEMP = "商品信息：{{name.DATA}}{{remark.DATA}}";//订单模板
	
	public static Long FD_ORDER = 100L;//快递模板
	public static String  FD_ORDER_TEMP = "{{first.DATA}}\n\n{{name.DATA}}您好！您有一份【{{kuaidiname.DATA}}】快递已经到了物业管理中心，快递单号：【{{num.DATA}}】。请带有效证件到物管中心领取。\n{{remark.DATA}}";//快递模板
	
	public static Long ACCOUNT_RECHARGE = 99L;//充值模板
	public static String  ACCOUNT_RECHARGE_TEMP = "{{first.DATA}}\n\n{{accountType.DATA}}：{{account.DATA}}]\n充值金额：{{amount.DATA}}\n充值状态：{{result.DATA}}\n{{remark.DATA}}";//充值模板
	
	public static Long ACCOUNT_COSUME = 98L;//消费模板
	public static String  ACCOUNT_COSUME_TEMP = "您好，您已成功消费。\n\n{{productType.DATA}}：{{name.DATA}}消费时间：{{time.DATA}}\n{{remark.DATA}}";//消费模板
	
	public static Long ACCOUNT_CHANGE = 97L;//账户变动模板
    public static String  ACCOUNT_CHANGE_TEMP = "{{first.DATA}}\n\n账户：{{account.DATA}}\n时间：{{time.DATA}}\n类型：{{type.DATA}}\n{{remark.DATA}}";//账户变动模板
    
	
	public static Map<Long, String> tempMap;
	
	static{
		tempMap = new HashMap<Long, String>();
		tempMap.put(SC_ORDER, SC_ORDER_TEMP);
		tempMap.put(ACCOUNT_RECHARGE, ACCOUNT_RECHARGE_TEMP);
		tempMap.put(ACCOUNT_COSUME, ACCOUNT_COSUME_TEMP);
		tempMap.put(ACCOUNT_CHANGE, ACCOUNT_CHANGE_TEMP);
	}
	
	public static String getTemplateStr(Long tempid){
		return tempMap.get(tempid);
	}
}
