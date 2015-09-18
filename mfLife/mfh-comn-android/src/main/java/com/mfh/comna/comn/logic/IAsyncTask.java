package com.mfh.comna.comn.logic;

import android.app.ProgressDialog;

/**
 * 执行后台异步任务并在任务请求结束后根据结果绘制UI界面的接口
 * 相当于Android AsyncTask的接口。
 * 
 * 其中P代表参数，T代表异步执行的结果类型
 * @author zhangyz created on 2013-4-10
 * @since Framework 1.0
 */
public interface IAsyncTask<P, T> {
    /**
     * 1、由后台线程执行后台异步任务；内部已经处理了异常（会调用doInBackgroundException)，子类不需要catch;
     * @param taskKind 任务号
     * @param params 任务执行参数
     * @return 返回执行的数据，结果可能是一个object或object数组
     * @author zhangyz created on 2013-4-11
     */
    T doInBackground(int taskKind, P... params);
    
    /**
     * 后台线程开始处理数据
     * 该方法应在UI主线程中运行
     * @author zhangyz created on 2013-5-15
     */
    ProgressDialog onPreExecute(int taskKind);
    
    /**
     * 2、由后台线程执行，后台异步任务出错时调用此方法
     * @param ex 异常
     * @param params 执行异步任务时的参数
     * @return
     * @author zhangyz created on 2013-4-11
     */
    void doInBackgroundException(int taskKind, Throwable ex, P... params);
    
    /**
     * 后台线程正在处理数据
     * 可以执行进度条显示
     * 该方法应在UI主线程中运行
     * @param values 进度数据
     * @author zhangyz created on 2013-4-11
     */
    void onProgressUpdate(int taskKind, Integer... values);

    /**
     * 3、后台执行成功后，由ui主线程执行更新前台UI界面的任务接口
     * @param taskKind 任务号
     * @param result 任务执行结束后返回的参数
     * @param params 当时执行任务传递的参数
     * @author zhangyz created on 2013-4-11
     */
    void onPostExecute(int taskKind, T result, P... params);
}
