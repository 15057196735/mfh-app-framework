package com.mfh.comna.bizz.member.view;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.mfh.comna.R;
import com.mfh.comna.comn.logic.IBaseViewComponent;
import com.mfh.comna.view.BaseFragmentActive;
import com.mfh.comna.bizz.member.MemberConstants;

/**
 * 通讯录中选中一个人
 */
public class MemberCheckActivity extends BaseFragmentActive {
    private MemberCheckableFragment memberFragment = null;
    private BroadcastReceiver memSelectReceiver = null;

    @Override
    protected void initActionBar(ActionBar actionBar) {
        super.initActionBar(actionBar);
        actionBar.setIcon(R.drawable.white_logo);
       //actionBar.setTitle();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_member_check;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] existIds = this.getIntent().getStringArrayExtra("existIds");
        int key = this.getIntent().getIntExtra("key", 0);

        memberFragment = new MemberCheckableFragment();
        Bundle param = new Bundle();
        param.putBoolean("checkMode", true);
        if (existIds != null)
            param.putStringArray("existIds", existIds);
        if (key != 0)
            param.putInt("key", key);
        memberFragment.setArguments(param);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.member_container, memberFragment);
        transaction.commit();

        registerMsgReceiver();
    }

    @Override
    public ProgressDialog onPreExecute(int taskKind) {
        return null;//super.onPreExecute(taskKind);
    }

    /*
        注册消息会话改变监听器
        */
    private void registerMsgReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MemberConstants.ACTION_MEMBER_SELECTED);
        intentFilter.addAction(MemberConstants.ACTION_MEMBER_UNSELECTED);
        final MemberCheckActivity that = this;
        memSelectReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                if (intent.getAction().equals(MemberConstants.ACTION_MEMBER_SELECTED)) {
                    //选择一个人员
                    //Human human = (Human)intent.getSerializableExtra("human");
                    that.setResult(IBaseViewComponent.RETURN_CODE_OK, intent);
                    that.finish();
                }
            }
        };
        this.registerReceiver(memSelectReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        if (memSelectReceiver != null)
            this.unregisterReceiver(memSelectReceiver);
        super.onDestroy();
    }
}
