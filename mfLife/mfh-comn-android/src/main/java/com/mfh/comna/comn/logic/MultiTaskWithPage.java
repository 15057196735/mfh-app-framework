/**
 * Created by zhangyz on 14-5-23.
 */
package com.mfh.comna.comn.logic;

import com.mfh.comn.bean.PageInfo;
import com.mfh.comn.net.data.IResponseData;
import com.mfh.comn.net.data.RspQueryResult;
import com.mfh.comna.comn.database.dao.NetProcessor;

import java.util.Iterator;

/**
 * 异步环境下，执行多任务查询，每个查询又支持分页。注意多任务查询返回的结果集类型都是一样的。
 * @param <T> 查询结果集中返回的bean
 * @param <Param> 查询参数
 * @param <TaskKind> 每个任务编号的类型
 */
public class MultiTaskWithPage<T, Param, TaskKind> extends MultiContinuousTask<T, Param, TaskKind>{
    private PageInfo pageInfo;

    /**
     * 连续多任务查询回调接口
     * @param <T>
     * @param <Param>
     * @param <TaskKind>
     */
    public interface MultiQueryTaskCallBack<T, Param, TaskKind> {
        /**
         * 执行后台下载,后台线程中
         * @param paramIn 参数
         * @param taskKind 当前哪种任务
         * @param callbackMethod 回调函数
         */
        public void doOneTask(Param paramIn, TaskKind taskKind, NetProcessor.QueryRsProcessor<T> callbackMethod);

        /**
         * 下载成功后执行，主线程中
         * @param rs 结果集
         * @param  taskKind 当前任务类型
         */
        public void afterOneTask(RspQueryResult<T> rs, TaskKind taskKind);

        /**
         * 任务全部执行完毕
         */
        public void onFinishTasks();

        /**
         * 任务执行发送异常
         */
        public void onError(Throwable ex);
    }

    /**
     * 构造函数
     * @param taskIter 任务迭代器，用于支持多任务，每次执行一个任务。每个任务会分页执行多次请求。
     * @param asyncTaskIn 回调接口，一个是下载接口实现，一个是下载完成后接口实现
     * @param pageSize 分页大小
     */
    public MultiTaskWithPage(Iterator<TaskKind> taskIter,
                             final MultiQueryTaskCallBack<T, Param, TaskKind> asyncTaskIn, int... pageSize) {
        super(taskIter, new MultiTaskCallBackImpl<T, Param, TaskKind>(asyncTaskIn));
        if (pageSize != null && pageSize.length > 0)
            this.pageInfo = new PageInfo(pageSize[0]);
        else
            this.pageInfo = new PageInfo();
    }

    @Override
    protected NetProcessor.Processor<T> genProcessor() {
        NetProcessor.QueryRsProcessor<T> callbackMethod = new NetProcessor.QueryRsProcessor<T>(this.pageInfo) {
            //此处代码本身还是在主线程运行
            @Override
            public void processQueryResult(RspQueryResult<T> rs) {
                try {
                    //防止服务器没有处理分页信息,否则会不断发动请求
                    if (pageInfo.isNotInit() || rs.getReturnNum() == 0) {
                        pageInfo.setTotalCount(0);
                    }
                    asyncTask.afterOneTask(rs, taskKind);
                }
                catch(Throwable ex) {
                    asyncTask.onError(ex);
                    return;
                }
                startContinueTask(param);
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

    @Override
    protected void startContinueTask(Param param) {
        if (pageInfo.hasNextPage()) {
            pageInfo.moveToNext();
            super.startContinueTask(param);
        }
        else
            nextWork();//再次回调
    }

    @Override
    protected void nextTask() {
        super.nextTask();
        pageInfo.reset();
    }

    private static class MultiTaskCallBackImpl<T, Param, TaskKind> implements MultiTaskCallBack<T, Param, TaskKind> {
        private MultiQueryTaskCallBack<T, Param, TaskKind> queryCallBack;

        private MultiTaskCallBackImpl(MultiQueryTaskCallBack<T, Param, TaskKind> queryCallBack) {
            this.queryCallBack = queryCallBack;
        }

        @Override
        public void doOneTask(Param paramIn, TaskKind taskKind, NetProcessor.Processor<T> callbackMethod) {
            NetProcessor.QueryRsProcessor<T> queryCall = (NetProcessor.QueryRsProcessor<T>)callbackMethod;
            queryCallBack.doOneTask(paramIn, taskKind, queryCall);
        }

        @Override
        public void afterOneTask(IResponseData rs, TaskKind taskKind) {
            RspQueryResult<T> queryRs = (RspQueryResult<T>)rs;
            queryCallBack.afterOneTask(queryRs, taskKind);
        }

        @Override
        public void onFinishTasks() {
            queryCallBack.onFinishTasks();
        }

        @Override
        public void onError(Throwable ex) {
            queryCallBack.onError(ex);
        }
    }
}