package com.mfh.comna.comn.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * 对android的异步任务再做包装，支持多项异步任务、启动前显示等待框等，
 * 异常处理封装并提示出错消息等。
 * 因为是多任务，故每个任务的参数和返回值类型可能不同，不能统一定义，所以都采用Object作为泛型。
 * 进度值泛型则统一采用Integer整形。
 * @param <P> 参数
 * @param <T> 结果
 * @author zhangyz created on 2013-5-15
 * @since Framework 1.0
 */
public final class MyMultiAsyncTask<P, T> extends AsyncTask<P, Integer, T> 
        implements IPublishProgressAble<Integer>{
    private boolean progress = true;
    private int rate = 1000 * 1;//每秒

    public static int TASK_KIND_DEFAULT = 0;
    private IAsyncTask<P, T> handle;
    private int taskKind = TASK_KIND_DEFAULT;// 任务类型，固定的参数

    private transient P[] params;//传递的参数

    private transient ProgressDialog progressDialog = null;
    private transient Throwable processEx = null;//处理过程中产生的异常
    
    private Logger logger = null;
    
    protected Logger getLogger() {
        if (logger == null)
            logger = LoggerFactory.getLogger(MyMultiAsyncTask.class);
        return logger;
    }

    public MyMultiAsyncTask(IAsyncTask<P, T> handle) {
        super();
        this.handle = handle;
    }
    
    public MyMultiAsyncTask(IAsyncTask<P, T> handle, int taskKind) {
        super();
        //this.context = context;
        this.handle = handle;
        if (taskKind == 0)
            throw new RuntimeException("默认的任务类型就是" + Integer.toString(taskKind) + "!请重新指定!");
        this.taskKind = taskKind;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = handle.onPreExecute(taskKind);
    }

    @Override
    public void publishProgressByService(Integer... param) {
        super.publishProgress(param);   
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        handle.onProgressUpdate(taskKind, values);
    }
    
    @Override
    protected T doInBackground(P... params) {
        try {
            // publishProgress(1);//会触发执行onProgressUpdate
            this.params = params;
            return handle.doInBackground(taskKind, params);
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
                handle.onPostExecute(taskKind, result, params);
                hideProgressDialog();
            }
            else {
                hideProgressDialog();
                handle.doInBackgroundException(taskKind, processEx, params);
            }
        }
        catch (Throwable ex) {
            hideProgressDialog();
            getLogger().error(null, ex);
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
