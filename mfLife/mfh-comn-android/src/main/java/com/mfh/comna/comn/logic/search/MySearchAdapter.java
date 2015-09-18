package com.mfh.comna.comn.logic.search;


import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

/**
 * 在activity或fragment中支持搜索
 * 其中searchView可以在actionbar上或页面上，这个由子类自己去放置就行了，但是需要在初始化时明确调用initSearchView方法。
 * 如：来自于actionbar,在onCreateOptionsMenu中：SearchView searchView = (SearchView)menu.getItem(0).getActionView()；
 * 来自于页面，在onCreate中：SearchView searchView = (SearchView)this.findViewById(R.id.indiSearchView)
 * 若改变配置可以再configSearchView()方法。
 * 然后有doSearch和genSuggestionProvider方法需要实现，后者可以返回null
 * @author zhangyz created on 2013-5-17
 * @since Framework 1.0
 */
public abstract class MySearchAdapter implements SearchView.OnQueryTextListener, SearchView.OnSuggestionListener {
    public static String[] cursorCols = new String[] { BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1 };
    private boolean needSuggest = true;// 是否需要搜索建议
    protected MySuggestionProvider suggestionAdapter;
    protected SearchView searchView;    
    private Context context;
    private String preQueryToken = "";//初始的检索串
    
    /**
     * 构造函数
     * @param context 上下文环境
     */
    public MySearchAdapter(Context context) {
        super();
        this.context = context;
    }
    
    /**
     * 由子类构造一个搜索建议提供者，若为null，代表无需使用搜索建议
     * @return
     * @author zhangyz created on 2013-5-17
     */
    public MySuggestionProvider genSuggestionProvider() {return null;}

    /**
     * 查询最终数据
     * 
     * @param queryToken
     * @author zhangyz created on 2013-5-17
     */
    public abstract void doSearch(String queryToken);

    /**
     * 对工具栏上的搜索控件进行外观设置
     * 
     * @author zhangyz created on 2013-5-17
     */
    protected void configSearchView(SearchView searchView, int resQueryHint) {
        searchView.setIconifiedByDefault(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        params.setMargins(10, 5, 5, 10);
        params.gravity = Gravity.CENTER;
        searchView.setLayoutParams(params);
//        searchView.setMaxWidth(450);
        searchView.setQueryHint(context.getString(resQueryHint));
    }

    /**
     * 初始化搜索控件
     * @param resQueryHint 搜索什么的提示字符串
     * @author zhangyz created on 2013-5-17
     */
    public void initSearchView(SearchView searchView, int resQueryHint) {
        this.searchView = searchView;
        configSearchView(searchView, resQueryHint);

        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);
        suggestionAdapter = genSuggestionProvider();
        if (suggestionAdapter != null) {
            needSuggest = true;
            searchView.setSuggestionsAdapter((CursorAdapter) suggestionAdapter);
            searchView.setOnSuggestionListener(this);
        }
        else
            needSuggest = false;

        // 不通过搜索框架转发了，直接自己处理,实现OnQueryTextListener之类；若需要通过搜索框架转发,则需要把接收目标activity在工程配置文件里注明。
        /*
         * SearchManager searchManager = (SearchManager) getSystemService(android.content.Context.SEARCH_SERVICE);
         * //使用getComponentName(),相当于自己就是可以接收search事件的;但很别扭，相当于重启一个本Activity的实例 android.content.ComponentName cmName =
         * new android.content.ComponentName(this, IndiSearchActivity.class);//this.getComponentName()
         * searchView.setSearchableInfo(searchManager.getSearchableInfo(cmName));
         */
    }

    @Override
    public boolean onQueryTextChange(String query) {//当查询文本改变时的回调函数,可以在此改变搜索建议数据
        if (searchView == null) {
            Toast.makeText(context, "请先调用initSearchView方法", Toast.LENGTH_LONG).show();
            return false;
        }
        if (query == null || preQueryToken.equals(query))//当发生切换时，莫名其妙会被调用，但传入的是空串。
            return false;
        preQueryToken = query;
        
        if (needSuggest) {// 查询建议数据
            Cursor newCursor = suggestionAdapter.querySuggestData(query);
            searchView.getSuggestionsAdapter().swapCursor(newCursor);
        }
        else {// 查询最终数据
            doSearch(query);
        }
        return true;
    }

    /**
     * 当返回false相当于交由搜索框架去处理，返回true代表自行处理了。其他同理。 (non-Javadoc)
     * 
     * @see android.widget.SearchView.OnQueryTextListener#onQueryTextSubmit(String)
     */
    @Override
    public boolean onQueryTextSubmit(String query) {// 当查询文本改变时的回调函数
        doSearch(query);
        return true;
    }

    @Override
    public boolean onSuggestionClick(int position) {// 在查询建议上选择事件时的回调接口
        if (suggestionAdapter != null)
            doSearch(suggestionAdapter.getSuggestionToken(position));
        return true;
    }

    @Override
    public boolean onSuggestionSelect(int position) {// 在查询建议上选择事件时的回调接口
        return false;
    }
}
