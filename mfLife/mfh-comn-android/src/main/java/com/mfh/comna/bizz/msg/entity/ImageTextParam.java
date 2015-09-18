package com.mfh.comna.bizz.msg.entity;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSONObject;

/**
 * 图文消息
 */
public class ImageTextParam extends WxParam{

	private List<DataItem> data = new ArrayList<DataItem>();
	
	public ImageTextParam() {
		super(WxMsgTypeConstant.TUWEN);
	}

    public List<DataItem> getData() {
        return data;
    }

    public void setData(List<DataItem> data) {
        this.data = data;
    }

    /**
     * 从json串中构造
     * @param rawString
     * @return
     */
    public static ImageTextParam fromJson(String rawString) {
        JSONObject json = JSONObject.parseObject(rawString);
        ImageTextParam ret = JSONObject.toJavaObject(json, ImageTextParam.class);
        return ret;
    }

	public void AddDateItem(DataItem item){
		data.add(item);
	}
	
	public void AddAll(List<DataItem> items){
		this.data.addAll(items);
	}
	
	@Override
	public String toString(){
        return JSONObject.toJSONString(this);
	}

    @Override
    public String getSummary() {
        if (data != null && data.size() > 0)
            return genShortMsg(data.get(0).getTitle() + "等图文消息");
        else
            return "空消息";
    }

    @Override
    public String getContent() {
        String ret = "";
        if (data != null && data.size() > 0) {
            for (DataItem item : data) {
                ret += item.getTitle() + "\r\n";
            }
            return ret;
        }
        else
            return "空消息";
    }
}
