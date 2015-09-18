package net.tsz.afinal.http;

public abstract class AjaxCallBack <T>{
    private boolean progress = true;
    private int rate = 1000 * 1;//每秒
    
//  private Class<T> type;
//  
//  public AjaxCallBack(Class<T> clazz) {
//      this.type = clazz;
//  }
    
    
    public boolean isProgress() {
        return progress;
    }
    
    public int getRate() {
        return rate;
    }
    
    /**
     * 设置进度,而且只有设置了这个了以后，onLoading才能有效。
     * @param progress 是否启用进度显示
     * @param rate 进度更新频率
     */
    public AjaxCallBack<T> progress(boolean progress , int rate) {
        this.progress = progress;
        this.rate = rate;
        
        /*JSONObject root = new JSONObject(response);
        resultId = Integer.parseInt(root.getString("userid"));*/
        
        return this;
    }
    
    /**
     * 后台线程开始处理数据
     * 该方法应在UI主线程中运行
     * @author zhangyz created on 2013-5-15
     */
    public void onStart(){};
    
    /**
     * 后台线程开始处理数据，进度。。
     * 该方法应在UI主线程中运行
     * onLoading方法有效progress
     * @param count
     * @param current
     */
    public void onLoading(long count, long current){};
    
    /**
     * 后台线程处理数完数据后，参数为获取的值
     * 该方法应在UI主线程中运行
     * @param t
     * @author zhangyz created on 2013-5-15
     */
    public void onSuccess(T t){};
    
    /**
     * 后台线程处理数据的任何错误
     * 该方法应在UI主线程中运行
     * @param t
     * @param strMsg
     * @author zhangyz created on 2013-5-15
     */
    public void onFailure(Throwable t, String strMsg){};
}
