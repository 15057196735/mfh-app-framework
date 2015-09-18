package com.mfh.comna.comn.logic;

import android.util.Log;

import com.mfh.comn.bean.EntityWrapper;
import com.mfh.comn.net.ResponseBody;
import com.mfh.comn.net.data.RspQueryResult;

import java.util.List;

/**
 * Created by Administrator on 2014/12/11.
 * json数据解析， 统一异常处理 待完善
 */
public class JsonParser {

    public static <T> List<EntityWrapper<T>> parseArray(String result , Class<T> clazz) {
        ResponseBody responseBody = parserResponse(result, clazz);
        try {
            if (!responseBody.isSuccess()) {
                String errMsg = responseBody.getRetCode() + ":" + responseBody.getReturnInfo();
                Log.e("JsonParser", errMsg);
            } else {
                RspQueryResult<T> rspData = (RspQueryResult<T>) responseBody.getData();
                return rspData.getRowDatas();
            }
        }catch (Exception e) {
            Log.e("JsonParser", e.getMessage());
        }
        return null;
    }

    /**
     * @param rawValue
     * @return
     */
    public static ResponseBody parserResponse(String rawValue ,Class clazz) {
        com.mfh.comn.net.JsonParser parser = new com.mfh.comn.net.JsonParser();
        ResponseBody resp = parser.parser(rawValue, clazz, com.mfh.comn.net.JsonParser.defaultFormat);
        return resp;
    }
}
