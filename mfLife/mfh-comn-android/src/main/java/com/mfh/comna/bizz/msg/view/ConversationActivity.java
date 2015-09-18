package com.mfh.comna.bizz.msg.view;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mfh.comna.R;
import com.mfh.comna.view.BaseFragmentActive;

/**
 * 小伙伴·周边/商家
 * Created by ZZN on 15-05-28.
 */
public class ConversationActivity extends BaseFragmentActive {
    public static final String EXTRA_KEY_CONVERSATION_TYPE = "EXTRA_KEY_CONVERSATION_TYPE";

    private ImageButton ibBack;
    private TextView tvTopBarTitle;
    private Button btnMore;

//    private Fragment[] fragments;
    private ConversationSurroundFragment surroundFragment;
    private ConversationStoreFragment serviceFragment;

    //对话类型：0商家/1周边
    private int conversationType = 0;

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

        conversationType = this.getIntent().getIntExtra(EXTRA_KEY_CONVERSATION_TYPE, 0);

        initTopBar();
        initFragments();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null){
            outState.putInt(EXTRA_KEY_CONVERSATION_TYPE, conversationType);
        }
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
        btnMore = (Button) findViewById(R.id.btnMore);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });
        if(conversationType == 1){
            btnMore.setBackgroundResource(R.drawable.actionbar_store_indicator);
            tvTopBarTitle.setText(R.string.topbar_title_surround);
        }
        else{
            btnMore.setBackgroundResource(R.drawable.actionbar_store_indicator);
            tvTopBarTitle.setText(R.string.topbar_title_store);
        }
        btnMore.setVisibility(View.GONE);
    }

    /**
     * 初始化内容视图
     * Caused by: java.lang.IllegalStateException: commit already called
     * */
    private void initFragments(){
//        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, serviceFragment)
//                .add(R.id.fragment_container, surroundFragment)
//                .hide(surroundFragment).hide(serviceFragment)
//                .commit();

        if(conversationType == 1){
            surroundFragment = new ConversationSurroundFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, surroundFragment).show(surroundFragment)
                    .commit();
        }
        else{
            serviceFragment = new ConversationStoreFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, serviceFragment).show(serviceFragment)
                    .commit();
        }
    }
}
