package com.mfh.comna.bizz.material.view;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;

import com.mfh.comna.bizz.material.ResourceUtil;
import com.mfh.comna.bizz.material.entity.ResourceData;
import com.mfh.comna.bizz.material.logic.MaterialService;
import com.mfh.comna.R;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.view.BaseListActivity;

/**
 * Created by Shicy on 14-4-25.
 */
public abstract class MaterialActivity extends BaseListActivity<ResourceData> {
    protected MaterialService ms = null;
    protected ResourceUtil resourceUtil = new ResourceUtil();

    @Override
    protected void initActionBar(ActionBar actionBar) {
        super.initActionBar(actionBar);
        actionBar.setIcon(R.drawable.white_logo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ms = ServiceFactory.getService(MaterialService.class, this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isAsyncDao() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return com.mfh.comna.R.layout.comn_list_view_3;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.material, menu);
        return true;
    }
}
