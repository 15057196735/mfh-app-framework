package com.mfh.comna.view;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import com.mfh.comna.comn.logic.IAdapterLetterIndexAble;
import com.mfh.comna.comn.bean.ILetterIndexAble;
import com.mfh.comna.comn.bean.KvBean;
import com.mfh.comna.comn.logic.LetterPageAdapter;
import com.mfh.comna.comn.logic.LetterPageHelper;
import com.mfh.comna.comn.logic.MyPageListAdapter;
import com.mfh.comna.R;
import com.mfh.comna.utils.DensityUtil;

/**
 * 支持通过字母索引的列表Fragment，典型的可以用于通讯录场景：一个左边的列表 + 右边的字母索引竖长条。
 * 1、列表可以按照字母顺序排序；
 * 2、所有记录按找归属字母分组显示，每组开头显示字母分割线
 * 3、外界通过调用initLetterIndexView方法将字母索引条与之关联。
 * 4、当按中索引条中的某字母时，会在list中央以大号字显示该字母块,并将列表滚动到该字母开头的条目。
 * 若该条目还未加载，则自动再从后台读取数据直至将该条目加载进来。
 * 但还有个限制，譬如超过2000条还未加载到该条目，则放弃加载以防止内存爆掉。 
 * 
 * 5、子类可以覆盖getLayoutId(),以定制不同的list和大号字母块显示,默认是R.layout.comn_list_tv_view
 * 若要覆盖索引条onTouch事件，可以覆盖子类LivTouchListener。
 * 6、子类可以覆盖fillLetterIndexView()方法以改变字母索引bar的填充样式。
 * 7、子类可以覆盖getItemResItemFirstLytId()方法以提供自己的关于首字母条目的样式。
 * 8、继承createAdapter()方法，传递字母的分组信息
 * 
 * @param <T>
 * @author zhangyz created on 2013-5-13
 * @since Framework 1.0
 */
public abstract class BaseListIdxAbleFmt <T extends ILetterIndexAble> extends BaseListFragment<T> {  
    private boolean readTaskFlag = false;//后台读取数据标志,放在连续不断地重复读取。
    private ProgressDialog dialog;
    private TextView tv_show;
    protected ViewGroup layoutIndex;
    private ScrollView scrollView;

