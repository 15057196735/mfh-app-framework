package com.mfh.comna.comn.logic;

import android.app.Activity;

/**
 * 自己的可视化组件基类,因为通过组件界面会触发执行很多耗时操作，这些操作需要通过android异步任务机制执行。
 * 本接口确保实现者可以支持执行多个异步任务，任务通过任务号区分。
 * <Object>为异步任务执行的参数对象
 * @author zhangyz created on 2013-4-12
 * @since Framework 1.0
 */
public interface IBaseViewComponent<P,T> extends IAsyncTask<P,T>{
    int RETURN_CODE_NULL = Activity.RESULT_CANCELED;//按返回键默认返回0;
    int RETURN_CODE_OK = Activity.RESULT_OK;//按确定键返回0;
    
    /**
     * 执行一个默认的异步任务
     * 默认的任务号参见 :
     * @see com.mfh.comna.comn.logic.MyMultiAsyncTask.TASK_KIND_DEFAULT
     */
    void doAsyncTask();
    
    /**
     * 执行一个默认的异步任务,并传递参数
     * 默认的任务号参见 :
     * @see com.mfh.comna.comn.logic.MyMultiAsyncTask.TASK_KIND_DEFAULT
     * @param param 任务参数
     * @author zhangyz created on 2013-4-14
     */
    void doAsyncTaskWithParam(P... param);
    
    /**
     * 执行一个异步任务
     * @param taskKind 任务号，子类自定义
     * 
     * @author zhangyz created on 2013-4-12
     */
    void doAsyncTask(int taskKind);
    
    /**
     * 执行一个异步任务，并传递参数
     * @param taskKind 任务号，子类自定义
     * @param param
     * @author zhangyz created on 2013-4-12
     */
    void doAsyncTask(int taskKind, P... param);

    /**
     * 执行异步更新界面的操作
     * @param param
     * @author zhangyz created on 2013-4-11
     */
    void doAsyncUpdateUi(P... param);
}
