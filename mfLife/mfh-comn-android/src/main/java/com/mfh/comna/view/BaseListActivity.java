package com.mfh.comna.view;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.mfh.comn.bean.IObject;
import com.mfh.comn.bean.PageInfo;
import com.mfh.comn.net.data.RspQueryResult;
import com.mfh.comna.comn.bean.KvBean;
import com.mfh.comna.comn.database.dao.IDao;
import com.mfh.comna.comn.database.dao.NetProcessor;
import com.mfh.comna.comn.logic.IBaseViewComponent;
import com.mfh.comna.comn.logic.IFillItemView;
import com.mfh.comna.comn.logic.MyMultiAsyncTask;
import com.mfh.comna.comn.logic.MyPageListAdapter;
import com.mfh.comna.comn.logic.UpdateResultsRunable;
import com.mfh.comna.utils.DialogUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * 普通支持listView的基类,基于ListActivity改造。
 * 内部绑定了数据适配器，子类实现的数据适配器必须从MyPageListAdapter类继承。
 * 采用自定义actionbar，home按钮默认返回到上一个页面。
 * @author zhangyz created on 2013-4-5
 * @since Framework 1.0
 */
public abstract class BaseListActivity <T extends IObject> extends Activity implements OnClickListener, 
    IBaseViewComponent<Object, Object>, OnScrollListener, IFillItemView<T> {

    protected ActionBar myActionBar;//自定义actionBar
    private Handler mHandler = new Handler();//用于异步更新界面
    protected DialogUtil dialogUtil;
    
    protected PageInfo mPageInfo = null;
    protected int mLastItem, mFirstItem;
    protected int mLoadedCount = 0;//已经加载的指标项条数
    protected boolean completeFlag = true;//当前是否已经加载完毕数据和刷新操作。
    
    //这两个原来也是ListActivity中成员，但类型被改了。
    protected MyPageListAdapter<T> mAdapter = null;
    protected boolean cacheDataItem = true;//是否需要缓存列表条目
    protected AbsListView mList;
    private String searchToken;//是否有预置的模糊检索条件

    private final String mPageName = this.getClass().getSimpleName();
    
    /**
     * 获取面板的layout布局号
     * @return
     * @author zhangyz created on 2013-4-5
     */
    public abstract int getLayoutId();

    protected void initActionBar(ActionBar actionBar) {
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void setSearchToken(String searchToken) {
        this.searchToken = searchToken;
    }

    /**
     * dao本身是否异步
     * @return
     * @author zhangyz created on 2014-3-12
     */
    protected boolean isAsyncDao() {
        return IDao.isAyncDao;
    }

    @Override
    public boolean isItemEnabled(int position) {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageInfo = initPageInfo();
        setContentView(getLayoutId());

        myActionBar = this.getActionBar(); //(ActionBar)this.findViewById(R.id.gd_action_bar);
        if (myActionBar != null)
            initActionBar(myActionBar);

        getListView().setOnScrollListener(this);
        completeFlag = true;//初始化
    }

    @Override
    public void onResume() {
        super.onResume();
        //执行数据加载和显示
        doLoadAndRefresh();

        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }

    /**
     * 重新初始化页面元素。
     */
    private void resetPage() {
        mPageInfo.reset();
        mLastItem = 0;
        mFirstItem = 0;
        mLoadedCount = 0;
    }
    
    /**
     * 刷新，重新触发异步加载数据并刷新界面
     * @see #doLoadDataFact(boolean)
     * @author zhangyz created on 2013-5-3
     */
    public void doLoadAndRefresh() {
        if (!completeFlag) {
            return;//防止重复操作。
        }
        completeFlag = false;//表示正在加载
        resetPage();
        if (mAdapter != null) {
            mAdapter.clearData();
            mAdapter.notifyDataSetChanged();
        }
        else
            createAdapter();
        doLoadAndRefreshNext();
    }

    /**
     * 执行实际的下一页加载。
     * 读取下一页,方向可能是顺序或反序
     *
     */
    private void doLoadAndRefreshNext() {
        if (this.isAsyncDao()) {
            doLoadDataFact(true);
        }
        else {
            //执行异步加载数据，后面会执行到doLoadDataFact
            doAsyncTask();
        }
    }
    
    protected DialogUtil getDialogUtil() {
        if (dialogUtil == null)
            dialogUtil = new DialogUtil(this);
        return dialogUtil;
    }

    /*
    @Override
    public void onActionBarItemClicked(int position) {
        if (position == OnActionBarListener.HOME_ITEM) {
            this.setResult(RETURN_CODE_NULL, null);//返回为0
            this.finish();//结束当前activity
        }
    }
    */

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * 提示消息，稍后自动关闭
     * @param message 提示消息
     * @author zhangyz created on 2013-4-12
     */
    protected void showHint(CharSequence message) {
        getDialogUtil().showHint(message);
    }
    
    /** 
     * 提示消息，点击确定后关闭 
     * @param message 提示消息
     */
    protected void showMessage(CharSequence message){ 
        getDialogUtil().showMessage(message);
    }
    
    protected void showYesNoDialog(CharSequence message, DialogInterface.OnClickListener listen) {
        getDialogUtil().showYesNoDialog(message, listen);
    }
    
    /**
     * 显示一个典型的两按钮的询问对话框
     * @param title
     * @param message
     * @return ture：选择了是: false:选择了否
     * @author zhangyz created on 2013-4-5
     */
    protected void showYesNoDialog(CharSequence title, CharSequence message, 
            DialogInterface.OnClickListener listen) {
        getDialogUtil().showYesNoDialog(title, message, listen);
    }
    
    @Override
    public void doAsyncTask() {
        new MyMultiAsyncTask<Object, Object>(this).execute();        
    }

    @Override
    public void doAsyncTaskWithParam(Object... param) {
        new MyMultiAsyncTask<Object, Object>(this).execute(param);           
    }
    
    @Override
    public void doAsyncTask(int taskKind) {
        new MyMultiAsyncTask<Object, Object>(this, taskKind).execute();        
    }
    
    @Override
    public void doAsyncTask(int taskKind, Object... param) {
        new MyMultiAsyncTask<Object, Object>(this, taskKind).execute(param);        
    }
    
    @Override
    public void doAsyncUpdateUi(Object... param) {
        mHandler.post(new UpdateResultsRunable<Object>(this, param));
    }
    
    @Override
    public Object doInBackground(int taskKind, Object... params) {
        if (taskKind == MyMultiAsyncTask.TASK_KIND_DEFAULT) {
            return doLoadDataFact(false);
        }
        else
            return null;
    }
    
    @Override
    public void onPostExecute(int taskKind, Object result, Object...params) {
        if (taskKind == MyMultiAsyncTask.TASK_KIND_DEFAULT) {
            doRefreshViewFact(result);
        }
        else {
            
        }
    }
    
    @Override
    public ProgressDialog onPreExecute(int taskKind) {
        // 开启进度条
        return ProgressDialog.show(this, "请稍等...", "正在处理中...", true);
    }
    
    @Override
    public void onProgressUpdate(int taskKind, Integer... values) {
        
    }

    @Override
    public void doInBackgroundException(int taskKind, Throwable ex, Object... params) {
        try {
            Toast.makeText(this, "执行出错:" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        catch (Throwable e){
            System.out.println("doInBackgroundException出错:" + e.getMessage());
        }
        if (taskKind == MyMultiAsyncTask.TASK_KIND_DEFAULT) {
            this.completeFlag = true;//复位
        }
    }

    @Override
    public void onClick(View arg0) {
        
    }
    
    //==================下面是有关ListActivity的源码，从开源中拷贝过来=====================================//
    private boolean mFinishedStart = false;
    private Runnable mRequestFocus = new Runnable() {
        public void run() {
            mList.focusableViewAvailable(mList);
        }
    };

    /**
     * This method will be called when an item in the list is selected.
     * Subclasses should override. Subclasses can call
     * getListView().getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param l The ListView where the click happened
     * @param v The view that was clicked within the ListView
     * @param position The position of the view in the list
     * @param id The row id of the item that was clicked
     */
    protected void onListItemClick(AbsListView l, View v, int position, long id) {
    }

    /**
     * Ensures the list view has been created before Activity restores all
     * of the view states.
     *
     *@see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
     */
    @Override
    protected void onRestoreInstanceState(Bundle state) {
        ensureList();
        super.onRestoreInstanceState(state);
    }

    /**
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(mRequestFocus);
        super.onDestroy();
    }

    /**
     * Updates the screen state (current list and other views) when the
     * content changes.
     *
     * @see android.app.Activity#onContentChanged()
     */
    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mList = (AbsListView)findViewById(this.getListViewResLayoutId()); //com.android.internal.R.id.list
        if (mList == null) {
            throw new RuntimeException(
                    "Your content must have a ListView whose id attribute is " +
                    "'android.R.id.list'");
        }
        /*View emptyView = findViewById(com.android.internal.R.id.empty);
        if (emptyView != null) {
            mList.setEmptyView(emptyView);
        }*/
        mList.setOnItemClickListener(mOnClickListener);
        if (mFinishedStart) {
            setListAdapter(mAdapter);
        }
        mHandler.post(mRequestFocus);
        mFinishedStart = true;
    }

    /**
     * Provide the cursor for the list view.
     */
    @SuppressWarnings("unchecked")
    public void setListAdapter(ListAdapter adapter) {
        synchronized (this) {
            ensureList();
            mAdapter = (MyPageListAdapter<T>)adapter;
            mList.setAdapter(adapter);
        }
    }

    /**
     * Set the currently selected list item to the specified
     * position with the adapter's data
     *
     * @param position
     */
    public void setSelection(int position) {
        mList.setSelection(position);
    }

    /**
     * Get the position of the currently selected list item.
     */
    public int getSelectedItemPosition() {
        return mList.getSelectedItemPosition();
    }

    /**
     * Get the cursor row ID of the currently selected list item.
     */
    public long getSelectedItemId() {
        return mList.getSelectedItemId();
    }

    /**
     * Get the activity's list view widget.
     */
    public AbsListView getListView() {
        ensureList();
        return mList;
    }

    /**
     * Get the ListAdapter associated with this activity's ListView.
     */
    public ListAdapter getListAdapter() {
        return mAdapter;
    }

    private void ensureList() {
        if (mList != null) {
            return;
        }
        //setContentView(com.android.internal.R.layout.list_content_simple);
    }

    private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id)
        {
            onListItemClick((AbsListView)parent, v, position, id);
        }
    };
    
    //==================下面是有关数据适配器方面的操作=====================================================//    
    /**
     * 获取列表的每个条目view
     * @return
     * @author zhangyz created on 2013-4-14
     */
    public abstract int getItemResLayoutId(int position);
    
    /**
     * 获取列表的每个条目view
     * @return
     * @author zhangyz created on 2013-4-14
     */
    protected int getListViewResLayoutId() {
        return android.R.id.list;
    }
    
    /**
     * 读取一页数据
     * @param pageInfo
     * @return
     * @author zhangyz created on 2013-4-14
     */
    protected abstract List<KvBean<T>> readListPageData (String searchToken,PageInfo pageInfo, NetProcessor.QueryRsProcessor<T> callBack);
    
    /**
     * 创建列表数据适配器
     * @return
     * @author zhangyz created on 2013-4-14
     */
    protected void createAdapter() {
        if (cacheDataItem)
            mAdapter = new MyPageListAdapter<T>(this);
        else {
            mAdapter = new MyPageListAdapter<T>(this){
                @Override
                protected boolean needCacheViewItem() {
                    return false;//因Item不同故必须是false，否则有bug。
                }
            };
        }
    }

    /**
     * 初始化分页器，子类可以继承
     * @return
     */
    protected PageInfo initPageInfo() {
        return new PageInfo(20);
    }

    /**
     * 追加数据并刷新界面
     * @param param
     */
    protected void addDataAndNotify(KvBean<T> param) {
        synchronized (mAdapter) {
            mAdapter.addDataItem(param);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 追加数据并刷新界面
     * @param params
     */
    protected void addDatasAndNotify(List<KvBean<T>> params) {
        synchronized (mAdapter) {
            mAdapter.addDataItems(params);
            mAdapter.notifyDataSetChanged();
        }
    }
    
    /**
     * 读取一页数据
     * @param isAsyncDao 是否异步dao
     * @return
     * @author zhangyz created on 2013-4-14
     */
    protected List<KvBean<T>> doLoadDataFact(boolean isAsyncDao) {
        List<KvBean<T>> ret = null;
        if (mPageInfo.hasNextPage()) {
            mPageInfo.moveToNext();
            if (isAsyncDao) {
              //定义回调函数。
                NetProcessor.QueryRsProcessor<T> callback = new NetProcessor.QueryRsProcessor<T>(mPageInfo) {
                    @Override
                    public void processQueryResult(RspQueryResult<T> rs) {
                        mLoadedCount += rs.getReturnNum();
                        List<KvBean<T>> kvs = KvBean.exportToKvs(rs.getRowDatas());
                        doRefreshViewFact(kvs);
                    }                    
                };                
                readListPageData(searchToken,mPageInfo, callback);//searchToken,支持全文检索
            }
            else {
                ret = readListPageData(searchToken,mPageInfo, null);//searchToken
                mLoadedCount += ret.size();
            }
        }
        return ret;
    }

    /**
     * 更新一页数据
     * @param params
     * @author zhangyz created on 2013-4-14
     */
    @SuppressWarnings("unchecked")
    protected void doRefreshViewFact (Object params) {
        List<KvBean<T>> dataItems = (List<KvBean<T>>)params;
        synchronized (mAdapter) {
            if (mAdapter.haveNoData()) {//第一次读取完毕
                this.setAdapterData(dataItems);
            }
            else {//后续读取完毕dataItems
                if (dataItems != null && dataItems.size() > 0) {
                    if (mPageInfo.isFromLast())
                        mAdapter.insertDataItems(dataItems) ;
                    else
                        mAdapter.addDataItems(dataItems);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }

        if (mPageInfo.isFromLast()) {
            //滚到新载数据集的最后，默认是滚在第一个
            scroolToPos(dataItems.size() - 1);
                /*int y = dataItems.size() * 50;
                if (y > 500)
                    y = 500;
                this.getListView().scrollBy(0, y);*/
        }
        completeFlag = true;
    }
    
    /**
     * 设置数据适配器数据
     * @param dataItems
     * @author zhangyz created on 2013-4-14
     */
    protected void setAdapterData(List<KvBean<T>> dataItems) {
        setListAdapter(mAdapter);
        mAdapter.addDataItems(dataItems);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (!completeFlag) {
            return;//防止重复操作。
        }
        //当滚动停止且滚动的总数等于数据的总数，去加载 
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && mLoadedCount < mPageInfo.getTotalCount()) {
            if (mPageInfo.isFromLast()) {
                if (mFirstItem == 0) {
                    doLoadAndRefreshNext();
                }
            }
            else {
                if (mLastItem >= mLoadedCount) {
                    doLoadAndRefreshNext();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCoun) {
        mLastItem = firstVisibleItem + visibleItemCount;  
        mFirstItem = firstVisibleItem;
    }
    
    /**
     * 滑动到指定位置
     * @param pos
     * @author zhangyz created on 2013-5-12
     */
    public void scroolToPos(int pos) {
        if (pos >= 0) {
            ListView listView = (ListView)getListView();
            if (listView.getHeaderViewsCount() > 0) {// 防止ListView有标题栏，本例中没有。
                listView.setSelectionFromTop(
                        pos + listView.getHeaderViewsCount(), 0);
            }
            else {
                listView.setSelectionFromTop(pos, 0);// 滑动到第一项
            }
        }
        else {
            ;//说明的确没有该字母索引的数据
        }
    }
    
    //==================上面是有关数据适配器方面的操作=====================================================//
}
