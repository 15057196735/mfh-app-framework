package com.mfh.comna.bizz.member.view;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.mfh.comna.R;
import com.mfh.comna.view.BaseFragmentActive;

/**
 * Created by Shicy on 14-4-16.
 */
public class MemberListActivity extends BaseFragmentActive {

    @Override
    public int getLayoutId() {
        return R.layout.comn_fragment_activity;
    }

    @Override
    protected void initActionBar(ActionBar actionBar) {
        super.initActionBar(actionBar);
        actionBar.setIcon(R.drawable.white_logo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //String itemForwardActivityClsName = this.getIntent().getStringExtra(MemberFragment.ItemForwardActivityClsName);
        MemberFragment memberFragment = new MemberFragment();
        Bundle param = new Bundle();
        //param.putString(MemberFragment.ItemForwardActivityClsName, itemForwardActivityClsName);
        memberFragment.setArguments(param);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, memberFragment);
        transaction.commit();
    }

}
