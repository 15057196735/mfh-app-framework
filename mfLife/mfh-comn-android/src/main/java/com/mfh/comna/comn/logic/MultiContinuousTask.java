package com.mfh.comna.comn.logic;
import com.mfh.comn.net.data.IResponseData;
import com.mfh.comna.comn.database.dao.NetProcessor;

import java.util.Iterator;

/**
 * 异步环境下，执行连续的多个网络任务。每个任务异步执行完毕后，再顺序启动下一个任务，直至全部任务执行结束。
 * @param <T> 每个任务执行完毕返回的bean
 * @param <Param> 执行参数
 * @param <TaskKind> 每个任务具有一个编号以便于调度，该参数指编号的类型
 */
public class MultiContinuousTask<T, Param, TaskKind> {
    /**
     * 回调方法接口定义
     * @param <T>
     * @param <Param>
     * @param <TaskKind>
     */
    public interface MultiTaskCallBack<T, Param, TaskKind> {
        /**
         * 执行后台下载,后台线程中
         * @param paramIn 参数
         * @param taskKind 当前哪种任务
         * @param callbackMethod 回调函数
         */
        void doOneTask(Param paramIn, TaskKind taskKind, NetProcessor.Processor<T> callbackMethod);

        /**
         * 下载成功后执行，主线程中
         * @param rs 网络调用执行结果
         * @param  taskKind 当前任务类型
         */
        void afterOneTask(IResponseData rs, TaskKind taskKind);

        /**
         * 任务全部执行完毕
         */
        void onFinishTasks();

        /**
         * 任务执行发送异常
         */
        void onError(Throwable ex);
    }

    protected MultiTaskCallBack<T, Param, TaskKind> asyncTask = null;
    protected Iterator<TaskKind> taskIter = null;
    protected Param param;
    protected TaskKind taskKind;
    protected Throwable havedEx = null;//后台执行时发生了异常。
    private NetProcessor.Processor<T> callbackMethod = null;

    /**
     * 构造函数
     * @param taskIter 任务迭代器，用于支持多任务，每次执行一个任务。每个任务会分页执行多次请求。
     * @param asyncTaskIn 回调接口，一个是下载接口实现，一个是下载完成后接口实现
     */
    public MultiContinuousTask(Iterator<TaskKind> taskIter,
                             final MultiTaskCallBack<T, Param, TaskKind> asyncTaskIn) {
        this.taskIter = taskIter;
        this.asyncTask = asyncTaskIn;
    }

    public NetProcessor.Processor<T> getCallbackMethod() {
        if (callbackMethod == null) {
            callbackMethod = genProcessor();
        }
        return callbackMethod;
    }

    /**
     * 构造回调函数
     * @return
     */
    protected NetProcessor.Processor<T> genProcessor() {
        NetProcessor.Processor<T> callbackMethod = new NetProcessor.Processor<T>() {
            //此处代码本身还是在主线程运行
            @Override
            public void processResult(IResponseData rs) {
                try {
                    asyncTask.afterOneTask(rs, taskKind);
                }
                catch(Throwable ex) {
                    asyncTask.onError(ex);
                    return;
                }
                nextWork();
            }

            //此处代码本身还是在主线程运行
            @Override
            protected void processFailure(Throwable t, String errMsg) {
                super.processFailure(t, errMsg);
                asyncTask.onError(t);
            }
        };
        return callbackMethod;
    }

    /**
     * 启动任务
     * @param param 传入的参数
     */
    public void startWork(Param param) {
        this.param = param;
        nextWork();
    }

    /**
     * 启动或继续任务序列
     */
    protected void nextWork() {
        if (hasNextTask()) {
            nextTask();
            startContinueTask(param);
        }
        else
            asyncTask.onFinishTasks();
    }

    /**
     * 执行或继续执行一个任务
     * @param param
     */
    protected void startContinueTask(Param param) {
        try {
            //此处代码本身还是在主线程运行
            asyncTask.doOneTask(param, taskKind, getCallbackMethod());
        }
        catch(Throwable ex) {
            asyncTask.onError(ex);
            return;
        }
    }

    /**
     * 是否还有下一个任务需要执行
     * @return
     */
    private boolean hasNextTask() {
        return taskIter.hasNext();
    }

    /**
     * 执行下一个任务
     */
    protected void nextTask() {
        taskKind = taskIter.next();
    }

    public Param getParam() {
        return param;
    }
}