    /**
     * 绘制区段内第一个item
     * @param summary
     * @param listItemView
     * @param parent
     * @author zhangyz created on 2013-5-13
     */
    //protected abstract void fillFirstListItemView(T summary, View listItemView, ViewGroup parent);


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initLetterIndexView(getActivity(),layoutIndex,R.color.gray);
    }

    protected void fillFirstListItemView(KvBean<T> summary, View listItemView, ViewGroup parent,int position) {
        View sectionView = listItemView.findViewById(R.id.comnListFirstItemLayout);
        sectionView.setVisibility(View.VISIBLE);
        TextView tv = (TextView)sectionView.findViewById(R.id.letterIndexName);
        tv.setText(summary.getBean().getLetterIndex());

        LayoutInflater inflater = (LayoutInflater)parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View innerView = inflater.inflate(getItemResItemComnLytId(position), parent, false);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        params.weight = 1;
        int margin = DensityUtil.dip2px(getContext(),10);
        params.setMargins(0,margin,0,margin);
        ((LinearLayout)listItemView).addView(innerView, params);
        fillComnListItemView(summary, innerView, parent);
        if (innerView.getTag() == null)
            innerView.setTag(listItemView.getTag());
        
        /*View userLayout = listItemView.findViewById(R.id.staffUserItemLayout);
        fillUserInfoPart(userLayout, summary);
        listItemView.setTag(summary);*/
    }
    
    /**
     * 绘制区段内其他item
     * @param summary
     * @param listItemView
     * @param parent
     * @author zhangyz created on 2013-5-13
     */
    protected abstract void fillComnListItemView(KvBean<T> summary, View listItemView, ViewGroup parent);
        
    /**
     * 获取一个区段之首的条目layout
     * @return
     * @author zhangyz created on 2013-5-13
     */
    protected int getItemResItemFirstLytId() {
        return R.layout.comn_list_tv_item;
    }
    
    /**
     * 获取其他普通的条目layout
     * @return
     * @author zhangyz created on 2013-5-13
     */
    protected abstract int getItemResItemComnLytId(int position);
    
    @Override
    public int getLayoutId() {
        return R.layout.comn_list_tv_view;
    }

    @Override
    protected void createViewInner(View rootView, ViewGroup container, Bundle savedInstanceState) {
        super.createViewInner(rootView, container, savedInstanceState);
        tv_show = (TextView)rootView.findViewById(R.id.letterCurSelect);
        layoutIndex = (LinearLayout) rootView.findViewById(R.id.ll_idx);
        scrollView = (ScrollView) rootView.findViewById(R.id.sv_index);

        scrollView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tv_show.setVisibility(View.GONE);
                return false;
            }
        });
    }


    
    /**
     * 填充字母索引bar中的内容，子类可以覆盖
     * @param context
     * @param layoutIndex
     * @author zhangyz created on 2013-5-18
     */
    public void fillLetterIndexView(Context context, ViewGroup layoutIndex) {
        this.layoutIndex = layoutIndex;
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        params.setMargins(10, 2, 10, 2);
        layoutIndex.removeAllViews();
        for (int ii = 0; ii < LetterPageHelper.indexLetters.length; ii++) {
            final TextView tv = new TextView(context);
            tv.setLayoutParams(params);
            tv.setText(String.valueOf(LetterPageHelper.indexLetters[ii]));
            tv.setTextColor(Color.parseColor("#606060"));
            tv.setTextSize(15);
            layoutIndex.addView(tv);            
        }
    }
    
    /**
     * 只初始化字母索引区的监听事件,不需要再绘制字母索引区。这样可以多次使用,只绘制一次。
     * @param layoutIndex
     * @param downBackColor
     * @author zhangyz created on 2013-5-19
     */
    public void initLetterIndexViewListen(ViewGroup layoutIndex, int downBackColor) {
        this.layoutIndex = layoutIndex;
        LivTouchListener  touchListener = new LivTouchListener(layoutIndex, downBackColor);
        layoutIndex.setOnTouchListener(touchListener);
    }
    
    /**
     * 初始化字母索引bar
     * @param layoutIndex
     * @author zhangyz created on 2013-5-13
     */
    public void initLetterIndexView(Context context, ViewGroup layoutIndex, int downBackColor) {
        fillLetterIndexView(context, layoutIndex);
        initLetterIndexViewListen(layoutIndex, downBackColor);
    }
    
    @Override
    public void fillListItemView(KvBean<T> bean, View listItemView, int position, ViewGroup parent) {
        boolean isFirst = bSectionFirst(position, bean);      
        if (!isFirst) {
            fillComnListItemView(bean, listItemView, parent);
        }
        else {
            fillFirstListItemView(bean, listItemView, parent, position);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        ;
    }

    /**
     * 判断是否位于区段之首
     * @param position
     * @return
     * @author zhangyz created on 2013-5-12
     */
    protected boolean bSectionFirst(int position) {
        MyPageListAdapter<T> ladapter = ((MyPageListAdapter<T>)mAdapter);
        KvBean<T> summary = ladapter.getItem(position);
        String letterIndexPreName = summary.getBean().getLetterIndex();
        if (letterIndexPreName == null)
            return false;
        Integer pos = ((IAdapterLetterIndexAble)mAdapter).getSelector().get(letterIndexPreName);
        if (pos != null && (pos.intValue() == position)) 
            return true;
        else
            return false;
    }

    /**
     * 判断是否位于区段之首
     * @param position
     * @return
     * @author zhangyz created on 2013-5-12
     */
    protected boolean bSectionFirst(int position, KvBean<T> summary) {
        String letterIndexPreName = summary.getBean().getLetterIndex();
        if (letterIndexPreName == null)
            return false;
        IAdapterLetterIndexAble ladapter = ((IAdapterLetterIndexAble)mAdapter);
        Integer pos = ladapter.getSelector().get(letterIndexPreName);
        if (pos != null && (pos.intValue() == position))
            return true;
        else
            return false;
    }
    
    @Override
    public int getItemResLayoutId(int position) {//adapterd needCacheViewItem()配合使用false
        if (bSectionFirst(position)) 
            return getItemResItemFirstLytId();
        else
            return getItemResItemComnLytId(position);
    }
    
    /**
     * 提供索引字符集,返回null，则代表使用A、B、C.....
     * @return
     * @author zhangyz created on 2014-3-12
     */
    protected abstract String[] collectIndexLetter();

    @Override
    protected MyPageListAdapter<T> createAdapter() {
        String[] paramLetters = collectIndexLetter();
        return new LetterPageAdapter<T>(this, paramLetters);
    }
    
    @Override
    public ProgressDialog onPreExecute(int taskKind) {
        return null;
    }
    
    /**
     * 启动读取下一页的操作，以便读取的条目中有包含指定的字母索引
     * @param letterIndexName
     */
    public synchronized void doReadNextPageUtil(String letterIndexName) {
        if (readTaskFlag)
            return;
        if (mLoadedCount < mPageInfo.getTotalCount()) {
            readTaskFlag = true;
            dialog = ProgressDialog.show(this.getActivity(), "请稍等...", "正在处理中...", true);
            this.doAsyncTaskWithParam(letterIndexName, 1);//1代表第一次
        }
    }
    
    /**
     * 内部逻辑，循环，读取下一页直至读取的条目中有包含指定的字母索引，与onPostExecuteOfMain配合形成一个循环。
     * @param letterIndexName
     * @param count 第几次读取，1代表第一次
     * @return false:已经读完。
     * @author zhangyz created on 2013-5-12
     */
    private boolean doReadNextPageLoop(String letterIndexName, int count) {
        readTaskFlag = true;
        if (mLoadedCount < mPageInfo.getTotalCount() && mLoadedCount < 2000) {//最多支持循环读取2000条
            this.doAsyncTaskWithParam(letterIndexName, count);//1代表第一次
            return true;
        }
        else
            return false;
    }
    
    @Override
    protected void doRefreshViewFact (Object result, Object...params) {
//        @SuppressWarnings("unchecked")
        List<KvBean<T>> dataItems = (List<KvBean<T>>)result;
//        Collections.sort(dataItems, new Comparator<KvBean<T>>() {
//            @Override
//            public int compare(KvBean<T> lhs, KvBean<T> rhs) {
//                return lhs.getBean().getLetterIndex().compareTo(rhs.getBean().getLetterIndex());
//            }
//        });
        
        //先进行排序
        super.doRefreshViewFact(dataItems, params);
        if (params != null && params.length > 0) {
            String letterIndexName = (String)(params[0]);
            int count = (Integer)(params[1]);
            if (letterIndexName != null) {
                IAdapterLetterIndexAble ladapter = ((IAdapterLetterIndexAble)mAdapter);
                Integer pos = ladapter.getSelector().get(letterIndexName);
                if (pos == null) {
                    if (!doReadNextPageLoop(letterIndexName, ++count)){//继续触发读取
                        clearReadLoop();
                        ladapter.getSelector().put(letterIndexName, -1);//代表已经处理过该字母索引了，只是没有数据。
                    }
                }
                else {
                    clearReadLoop();
                    scroolToPos(pos);//滚动到指定位置
                }
            }
        }
    }
    
    /**
     * 停止执行后的清理工作
     * 
     * @author zhangyz created on 2013-5-12
     */
    private void clearReadLoop() {
        readTaskFlag = false;
        try {
            if (dialog != null)
                dialog.dismiss();
        }
        catch(Throwable e) {
            ;
        }        
    }
    
    /**
     * 字母索引区touch事件
     * 
     * @author zhangyz created on 2013-5-13
     * @since Framework 1.0
     */
    public class LivTouchListener implements OnTouchListener{
        private int downBackColor;
        private ViewGroup layoutIndex;
        
        public LivTouchListener(ViewGroup layoutIndex, int downBackColor) {
            super();
            this.downBackColor = downBackColor;
            this.layoutIndex = layoutIndex;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float y = event.getY() - layoutIndex.getTop();
            int letterHeight = layoutIndex.getHeight() / LetterPageHelper.indexLetters.length;//单个字母块高度
            int index = (int) (y / letterHeight);
            
            IAdapterLetterIndexAble adapter = (IAdapterLetterIndexAble)getListAdapter();            
            if (index > -1 && index < LetterPageHelper.indexLetters.length) {// 防止越界
                String key = LetterPageHelper.indexLetters[index];
                tv_show.setVisibility(View.VISIBLE);
                tv_show.setText(LetterPageHelper.indexLetters[index]);
                
                if (adapter.getSelector().containsKey(key)) {
                    int pos = adapter.getSelector().get(key);
                    scroolToPos(pos);
                }
                else {
                    doReadNextPageUtil(key);//读取下一页
                }
            }
            else {
                tv_show.setText("");
                tv_show.setVisibility(View.INVISIBLE);
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    layoutIndex.setBackgroundColor(getResources().getColor(downBackColor));
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    layoutIndex.setBackgroundColor(Color.parseColor("#00ffffff"));
                    tv_show.setVisibility(View.INVISIBLE);
                    break;
            }                
            return true;
        }
    }
}
