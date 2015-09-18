package com.mfh.comna.bizz.member.view;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.mfh.comna.bizz.login.logic.MfhLoginService;
import com.mfh.comn.bean.EntityWrapper;
import com.mfh.comn.bean.PageInfo;
import com.mfh.comna.R;
import com.mfh.comna.comn.bean.KvBean;
import com.mfh.comna.comn.database.dao.NetProcessor;
import com.mfh.comna.comn.logic.BaseService;
import com.mfh.comna.comn.logic.MyPageListAdapter;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.api.utils.StringUtils;
import com.mfh.comna.view.BaseListFragment;
import com.mfh.comna.view.img.FineImgView;
import com.mfh.comna.bizz.member.entity.SubdisManager;
import com.mfh.comna.bizz.member.logic.MemberManagerService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/10/22.
 */
public class MemberManagerFragment extends BaseListFragment<SubdisManager>{

    private MemberManagerService service;
    private MfhLoginService loginService;
    private String preLetter = null;//全局变量，否则一分页会出问题


    @Override
    public int getItemResLayoutId(int position) {
        if (mAdapter.getItem(position).isSeparator())
            return com.mfh.comna.R.layout.comn_list_item_header;
        else
            return R.layout.mem_list_item;
    }

    @Override
    protected MyPageListAdapter<SubdisManager> createAdapter() {
        return new MyPageListAdapter<SubdisManager>(this){
            @Override
            protected boolean needCacheViewItem() {
                return false;//因Item不同故必须是false，否则有bug。
            }
        };
    }

    @Override
    public BaseService getService() {
        service =  ServiceFactory.getService(MemberManagerService.class, this.getContext());
        return service;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginService = MfhLoginService.get();
    }

    @Override
    public void fillListItemView(KvBean<SubdisManager> kvBean, View listItemView, int position, ViewGroup parent) {
        if (kvBean.isSeparator()) {
            TextView text1 = (TextView)listItemView.findViewById(android.R.id.text1);
            text1.setText(kvBean.getSeparatTitle());
        }
        else {
            fillMemberItem(kvBean, listItemView);
        }
    }

    /**
     * 填充一个通讯录
     * @param kvBean
     * @param listItemView
     */
    public static void fillMemberItem(KvBean<SubdisManager> kvBean, View listItemView) {
        SubdisManager human = kvBean.getBean();
        fillMemberItem(human, listItemView);

        TextView text2 = (TextView)listItemView.findViewById(android.R.id.text2);
        String signName = human.getSignname();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(signName)) {
            if (!signName.equals(human.getName()))//防止重复显示
                text2.setText(signName);//getAdress()不能用
        }
        if (null != signName) {
            String longName = StringUtils.ReturnLongerWhereContain(signName, human.getName());
            if (longName != null){
                text2.setText(R.string._blank);
                TextView text1 = (TextView)listItemView.findViewById(android.R.id.text1);
                text1.setText(longName);
            }
        }
    }

    private static void fillMemberItem(SubdisManager human, View listItemView) {
        FineImgView fv = (FineImgView)listItemView.findViewById(R.id.member_headImage);
        if (fv == null)
            return;
        fv.setFao(FineImgView.getHeadImgFao());
        fv.setNeedSample(true);
        fv.setSrc(human.getHeadimage());//"f984264aa7c89b48fc2d08a64c21bd08.jpg"
        TextView text1 = (TextView)listItemView.findViewById(android.R.id.text1);
        text1.setText(human.getName());
    }

    @Override
    protected List<KvBean<SubdisManager>> readListPageData(String searchToken, PageInfo pageInfo, NetProcessor.QueryRsProcessor<SubdisManager> callBack) {
        List<SubdisManager> managers = service.getDao().queryManager(loginService.getUserId(),searchToken,pageInfo);
        List<KvBean<SubdisManager>> ret = new ArrayList<KvBean<SubdisManager>>();
        for (SubdisManager bean : managers) {
            String lx = bean.getSubdisName();
            if (!lx.equals(preLetter)) {
                //自动加上分隔bean
                String labelCaption = bean.getSubdisName();
                KvBean<SubdisManager> hm = new KvBean<SubdisManager>(labelCaption);
                ret.add(hm);
                preLetter = lx;
            }
            ret.add(new KvBean<SubdisManager>(new EntityWrapper(bean)));
        }
        return ret;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected boolean isAsyncDao() {
        return false;
    }
}
