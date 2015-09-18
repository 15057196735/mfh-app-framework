package com.mfh.comna.comn.logic;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.mfh.comna.bizz.BizApplication;

import net.tsz.afinal.http.AjaxParams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 14-5-16.
 */
public class ComnService implements  IService {
    //统一日志记录框架,在android上会绑定andoid内部日志。在服务器上会绑定log4j之类，具体看使用的关联日志实现jar包。
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private Context context;
    @SuppressWarnings("rawtypes")
    public Context getContext() {
        if (context == null)
            return BizApplication.getAppContext();
        else
            return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * 直接构造ajaxParam，并追加json参数
     * @param keysAndValues
     * @return
     */
    protected AjaxParams genJsonAjaxParam(Object... keysAndValues) {
        AjaxParams params = new AjaxParams();
        addJsonParam(params, keysAndValues);
        return params;
    }

    protected void addJsonParam(JSONObject json, Object... keysAndValues) {
        int len = keysAndValues.length;
        if (len > 0) {
            if (len % 2 != 0)
                throw new IllegalArgumentException("传入的参数必须成对!");
            for (int i = 0; i < len; i += 2) {
                String key = String.valueOf(keysAndValues[i]);
                Object val = keysAndValues[i + 1];
                if (val != null)
                    json.put(key, val);
            }
        }
    }

    /**
     * 追加新的json参数，参数名为jsonStr，服务器端规定好了
     * @param params
     * @param keysAndValues key/value对
     */
    protected void addJsonParam(AjaxParams params, Object... keysAndValues) {
        JSONObject json = new JSONObject();
        addJsonParam(json, keysAndValues);
        params.put("jsonStr", json.toJSONString());
    }

    /**
     * 追加新的json参数，参数名为jsonStr。若jsonStr已经存在，则附加在后面
     * @param params
     * @param keysAndValues key/value对
     */
    protected void addOrAppendJsonParam(AjaxParams params, Object... keysAndValues) {
        String jsonStr = params.getNormalValue("jsonStr");
        if (jsonStr == null || jsonStr.length() == 0)
            addJsonParam(params, keysAndValues);
        else {
            JSONObject json = JSONObject.parseObject(jsonStr);
            addJsonParam(json, keysAndValues);
            params.put("jsonStr", json.toJSONString());
        }
    }
}
