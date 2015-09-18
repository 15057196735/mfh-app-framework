package com.mfh.comna.bizz.member.view;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import com.mfh.comna.bizz.login.LoginService;
import com.mfh.comn.bean.EntityWrapper;
import com.mfh.comn.bean.PageInfo;
import com.mfh.comn.net.data.RspQueryResult;
import com.mfh.comna.R;
import com.mfh.comna.comn.bean.KvBean;
import com.mfh.comna.comn.database.dao.NetProcessor;
import com.mfh.comna.comn.logic.BaseService;
import com.mfh.comna.comn.logic.MyPageListCheckAdapter;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.view.BaseListCheckFragment;
import com.mfh.comna.bizz.member.MemberConstants;
import com.mfh.comna.bizz.member.entity.Human;
import com.mfh.comna.bizz.member.entity.Ihuman;
import com.mfh.comna.bizz.member.logic.MemberService;
import com.mfh.comna.bizz.msg.entity.SessionGroupMember;
import com.mfh.comna.bizz.msg.logic.SessionGroupService;
import java.util.ArrayList;
import java.util.List;

/**
 * 带选择框的通讯录，以便于选择对话人员
 * Created by Shicy on 14-4-12.
 */
public class MemberCheckableFragment extends BaseListCheckFragment<Ihuman> {
    private MemberService memberService;
    private LoginService ls = ServiceFactory.getService(LoginService.class.getName());
    private boolean isAddMember = true;//是否添加成员模式
    private SessionGroupService groupService;
    private Long sessionId = null;
    private String[] guids = null;
    private BroadcastReceiver memReceiver = null;
    private String preLetter = null;//全局变量，否则一分页会出问题
    private int key = 0;

    /**
     * 构造函数
     */
    public MemberCheckableFragment() {
        super();
        groupService = ServiceFactory.getService(SessionGroupService.class, this.getContext());
    }

    @Override
    protected void createViewInner(View rootView, ViewGroup container, Bundle savedInstanceState) {
        isAddMember = getArguments().getBoolean("checkMode");
        sessionId = getArguments().getLong("sessionId");
        guids = getArguments().getStringArray("existIds");
        key = getArguments().getInt("key");
        super.createViewInner(rootView, container, savedInstanceState);
    }

    /**
     * 创建列表数据适配器
     * @return
     * @author zhangyz created on 2013-4-14
     */
    @Override
    protected MyPageListCheckAdapter<Ihuman> createAdapter() {
        return new MyPageListCheckAdapter<Ihuman>(getItemCheckResId(), this){
            @Override
            protected boolean needCacheViewItem() {
                return false;//因Item不同故必须是false，否则有bug。
            }
        };
    }

    /**
     * 是否存在指定人员编号
     * @param id
     * @return
     */
    private boolean existHumanId(String id) {
        if (guids == null || id == null)
            return false;
        for (String theId : guids) {
            if (id.equals(theId))
                return true;
        }
        return false;
    }

    @Override
    public int getItemResLayoutId(int position) {
        if (mAdapter.getItem(position).isSeparator())
            return R.layout.comn_list_item_header;
        return R.layout.mem_list_item_checkable;
    }

    @Override
    protected int getItemCheckResId() {
        return R.id.member_listItemChk;
    }

    @Override
    protected boolean isAsyncDao() {
        if (isAddMember)
            return false;
        else
            return true;
    }

    @Override
    public boolean isItemEnabled(int position) {
        return !mAdapter.getItem(position).isSeparator();
    }

    @Override
    public BaseService getService() {
        memberService = ServiceFactory.getService(MemberService.class, this.getContext());
        return memberService;
    }


