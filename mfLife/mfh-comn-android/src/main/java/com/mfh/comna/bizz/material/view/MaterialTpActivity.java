package com.mfh.comna.bizz.material.view;

import android.view.View;
import android.view.ViewGroup;

import com.mfh.comna.api.utils.ImageUtil;
import com.mfh.comna.bizz.material.ResourceUtil;
import com.mfh.comna.bizz.material.entity.ResourceData;
import com.mfh.comn.bean.PageInfo;
import com.mfh.comna.R;
import com.mfh.comna.comn.bean.KvBean;
import com.mfh.comna.comn.database.dao.NetProcessor;
import com.mfh.comna.view.img.FineImgView;

import java.util.List;

/**
 * 图片Activity
 * Created by Shicy on 14-4-25.
 */
public class MaterialTpActivity extends MaterialActivity {

    @Override
    public int getItemResLayoutId(int position) {
        return R.layout.material_list_item_tp;
    }

    @Override
    public void fillListItemView(KvBean<ResourceData> kvBean, View listItemView, int position, ViewGroup parent) {
        ResourceData rsData = kvBean.getBean();
        String picUrl = resourceUtil.getMsgAttr(rsData).getPicurl();
        FineImgView fv = (FineImgView)listItemView.findViewById(R.id.mat_imgView);
        fv.setFao(ResourceUtil.getMatImgFao());
        fv.setSrc(picUrl);
        fv.setNeedSample(true);
        fv.setMaxWidth(ImageUtil.getScreenWidth(this));
        MaterialItemBtnBar barView = (MaterialItemBtnBar)listItemView.findViewById(R.id.mat_btnBar);
        barView.setTag(rsData.getId());
    }

    @Override
    protected List<KvBean<ResourceData>> readListPageData(String searchToken,PageInfo pageInfo,
        NetProcessor.QueryRsProcessor<ResourceData> callBack) {
        ms.queryFromNet(ResourceData.RES_TYPE_IMAGE, callBack);
        return null;//因为异步
    }
}
