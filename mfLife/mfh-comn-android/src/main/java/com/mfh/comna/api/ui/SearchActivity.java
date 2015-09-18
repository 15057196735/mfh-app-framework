package com.mfh.comna.api.ui;


import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.mfh.comna.api.adapter.SearchConversationAdapter;
import com.mfh.comna.api.helper.SharedPreferencesHelper;
import com.mfh.comna.api.widgets.CustomSearchView;
import com.mfh.comna.bizz.login.logic.MfhLoginService;
import com.mfh.comna.bizz.msg.dao.EmbSessionDao;
import com.mfh.comna.bizz.msg.entity.EmbSession;
import com.mfh.comna.bizz.msg.logic.EmbSessionService;
import com.mfh.comn.bean.PageInfo;
import com.mfh.comna.R;
import com.mfh.comna.comn.bean.KvBean;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.view.BaseFragmentActive;

import java.util.List;


/**
 * 搜索·会话
 *
 * */
public class SearchActivity extends BaseFragmentActive {
    private CustomSearchView searchView;
    private Button btnCancel;
    private ListView mListView;
    private SearchConversationAdapter mAdapter;

    private EmbSessionDao sessionDao;

    private static final int MAX_PAGE_SIZE = 100;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionDao = ServiceFactory.getService(EmbSessionService.class).getDao();

        btnCancel = (Button) findViewById(R.id.button_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        initSearchViewEX();

        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new SearchConversationAdapter();
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        DeviceUtils.hideSoftInput();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sessionDao == null){
            sessionDao = ServiceFactory.getService(EmbSessionService.class).getDao();
        }
    }

    /**
     * 初始化搜索框
     * */
    private void initSearchViewEX(){
        searchView = (CustomSearchView) findViewById(com.mfh.comna.R.id.searchBar);
        searchView.setHint(com.mfh.comna.R.string.search_bar_hint_conversation);
        searchView.setListener(new CustomSearchView.CustomSearchViewListener() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                doSearchWork(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void doSearch(String queryText) {
                //TODO
                doSearchWork(queryText);
            }

        });
    }

    private void doSearchWork(String queryText){
        if(TextUtils.isEmpty(queryText)){
            mAdapter.clearData();
            mAdapter.notifyDataSetChanged();
            return;
        }

        PageInfo pageInfo = new PageInfo(0, MAX_PAGE_SIZE);
        List<EmbSession> result = sessionDao.queryMySessions(MfhLoginService.get().getLoginName(),
                queryText, pageInfo);

        mAdapter.clearData();
        mAdapter.addDataItems(KvBean.exportToKvsDirect(result));
        mAdapter.notifyDataSetChanged();
    }

}
