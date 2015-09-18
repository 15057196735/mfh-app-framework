package com.mfh.comna.comn.logic;

import android.app.Dialog;
import android.os.AsyncTask;

import com.mfh.comna.api.ui.dialog.DialogHelper;
import com.mfh.comna.bizz.BizApplication;
import com.mfh.comna.comn.ComnApplication;
import com.mfh.comna.utils.DialogUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对android核心异步任务的再封装,可以支持处理异常等,支持日志等.
 * 若需处理异常继承doInBackgroundException()即可。
 * Created by Administrator on 14-6-13.
 */
public abstract class MyAsyncTask<P, T> extends AsyncTask<P, Integer, T> {
    private boolean progress = true;
    private int rate = 1000 * 1;//每秒
    private transient P[] params;//传递的参数
    protected transient Dialog progressDialog = null;
    private transient Throwable processEx = null;//处理过程中产生的异常
    private Logger logger = null;

    /**
     * 获取日志
     * @return
     */
    protected Logger getLogger() {
        if (logger == null)
            logger = LoggerFactory.getLogger(this.getClass());
        return logger;
    }

    protected MyAsyncTask(boolean showDialog) {
        if (showDialog)
            progressDialog = DialogHelper.genProgressDialog(ComnApplication.getAppContext(), false, null);
        else
            progressDialog = null;
    }

    /**
     * 执行后台任务,若有异常直接抛出即可
     * @param params 原始参数
     * @return 成功执行后返回结果
     */
    protected abstract T doInBackgroundInner(P... params);

    /**
     * 成功执行
     * @param result 执行结果
     * @param params 原始参数
     */
    protected abstract void onPostExecuteInner(T result, P... params);

    /**
     * 处理异常,子类无须理会亦可
     * @param ex
     * @param params
     */
    protected void doInBackgroundException(Throwable ex, P... params) {

    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected T doInBackground(P... params) {
        this.params = params;
        try {
            return doInBackgroundInner( params);
        }
        catch (Throwable ex) {// 需要自己处理异常，否则后面不会执行onPostExecute
            getLogger().error(ex.getMessage(), ex);
            processEx = ex;
            return null;
        }
    }

    @Override
    protected void onPostExecute(T result) {// 装载结束
        super.onPostExecute(result);
        try {
            if (processEx == null) {//成功执行
                onPostExecuteInner(result, params);
                // 隐藏进度条
                hideProgressDialog();
            }
            else {
                // 隐藏进度条
                hideProgressDialog();
                try {
                    DialogUtil.showHint(processEx.getMessage());
                    doInBackgroundException(processEx, params);
                }
                catch (Throwable ex) {
                    getLogger().error(ex.getMessage(), ex);
                }
            }
        }
        catch (Throwable ex) {
            try {
                hideProgressDialog();
                getLogger().error(null, ex);
            }
            catch (Throwable ex2) {
                ;
            }
        }
    }

    public boolean isProgress() {
        return progress;
    }

    public int getRate() {
        return rate;
    }

    public void setProgress(boolean progress) {
        this.progress = progress;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    /**
     * 隐藏进度条
     * */
    private void hideProgressDialog(){
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
