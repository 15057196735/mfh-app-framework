package com.mfh.comn.bean.msg.param;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.mfh.comn.bean.msg.MsgConstant;

@SuppressWarnings("serial")
public class ImageTextParam extends BaseParam{
	private List<DataItem> data = new ArrayList<DataItem>();
	
	public ImageTextParam() {
        super();
    }

    public ImageTextParam(List<DataItem> data) {
        super();
        this.data = data;
    }

    @JSONField(serialize=false)
    @Override
    public String getType() {
        return MsgConstant.MSG_TECHTYPE_TUWEN;
    }

    public void AddDateItem(DataItem item){
		data.add(item);
	}
	
	public void AddAll(List<DataItem> items){
		this.data.addAll(items);
	}
	
	public List<DataItem> getData() {
		return data;
	}

	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
    
    @Override
    public void attachSignName(String signname) {
        //unsupport signname
    }

    @Override
    public boolean haveSignName() {
        return true;
    }
}