    @Override
    protected List<KvBean<Ihuman>> readListPageData(String searchToken, PageInfo pageInfo,
              final NetProcessor.QueryRsProcessor<Ihuman> callBack) {
        if (isAddMember) {//添加人员
            Long ownerId = ls.getUserId();
            List<Human> humans = memberService.getDao().queryMemsByOwnerId(ownerId, null, pageInfo);

            List<KvBean<Ihuman>> ret = new ArrayList<KvBean<Ihuman>>();
            for (Human bean : humans) {
                if (existHumanId(bean.getGuid()))
                    continue;
                String lx = bean.genLastLetterIndex();
                if (!lx.equals(preLetter)) {
                    //自动加上分隔bean
                    String labelCaption = bean.genSeperatorLabel();
                    KvBean<Ihuman> hm = new KvBean<Ihuman>(labelCaption);
                    ret.add(hm);
                    preLetter = lx;
                }
                if (key == 0 || (key == 1 && !MemberConstants.MEMBER_TYPE_OWNER.equals(bean.getLetterIndex())))
                    ret.add(new KvBean<Ihuman>(new EntityWrapper(bean)));
            }
            return ret;
        }
        else {//移除人员
            NetProcessor.QueryRsProcessor<SessionGroupMember> factCall;
            factCall = new NetProcessor.QueryRsProcessor<SessionGroupMember>(callBack.getPageInfo()) {
                @Override
                public void processQueryResult(RspQueryResult<SessionGroupMember> rs) {
                    //泛型转换
                    RspQueryResult<Ihuman> newRs = new RspQueryResult<Ihuman>();
                    newRs.setTotalNum(rs.getTotalNum());
                    List<EntityWrapper<SessionGroupMember>> aa = rs.getRowDatas();
                    MyPageListCheckAdapter checkAdapter = (MyPageListCheckAdapter)mAdapter;
                    for (EntityWrapper<SessionGroupMember> gm : aa) {
                        EntityWrapper<Ihuman> im = new EntityWrapper<Ihuman>(gm.getBean(), gm.getCaption());
                        newRs.addRowItem(im);
                        //事先预置上，代表这些都是一开始被选中的
                        checkAdapter.addSelectItemId(im.getBean().getId().intValue());
                    }
                    callBack.processQueryResult(newRs);
                }
            };
            groupService.queryFromNet(sessionId, factCall);
            return null;
        }
    }

    @Override
    public void fillListItemView(KvBean<Ihuman> kvBean, View listItemView, int position, ViewGroup parent) {
        if (kvBean.isSeparator()) {
            TextView text1 = (TextView)listItemView.findViewById(android.R.id.text1);
            text1.setText(kvBean.getSeparatTitle());
        }
        else {
            MemberFragment.fillMemberItemSimple(kvBean, listItemView);
        }
    }

    /**
     * 执行选中
     * @param v
     */
    private void doSelect(View v) {
        Intent intent = new Intent(MemberConstants.ACTION_MEMBER_SELECTED);
        KvBean<Ihuman> bean = (KvBean<Ihuman>)v.getTag();
        intent.putExtra("human", bean.getBean());
        this.getActivity().sendBroadcast(intent);
    }

    /**
     * 执行去选
     * @param v
     */
    private void doUnSelect(View v) {
        Intent intent = new Intent(MemberConstants.ACTION_MEMBER_UNSELECTED);
        KvBean<Ihuman> bean = (KvBean<Ihuman>)v.getTag();
        intent.putExtra("humanId", bean.getBean().getId());
        this.getActivity().sendBroadcast(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        MyPageListCheckAdapter<Ihuman> adapter = (MyPageListCheckAdapter<Ihuman>)mAdapter;
        boolean bChecked = adapter.doItemClick(l, v, position, id);
        if (bChecked) {
            if (isAddMember)
                doSelect(v);
            else
                doUnSelect(v);
        }
        else {
            if (isAddMember)
                doUnSelect(v);
            else
                doSelect(v);
        }
    }

    @Override
    public void onDestroy() {
        if (memReceiver != null)
            getActivity().unregisterReceiver(memReceiver);
        super.onDestroy();
    }
}
