package com.mfh.comn.net;

import com.mfh.comn.net.data.IResponseData;

/**
 * 服务端返回数据定义框架的基类
 * 
 * @author zhangyz created on 2013-5-14
 * @since Framework 1.0
 */
public class ResponseBody {
    protected final String TAG_QUERYRESULT = "result";
    protected final String TAG_TEMP_cacheDomain = "cacheDomain";

    protected final static String TAG_ROW = "row";
    protected final static String TAG_CODE = "code";
    protected final static String TAG_CAPTION = "caption";
    
    protected final String TAG_DATAPROPS = "props";//针对json需要,xml自己本身有属性    
    
    protected boolean bZipFlag = false;//是否压缩    
    protected boolean bEncodeflag = false;//是否编码    
    protected boolean bEncryptflag = false;//是否加密
    
    protected String returnInfo;//返回码的描述    
    protected int retCode = -999;// 返回码。
    protected Integer version = 0;
    protected IResponseData data = null;//数据部分
    
    public static int RETCODE_SUCCESS = 0;//成功标志
    
    /**
     * 直接初始化
     * @param pa_returnCode
     * @param pa_returnInfo
     * @param data
     * @author zhangyz created on 2014-3-8
     */
    public void initDirect(int pa_returnCode, String pa_returnInfo, IResponseData data) {
        this.setResult(pa_returnCode, pa_returnInfo);
        this.data = data;
    }
    
    public boolean isSuccess() {
        return this.retCode == RETCODE_SUCCESS;
    }
    
    public boolean isbZipFlag() {
        return bZipFlag;
    }
    
    public void setbZipFlag(boolean bZipFlag) {
        this.bZipFlag = bZipFlag;
    }
    
    public boolean isbEncodeflag() {
        return bEncodeflag;
    }
    
    public void setbEncodeflag(boolean bEncodeflag) {
        this.bEncodeflag = bEncodeflag;
    }
    
    public boolean isbEncryptflag() {
        return bEncryptflag;
    }
    
    public void setbEncryptflag(boolean bEncryptflag) {
        this.bEncryptflag = bEncryptflag;
    }
    
    public IResponseData getData() {
        return data;
    }

    
    public void setData(IResponseData data) {
        this.data = data;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * 获取返回码描述
     * @return
     * @author zhangyz created on 2013-5-14
     */
    public String getReturnInfo() {
        return returnInfo;
    }
    
    public void setReturnInfo(String returnInfo) {
        this.returnInfo = returnInfo;
    }
    
    /**
     * 获取返回码
     * @return
     * @author zhangyz created on 2013-5-14
     */
    public int getRetCode() {
        return retCode;
    }
    
    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    /**
     * 设置Result。
     * @param pa_returnCode 返回码,整型;
     * @param pa_returnInfo 对返回码的描述，可以为空;
     */
    protected void setResult(int pa_returnCode, String pa_returnInfo) {
        if (pa_returnInfo == null)
            pa_returnInfo = "";
        returnInfo = pa_returnInfo;
        retCode = pa_returnCode;
    }
}
