package com.mfh.comna.bizz.material.dao;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.mfh.comna.api.utils.MLog;
import com.mfh.comn.net.ResponseBody;
import com.mfh.comn.net.data.IResponseData;
import com.mfh.comn.net.data.RspValue;
import com.mfh.comna.comn.database.dao.NetCallBack;
import com.mfh.comna.network.NetFactory;

import net.tsz.afinal.http.AjaxParams;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 用于上次到java资源库的dao接口
 * Created by Administrator on 14-5-16.
 */
public class ResourceNetDao {
    protected String uploadUrl = null;
    protected String downLoadUrl = null;
    private Context context;

    public ResourceNetDao(Context context) {
        //uploadUrl = NetFactory.getServerUrl() + "/res/upload";
        uploadUrl = NetFactory.getServerUrl() + "/res/remotesave/upload";
        //uploadUrl = NetFactory.getImageUploadUrl() + "/res/save/upload";
        downLoadUrl = NetFactory.getServerUrl() + "/res/remotesave/download";
        //downLoadUrl = "http://devmsgcore.manfenjiayuan.com:58080/msgcore";
        this.context = context;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    /**
     * 上传后的回调函数
     */
    public abstract static class UploadCallback{
        /**
         * 对上传成功的图像进行处理
         * @param imgId 图像编号 若为-1，代表没有意义
         * @param imgUrl 图像在服务器上的地址 可能为空
         */
        public abstract void processImg(long imgId, String imgUrl, File srcFile);
    }

    /**
     * 执行文件上传,fileToUpload
     * @param srcFile
     */
    public void doUpload( AjaxParams params, final File srcFile, final UploadCallback callback) {
        try {
            params.put("fileToUpload", srcFile);
            params.put("responseType", "3");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("不存在的文件:" + srcFile.getAbsolutePath());
        }
        MLog.d(String.format("[POST]upload: %s?%s", uploadUrl, params.toString()));
        NetFactory.getHttp().post(uploadUrl, params, new NetCallBack.NormalNetTask<String>(String.class, context) {
                @Override
                public void processResult(IResponseData rspData) {
                    MLog.d(rspData.toString());
                    RspValue<?> ret = (RspValue<?>) rspData;
                    Object retValue = ret.getValue();
                    if (retValue instanceof String) {
                        if (StringUtils.isNumeric(retValue.toString())) {
                            Long imgId = Long.parseLong(retValue.toString());
                            String imgUrl = downLoadUrl + ret.getValue();
                            callback.processImg(imgId, imgUrl, srcFile);
                        }
                        else {
                            String value = (String) retValue;
                            int index = value.indexOf(":");
                            Long id = Long.valueOf(value.substring(0, index));
                            String imgUrl = value.substring(index + 1);
                            callback.processImg(id, imgUrl, srcFile);
                        }
                    }
                    else if (retValue instanceof Long || retValue instanceof Integer) {
                        Long imgId = Long.parseLong(retValue.toString());
                        callback.processImg(imgId, null, srcFile);
                    }
                }

                @Override
                protected ResponseBody parserResponse(String rawValue) {
                    MLog.d(rawValue);
                    JSONObject json = JSONObject.parseObject(rawValue);
                    if (json.containsKey("error")) {//兼容老的不正式接口
                        String code = json.getString("error");
                        if ("0".equals(code)) {
                            String url = json.getString("url");
                            ResponseBody resp = new ResponseBody();
                            resp.initDirect(0, "", new RspValue<String>(url));
                            return resp;
                        } else
                            throw new RuntimeException("错误:" + rawValue);
                    }
                    else
                        return super.parserResponse(rawValue);
                }
            }
        );
    }
}
