package com.mfh.comna.bizz.material.view;

import android.view.View;
import android.view.ViewGroup;

import com.mfh.comna.bizz.material.entity.MsgAttr;
import com.mfh.comna.bizz.material.entity.ResourceData;
import com.mfh.comn.bean.PageInfo;
import com.mfh.comna.R;
import com.mfh.comna.comn.bean.KvBean;
import com.mfh.comna.comn.database.dao.NetProcessor;

import java.util.List;

/**
 * 图文
 * Created by Shicy on 14-4-25.
 */
public class MaterialTwActivity extends MaterialActivity {

    @Override
    public int getItemResLayoutId(int position) {
        return R.layout.material_list_item_tw;
    }

    @Override
    public void fillListItemView(KvBean<ResourceData> kvBean, View listItemView, int position, ViewGroup parent) {
        ResourceData rsData = kvBean.getBean();
        List<MsgAttr> attrs = resourceUtil.getMsgAttrList(rsData);
        MaterialTwItemView fv = (MaterialTwItemView)listItemView.findViewById(R.id.mat_tuwenView);
        fv.setMsgAttrs(attrs);
        MaterialItemBtnBar barView = (MaterialItemBtnBar)listItemView.findViewById(R.id.mat_btnBar);
        barView.setTag(rsData.getId());
    }

    @Override
    protected List<KvBean<ResourceData>> readListPageData(String searchToken,PageInfo pageInfo,
                                                  NetProcessor.QueryRsProcessor<ResourceData> callBack) {
        ms.queryFromNet(ResourceData.RES_TYPE_TUWEN, callBack);
        return null;//因为异步
    }
}
