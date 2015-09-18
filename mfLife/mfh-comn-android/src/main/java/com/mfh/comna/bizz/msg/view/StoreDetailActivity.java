package com.mfh.comna.bizz.msg.view;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mfh.comna.R;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.view.BaseFragmentActive;
import com.mfh.comna.bizz.msg.logic.EmbSessionService;

/**
 * 小伙伴·周边/商家·详情
 * Created by ZZN on 15-05-28.
 */
public class StoreDetailActivity extends BaseFragmentActive {
    public static final String EXTRA_KEY_CONVERSATION_TYPE = "EXTRA_KEY_CONVERSATION_TYPE";

    private ImageButton ibBack;
    private TextView tvTopBarTitle;

//    private Fragment[] fragments;
    private StoreDetailFragment storeDetailFragment;

    //对话类型：0商铺详情/1个人详情
    private int conversationType = 0;

    private EmbSessionService sessionService;

    @Override
    public int getLayoutId() {
        return R.layout.activity_conversation;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void initActionBar(ActionBar actionBar) {
        super.initActionBar(actionBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionService = ServiceFactory.getService(EmbSessionService.class);

        conversationType = this.getIntent().getIntExtra(EXTRA_KEY_CONVERSATION_TYPE, 0);

        initTopBar();
        initFragments();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        if (outState != null){
//            outState.putInt(EXTRA_KEY_CONVERSATION_TYPE, conversationType);
//        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override//只要发生切换，一定会调用到stop
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {//如果只是锁屏，不会调用destroy。
        super.onDestroy();
    }

    /**
     * 初始化导航栏视图
     * */
    private void initTopBar(){
        tvTopBarTitle = (TextView) findViewById(R.id.topbar_title);
        ibBack = (ImageButton) findViewById(R.id.ib_back);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //TODO,设置标题
        if(conversationType == 0){
            tvTopBarTitle.setText(R.string.topbar_title_store_detail);
        }
//        else{
//            tvTopBarTitle.setText(R.string.topbar_title_store);
//        }
    }

    /**
     * 初始化内容视图
     * Caused by: java.lang.IllegalStateException: commit already called
     * */
    private void initFragments(){
        storeDetailFragment = new StoreDetailFragment();

//        fragments = new Fragment[]{serviceFragment, surroundFragment};
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, storeDetailFragment)
                .hide(storeDetailFragment)
                .commit();

        if(conversationType == 0){
            getSupportFragmentManager().beginTransaction().show(storeDetailFragment).commit();
        }
    }

}
