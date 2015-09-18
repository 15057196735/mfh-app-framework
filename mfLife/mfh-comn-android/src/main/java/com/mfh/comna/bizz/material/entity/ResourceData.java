package com.mfh.comna.bizz.material.entity;

import com.mfh.comna.MfhEntity;
import com.mfh.comn.bean.IObject;

/**
 * 素材定义类
 */
public class ResourceData extends MfhEntity<Long> implements IObject<Long> {

	private static final long serialVersionUID = 1L;
    public static int RES_TYPE_TEXT = 0;//文本
    public static int RES_TYPE_TUWEN = 1;//图文
    public static int RES_TYPE_IMAGE = 2;//图片

	private String param ;
	/**0=文本，1=图文 2=图片 */
	private Integer type ;
	private Integer subdisid ;
	private Integer picfrom = 0;//图片来源：0微信

	/**素材是否公开  默认0=不公开，1=公开*/
	private Integer isPublic  = 0;
	/**标题*/
	private String title = "";
	public Integer getPicfrom() {
		return picfrom;
	}

	public void setPicfrom(Integer picfrom) {
		this.picfrom = picfrom;
	}

	//private List<MsgAttr> msgAttrList = new ArrayList<MsgAttr>();

	//public void setMsgAttrList(List<MsgAttr> msgAttrList) {
	//	this.msgAttrList = msgAttrList;
	//}

	public String getParam() {
		return param;
	} 

	public void setParam(String param) {
		this.param = param;
	} 


	public Integer getType() {
		return type;
	} 

	public void setType(Integer type) {
		this.type = type;
	} 


	public Integer getSubdisid() {
		return subdisid;
	} 

	public void setSubdisid(Integer subdisid) {
		this.subdisid = subdisid;
	}

	public Integer getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	} 
}
