package com.mfh.comna.comn.logic;

import java.util.Iterator;
import java.util.Map;

/**
 * 异步环境下，执行多任务查询，每个查询又支持分页。注意多任务查询返回的结果集类型都是一样的。
 * 任务查询的参数类型固定为Map<String, Object>,任务类型固定为：Integer
 * @param <T> 查询结果集中返回的bean
 * Created by Administrator on 14-5-23.
 */
public class MultiTaskWithPageSimple<T> extends MultiTaskWithPage<T, Map<String, Object>, Integer>{
    /**
     * 查询回调方法接口定义，简单版
     * @param <T>
     */
    public interface QueryAsyncTask<T> extends MultiQueryTaskCallBack<T, Map<String, Object>, Integer> {

    }

    /**
     * 构造函数
     * @param taskIter  任务迭代器，用于支持多任务，每次执行一个任务。每个任务会分页执行多次请求。
     * @param asyncTask 回调接口，一个是下载接口实现，一个是下载完成后接口实现
     * @param pageSize  分页大小
     */
    public MultiTaskWithPageSimple(Iterator<Integer> taskIter, QueryAsyncTask<T> asyncTask, int... pageSize) {
        super(taskIter, asyncTask, pageSize);
    }
}
