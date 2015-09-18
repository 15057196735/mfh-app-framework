package com.mfh.comna.bizz.material.view;

import android.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mfh.comna.bizz.material.entity.ResourceData;
import com.mfh.comn.bean.PageInfo;
import com.mfh.comna.R;
import com.mfh.comna.comn.bean.KvBean;
import com.mfh.comna.comn.database.dao.NetProcessor;

import java.util.List;

/**
 * 常用语列表
 * Created by Shicy on 14-4-25.
 */
public class MaterialCyyActivity extends MaterialActivity {

    @Override
    protected void initActionBar(ActionBar actionBar) {
        super.initActionBar(actionBar);
        actionBar.setIcon(R.drawable.logo_mfh);
    }

    @Override
    public int getItemResLayoutId(int position) {
        return R.layout.material_list_item_cyy;
    }

    @Override
    public void fillListItemView(KvBean<ResourceData> kvBean, View listItemView, int position, ViewGroup parent) {
        ResourceData rsData = kvBean.getBean();
        String content = resourceUtil.getMsgAttr(rsData).getContent();
        TextView tv = (TextView)listItemView.findViewById(R.id.mat_paramText);
        tv.setText(content);

        MaterialItemBtnBar barView = (MaterialItemBtnBar)listItemView.findViewById(R.id.mat_btnBar);
        barView.setTag(content);
    }

    @Override
    protected List<KvBean<ResourceData>> readListPageData(String searchToken,PageInfo pageInfo,
                                                  NetProcessor.QueryRsProcessor<ResourceData> callBack) {
        ms.queryFromNet(ResourceData.RES_TYPE_TEXT, callBack);
        return null;//因为异步
    }
}
