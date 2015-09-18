package com.mfh.comna.view;



import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mfh.comna.api.utils.MLog;
import com.mfh.comn.bean.IObject;
import com.mfh.comn.bean.PageInfo;
import com.mfh.comn.net.data.RspQueryResult;
import com.mfh.comna.R;
import com.mfh.comna.actionbar.BaseActionBar.OnActionBarListener;
import com.mfh.comna.comn.bean.KvBean;
import com.mfh.comna.comn.database.dao.IDao;
import com.mfh.comna.comn.database.dao.NetProcessor;
import com.mfh.comna.comn.logic.BaseService;
import com.mfh.comna.comn.logic.IBaseViewComponent;
import com.mfh.comna.comn.logic.IFillItemView;
import com.mfh.comna.comn.logic.MyMultiAsyncTask;
import com.mfh.comna.comn.logic.MyPageListAdapter;

import java.util.List;

/**
 * 支持翻页list的fragment
 * 一次显示一页，翻到底部时触发读取新的一页。
 * 可以是listView、gridView等。
 * <T>是列表显示的bean类型。
 * 
 * @author zhangyz created on 2013-4-16
 * @since Framework 1.0
 */
public abstract class BaseListFragment<T extends IObject> extends BaseFragment
        implements OnClickListener, OnActionBarListener,
        IBaseViewComponent<Object, Object>, OnScrollListener,
        IFillItemView<T>, OnItemClickListener{

    public static final int PAGE_SIZE_DEF = 20;

    public static final int STATE_NONE = 0;
    public static final int STATE_REFRESH = 1;
    public static final int STATE_LOADMORE = 2;
    public static final int STATE_NOMORE = 3;
    public static final int STATE_PRESSNONE = 4;// 正在下拉但还没有到刷新的状态
    public static int mState = STATE_NONE;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    protected AbsListView mListView;//真正的列表控件，android原生
    //这两个原来也是ListActivity中成员，但类型被改了。
    protected MyPageListAdapter<T> mAdapter = null;

    protected PageInfo mPageInfo = null;
    private int mLastItem, mFirstItem;//内部变量
    protected int mLoadedCount = 0;//已经加载的指标项条数

    private String searchToken;//是否有预置的模糊检索条件
    protected boolean completeFlag = true;//当前是否已经加载完毕数据和刷新操作。
    protected boolean cacheDataItem = true;//是否缓存数据条目

    private boolean isOnece = true;


    /**
     * 布局ID
     * */
    @Override
    public int getLayoutId() {
        return 0;
    }

    /**
     * 初始化视图
     * */
    @Override
    protected void createViewInner(View rootView, ViewGroup container, Bundle savedInstanceState) {
        setupSwipeRefresh();

        mListView = (AbsListView)rootView.findViewById(getListViewResId()); //com.android.internal.R.id.list
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);

        mHandler.post(mRequestFocus);
    }

    /**
     * 初始化分页器，子类可以继承
     * @return
     */
    protected PageInfo initPageInfo() {
        return new PageInfo(PAGE_SIZE_DEF);
    }


    /**
     * 刷新加载更多
     * */
    public void refreshToLoadMore(){
        // 设置顶部正在刷新
        mListView.setSelection(0);
        setRefreshing(true);
//        mCurrentPage = 0;
//        doLoadAndRefreshNext();
    }

    /**
     * 设置预置的模糊检索条件
     * @param searchToken
     * @author zhangyz created on 2014-3-11
     */
    public void setSearchToken(String searchToken) {
        this.searchToken = searchToken;
    }

    public BaseListFragment() {
        super();
        mPageInfo = initPageInfo();
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


    //异步任务正在执行
    @Override
    public Object doInBackground(int taskKind, Object... params) {
        if (taskKind == MyMultiAsyncTask.TASK_KIND_DEFAULT) {
            return doLoadDataFact(false);
        }
        else
            return null;
    }

    @Override
    public void doInBackgroundException(int taskKind, Throwable ex, Object... params) {
        super.doInBackgroundException(taskKind, ex, params);
        if (taskKind == MyMultiAsyncTask.TASK_KIND_DEFAULT) {
            this.completeFlag = true;//复位
            notifyExecuteOnLoadFinish();
        }
    }

    //异步任务执行完毕
    @Override
    public void onPostExecute(int taskKind, Object result, Object...params) {
        if (taskKind == MyMultiAsyncTask.TASK_KIND_DEFAULT) {
            doRefreshViewFact(result, params);
        }
        else {

        }
    }

    /**
     * dao本身是否异步
     * @return
     * @author zhangyz created on 2014-3-12
     */
    protected boolean isAsyncDao() {
        return IDao.isAyncDao;
    }

    //==================下面是有关ListActivity的源码，从开源中拷贝过来=====================================//
    //private boolean mFinishedStart = false;
    private Runnable mRequestFocus = new Runnable() {
        public void run() {
            mListView.focusableViewAvailable(mListView);
        }
    };

    /**
     *
     * 初始化，加载并显示数据
     */
    protected void loadDataOnInit() {
        //读取数据加载界面
        completeFlag = true;
        if (mAdapter == null) {//在fragment重新被激活时，又会调用此方法。
            doLoadAndRefreshStart();
        }
        else {
//            completeFlag = false;
            //zhangzn@20150427, 注释掉该行，修复切换页面后搜索功能失效问题。
//            Log.d("Nat: .completeFlag", String.valueOf(completeFlag));
            mListView.setAdapter(mAdapter);
            //scroolToPos(mFirstItem);//没有用。因为frgment切换时onScroll会调用。
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getService() == null) {
            loadDataOnInit();
            return;
        }
        getService().initAllSyncDataLogic();
        loadDataOnInit();
    }

    /**
     * 刷新，重新触发异步加载数据并刷新界面
     * @author zhangyz created on 2013-5-3
     */
    public void doLoadAndRefreshStart() {
        if (!completeFlag){
            return;//防止重复操作。
        }
        completeFlag = false;//表示正在加载
        resetPage();
        if (mAdapter != null) {
            mAdapter.clearData();
            mAdapter.notifyDataSetChanged();
        }
        else{
            mAdapter = createAdapter();
        }

        mState = STATE_NONE;
        doLoadAndRefreshNext();
    }

    /**
     * 执行实际的下一页加载。
     * 读取下一页,方向可能是顺序或反序
     */
    public void doLoadAndRefreshNext() {
        if (this.isAsyncDao()) {
            doLoadDataFact(true);
        }
        else {
            //执行异步加载数据，后面会执行到doLoadDataFact
            doAsyncTask();
        }
    }

    /**
     * 读取一页数据.这在后台执行。
     * @param isAsyncDao dao本身是否异步执行,如果是则不需要再用fragment的异步任务框架。
     * @return
     * @author zhangyz created on 2013-4-14
     */
    protected List<KvBean<T>> doLoadDataFact(boolean isAsyncDao) {
        List<KvBean<T>> ret = null;
        if (mPageInfo.hasNextPage()) {
            mPageInfo.moveToNext();
            //异步dao,dao本身异步，返回里面执行doRefreshViewFact()
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
                readListPageData(searchToken, mPageInfo, callback);//无须返回
            }
            else {//同步dao，读取完之后还会调用doRefreshViewFact()刷新页面.
                ret = readListPageData(searchToken, mPageInfo, null);
                if (ret != null){
                    mLoadedCount += ret.size();
                }

                notifyExecuteOnLoadFinish();
            }
        }
        else{
            MLog.d("数据已经加载完毕，NO MORE DATA");
            notifyExecuteOnLoadFinish();
        }
        return ret;
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
     *
     */
    protected void addDatasAndNotify(List<KvBean<T>> params) {
        synchronized (mAdapter) {
            mAdapter.addDataItems(params);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 更新一页数据
     * @param params
     * @author zhangyz created on 2013-4-14
     */
    @SuppressWarnings("unchecked")
    protected void doRefreshViewFact (Object result, Object...params) {
        List<KvBean<T>> dataItems = (List<KvBean<T>>)result;
        synchronized (mAdapter) {//有时可能还有其他线程也在操作，此时就要同步
            if(dataItems != null){
                MLog.d("dataItems.size", String.valueOf(dataItems.size()));
            }

            if (mAdapter.haveNoData()) {//第一次读取完毕
                this.setAdapterData(dataItems);
            }
            else {//后续读取完毕dataItems
                if (dataItems != null && dataItems.size() > 0) {
//                    Log.d("Nat: doRefreshViewFact.dataItems.size", String.valueOf(dataItems.size()));
                    if (mPageInfo.isFromLast()){
                        mAdapter.insertDataItems(dataItems) ;
                    }
                    else{
                        mAdapter.addDataItems(dataItems);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
        if (mPageInfo.isFromLast() && dataItems != null) {
            //滚到新载数据集的最后，默认是滚在第一个
            scroolToPos(dataItems.size() - 1);
                /*int y = dataItems.size() * 50;
                if (y > 500)
                    y = 500;
                this.getListView().scrollBy(0, y);*/
        }
        completeFlag = true;
        notifyExecuteOnLoadFinish();

        if (isOnece && (null == dataItems || 0 == dataItems.size())) {
            syncDataFromFrontToEnd();
            isOnece = false;
        }
    }

    /*@Override
    protected void onRestoreInstanceState(Bundle state) {
        ensureList();
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(mRequestFocus);
        super.onDestroy();
    }*/

    /**
     * Provide the cursor for the list view.
     */
    @SuppressWarnings("unchecked")
    public void setListAdapter(ListAdapter adapter) {
        synchronized (this) {
            ensureList();
            mAdapter = (MyPageListAdapter<T>)adapter;
            mListView.setAdapter(adapter);
        }
    }

    /**
     * Set the currently selected list item to the specified
     * position with the adapter's data
     *
     * @param position
     */
    public void setSelection(int position) {
        mListView.setSelection(position);
    }

    /**
     * Get the position of the currently selected list item.
     */
    public int getSelectedItemPosition() {
        return mListView.getSelectedItemPosition();
    }

    /**
     * Get the cursor row ID of the currently selected list item.
     */
    public long getSelectedItemId() {
        return mListView.getSelectedItemId();
    }

    /**
     * Get the activity's list view widget.
     */
    public AbsListView getListView() {
        ensureList();
        return mListView;
    }

    /**
     * Get the ListAdapter associated with this activity's ListView.
     */
    public MyPageListAdapter<T> getListAdapter() {
        return mAdapter;
    }

    private void ensureList() {
        if (mListView != null) {
            return;
        }
        //setContentView(com.android.internal.R.layout.list_content_simple);
    }

    //==================下面是有关数据适配器和页面方面的操作=====================================================//

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
    protected int getListViewResId() {
        return android.R.id.list;
    }



    /**
     * 读取一页数据（含有内置的模糊搜索条件）
     * @param searchToken 模糊搜索条件，可以为空
     * @param pageInfo 分页信息
     * @param callBack 如果是异步dao则，需要此回调函数
     * @return
     * @author zhangyz created on 2013-4-14
     */
    protected abstract List<KvBean<T>> readListPageData (String searchToken,
                              PageInfo pageInfo, NetProcessor.QueryRsProcessor<T> callBack);

    /**
     * 读取总数
     * @param searchToken
     * @param callBack
     * @param callBack 如果是异步dao则，需要此回调函数
     * @return
     */
    /*protected Integer readListCount(String searchToken, NetProcessor.ComnProcessor<Integer> callBack ) {
        return null;
    }*/

    /**
     * 创建列表数据适配器
     * @return
     * @author zhangyz created on 2013-4-14
     */
    protected MyPageListAdapter<T> createAdapter() {
        if (cacheDataItem)
            return new MyPageListAdapter<T>(this);
        else {
            return new MyPageListAdapter<T>(this){
                @Override
                protected boolean needCacheViewItem() {
                    return false;//因Item不同故必须是false，否则有bug。
                }
            };
        }
    }



    /**
     * 设置数据适配器数据
     * @param dataItems
     * @author zhangyz created on 2013-4-14
     */
    private void setAdapterData(List<KvBean<T>> dataItems) {
        //            //TODO,test data for parent scrollview
//            for(int i=0; i<10; i++){
//                mAdapter.addDataItems(dataItems);
//            }
        mAdapter.addDataItems(dataItems);
        mListView.setAdapter(mAdapter);
    }


    protected boolean isBottom = true;//true： 往上；fasle:往下
    private int lastLastPos = -1;
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (!completeFlag){
            return;//防止重复操作。
        }

        //当滚动停止且滚动的总数等于数据的总数，去加载
//        屏幕停止滚动
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            mFirstItem = view.getFirstVisiblePosition();
            if (mLoadedCount < mPageInfo.getTotalCount()) {
                if (mPageInfo.isFromLast()) {
                    //滚动到顶部
                    if (mFirstItem == 0) {// && !isBottom
                        doLoadAndRefreshNext();
                    }
                }
                else {
                    if (mLastItem >= mLoadedCount) {
                        doLoadAndRefreshNext();
                    }
                }
            }
//            else if (mLoadedCount == mPageInfo.getTotalCount()){
//                syncDataFromFrontToEnd();
//            }
        }
    }

    protected boolean isTop = false;
    //public boolean isDown = false;
    //滚动完毕调用
    @Override
    public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCoun) {
        mFirstItem = firstVisibleItem;
        mLastItem = firstVisibleItem + visibleItemCount;

        if (mLastItem == totalItemCoun) {
            isBottom = true;
        }
        else{
            isBottom = false;
        }

        if (mFirstItem == 0) {
            isTop = true;
        }
        else{
            isTop = false;
        }

//        //修复列表为空时不能滑动加载数据问题
//        if(isTop && totalItemCoun == 0){
//            doLoadAndRefreshNext();
//        }

       /* if (lastLastPos == -1)
            lastLastPos = mLastItem;
        else {
            if (mLastItem > lastLastPos)
                isDown = false;
            else if (mLastItem < lastLastPos)
                isDown = true;
            lastLastPos = mLastItem;
        }*/
    }

    private void syncDataFromFrontToEnd() {
        if (getService() != null) {
            getService().syncDataFromFrontToEnd(2, new BaseService.SyncDataCallBack() {

                @Override
                public void success() {
                    doLoadAndRefreshStart();
                }

                @Override
                public void fail() {
                    showHint("获取数据失败");
                }
            });
        }
    }


    public boolean onTouchEvent(MotionEvent event) {
        final int y = (int)event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP: {
                isBottom = false;
                break;
            }
            case MotionEvent.ACTION_DOWN: {
                isBottom = true;
                break;
            }
        }
        return true;
    }



    /**
     * 滑动到指定位置
     * @param pos
     * @author zhangyz created on 2013-5-12
     */
    protected void scroolToPos(int pos) {
        if (pos >= 0) {
            ListView listView = (ListView)getListView();
            if (listView.getHeaderViewsCount() > 0) {// 防止ListView有标题栏，本例中没有。
                listView.setSelectionFromTop(
                        pos + listView.getHeaderViewsCount(), 0);
            }
            else {
                listView.setSelectionFromTop(pos, 0);// 滑动到第一项
            }
            //自动滚时
            if (mFirstItem != pos) {
                mFirstItem = pos;
                mLastItem = mFirstItem + mPageInfo.getPageSize();//确切位置不知道
            }
        }
        else {
            ;//说明的确没有该字母索引的数据
        }
    }

    //==================上面是有关数据适配器方面的操作=====================================================//

    @Override
    public boolean isItemEnabled(int position) {
        return true;
    }

    /**
     * 获取该界面获取数据的service
     */
    public abstract BaseService getService();

    private static final int MSG_LOAD_FINISHED = 0;

    private Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_LOAD_FINISHED:{
                    setRefreshing(false);
                }
                break;
            }
        }
    };

    /**
     * 完成刷新
     */
    protected void notifyExecuteOnLoadFinish(){
        Message msg = new Message();
        msg.what = MSG_LOAD_FINISHED;
        uiHandler.sendMessage(msg);
    }

    /**
     * 设置刷新
     * */
    private void setupSwipeRefresh(){
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefreshlayout);
        if(mSwipeRefreshLayout != null){
            mSwipeRefreshLayout.setColorSchemeResources(
                    R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                    R.color.swiperefresh_color3, R.color.swiperefresh_color4);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (mState == STATE_REFRESH) {
                        MLog.d("正在刷新");
                        return;
                    }

                    //TODO
                    refreshToLoadMore();
                }
            });
        }
        mState = STATE_NONE;
    }

    /**
     * 设置刷新状态
     * */
    public void setRefreshing(boolean refreshing) {
        if (refreshing) {
            setSwipeRefreshLoadingState();
        } else {
            setSwipeRefreshLoadedState();
        }
    }



    /** 设置顶部正在加载的状态 */
    private void setSwipeRefreshLoadingState() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
            // 防止多次重复刷新
            mSwipeRefreshLayout.setEnabled(false);


            mState = STATE_REFRESH;
        }
    }

    /** 设置顶部加载完毕的状态 */
    private void setSwipeRefreshLoadedState() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);


            mState = STATE_NONE;
        }
    }



}
