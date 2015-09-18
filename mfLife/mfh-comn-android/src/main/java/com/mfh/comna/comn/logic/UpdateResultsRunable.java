package com.mfh.comna.comn.logic;

/**
 * 用于执行界面更新UI任务
 * 
 * @author zhangyz created on 2013-4-12
 * @since Framework 1.0
 */
public class UpdateResultsRunable<T> implements Runnable{
    private T[] parmas;//更新场景        
    private IAsyncTask<Object,T> task = null;
    
    private int taskKind; //任务类型
    
    public UpdateResultsRunable(IAsyncTask<Object,T> task, int taskKind, T... parmasIn) {
        super();
        this.task = task;
        parmas = parmasIn;
        this.taskKind = taskKind;
    }
    
    public UpdateResultsRunable(IAsyncTask<Object, T> task, T... parmasIn) {
        super();
        this.task = task;
        parmas = parmasIn;
        this.taskKind = MyMultiAsyncTask.TASK_KIND_DEFAULT;
    }
    
    @Override
    public void run() {
        task.onPostExecute(taskKind, parmas[0]);      
    }        
}
