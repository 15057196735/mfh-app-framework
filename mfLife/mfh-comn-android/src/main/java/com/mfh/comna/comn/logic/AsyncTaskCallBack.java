package com.mfh.comna.comn.logic;


import android.app.ProgressDialog;
import android.content.Context;

import com.mfh.comna.utils.DialogUtil;


import net.tsz.afinal.http.AjaxCallBack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加入了进度条和出错提示或出错日志记录等
 * @param <T>
 * @author zhangyz created on 2013-5-15
 * @since Framework 1.0
 */
public abstract class AsyncTaskCallBack<T> extends AjaxCallBack<T> {
    protected Context context;
    private ProgressDialog progressDialog;
    private Logger logger = null;
    
    protected Logger getLogger() {
        if (logger == null)
            logger = LoggerFactory.getLogger(this.getClass());
        return logger;
    }
    
    /**
     * 构造函数
     * @param context android上下文。可以为空，若提供了此参数，则会自动出现"等待中..."；
     * 失败时也会弹出错误信息，否则只会记录日志信息。
     */
    public AsyncTaskCallBack(Context context) {
        super();
        this.context = context;
    }
    
    public AsyncTaskCallBack() {
        super();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (context != null)
            try {
                //progressDialog = ProgressDialog.show(context, "请稍等...", "正在处理中...", true);
            }
            catch(Throwable ex) {
                ;
            }
        else
            progressDialog = null;
    }

    /**
     * 留一个机会供子类做任何异常的处理，包括网络请求成功后但后期处理发生的异常。
     */
    protected void doWhenAnyException(Throwable ex) {;}

    @Override
    public void onFailure(Throwable t, String strMsg) {
        try {
            if (strMsg == null)
                strMsg = t.getMessage();
            super.onFailure(t, strMsg);
            doFailure(t, strMsg);
        }
        catch(Throwable ex) {
            doWhenAnyException(ex);
            getLogger().error(ex.getMessage(), ex);
        }
    }

    @Override
    public void onSuccess(T t) {
        super.onSuccess(t);
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
            doSuccess(t);
        }
        catch(Throwable ex) {
            doWhenAnyException(ex);
            getLogger().warn(ex.getMessage(), ex);
            if (context != null) {
                new DialogUtil(context).showHint(ex.getMessage());
            }
        }
    }
    
    /**
     * 调用成功后，执行具体的业务逻辑。
     * 可抛出异常，框架已经处理异常。
     * @param rawValue 原始值
     */
    protected abstract void doSuccess(T rawValue);
    
    /**
     * 调用失败后，执行具体的失败处理业务逻辑。
     * @param t
     */
    protected void doFailure(Throwable t, String errMsg) {
        if (context != null) {
            if (t instanceof java.net.UnknownHostException)
                errMsg = "网络不通,请检查你手机是否正确连上网络!";
            new DialogUtil(context).showHint(errMsg);
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        else {
            if (errMsg == null)
                errMsg = "";
            getLogger().error(errMsg);//改成记录日志。
        }
    }
}
