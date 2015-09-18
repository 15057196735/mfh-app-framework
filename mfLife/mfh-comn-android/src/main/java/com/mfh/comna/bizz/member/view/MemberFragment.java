package com.mfh.comna.bizz.member.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.mfh.comna.api.ui.dialog.DialogHelper;
import com.mfh.comna.api.utils.StringUtils;
import com.mfh.comna.bizz.login.LoginService;
import com.mfh.comna.bizz.login.logic.MfhLoginService;
import com.mfh.comn.bean.EntityWrapper;
import com.mfh.comn.bean.PageInfo;
import com.mfh.comna.R;
import com.mfh.comna.comn.bean.KvBean;
import com.mfh.comna.comn.database.dao.NetProcessor;
import com.mfh.comna.comn.logic.BaseService;
import com.mfh.comna.comn.logic.MyPageListAdapter;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.view.BaseListFragment;
import com.mfh.comna.view.img.FineImgView;
import com.mfh.comna.bizz.member.MemberConstants;
import com.mfh.comna.bizz.member.entity.Human;
import com.mfh.comna.bizz.member.entity.Ihuman;
import com.mfh.comna.bizz.member.logic.MemberService;

import java.util.ArrayList;
import java.util.List;

/**
 * 通讯录列表
 * Created by Shicy on 14-3-24.
 */
public class MemberFragment extends BaseListFragment<Human> {
    private MemberService memberService = ServiceFactory.getService(MemberService.class, this.getContext());
    private LoginService ls = null;
    private BroadcastReceiver memReceiver = null;
    private Class<? extends Activity> itemForwardActivity = null;//通讯录详细页面activity，外面可以作为参数指定
    public static String ItemForwardActivityClsName = "msg_pmc_member";
    String clsName = "defalut";
    private String preLetter = null;//全局变量，否则一分页会出问题
    private Dialog dialog;

    /**
     * 构造函数
     */
    public MemberFragment() {
        super();
        ls = ServiceFactory.getService(LoginService.class.getName());

    }

    @Override
    protected void createViewInner(View rootView, ViewGroup container, Bundle savedInstanceState) {
        super.createViewInner(rootView, container, savedInstanceState);
        registerMsgReceiver();
    }

    /**
     * 创建列表数据适配器
     * @return
     * @author zhangyz created on 2013-4-14
     */
    @Override
    protected MyPageListAdapter<Human> createAdapter() {
        return new MyPageListAdapter<Human>(this){
            @Override
            protected boolean needCacheViewItem() {
                return false;//因Item不同故必须是false，否则有bug。
            }
        };
    }

    /**
     * 注册消息会话改变监听器
     */
    private void registerMsgReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MemberConstants.ACTION_MEMBER_NEW);
        intentFilter.addAction(MemberConstants.ACTION_MEMBER_REFRESH);
        intentFilter.addAction(MemberConstants.ACTION_MEMBER_ERROR);
        final MemberFragment that = this;
        memReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                if (intent.getAction().equals(MemberConstants.ACTION_MEMBER_NEW)) {
                    that.loadDataOnInitDirect();
                    if (dialog != null)
                        dialog.dismiss();
                }else if(intent.getAction().equals(MemberConstants.ACTION_MEMBER_ERROR)){
                    if (dialog != null)
                        dialog.dismiss();
                }
                else //ACTION_MEMBER_REFRESH
                    that.doLoadAndRefreshStart();
            }
        };
        getActivity().registerReceiver(memReceiver, intentFilter);
    }

    /**
     * 引入此方法是为了避免没有通讯录是造成的死循环，因为会不断启动下载。
     */
    private void loadDataOnInitDirect() {
        super.loadDataOnInit();
    }

    @Override
    protected void loadDataOnInit() {
        String subdisIds = ((MfhLoginService)ls).getMySubdisIds();
        if (subdisIds == null || subdisIds.length() == 0) {
            showHint("管辖的小区为空!");
            return;
        }
        Long userId = ls.getUserId();
        super.loadDataOnInit();
        if (memberService.getDao().getCountByOwnerId(userId) <= 0) {
           // memberService.queryFromNet(userId, subdisIds);
            dialog = DialogHelper.genProgressDialog(getActivity(), true, null);
        }
        else {
            //再启动增量下载
           // memberService.queryFromNet(userId, subdisIds);
        }
    }

    @Override
    public int getItemResLayoutId(int position) {
        if (mAdapter.getItem(position).isSeparator())
            return com.mfh.comna.R.layout.comn_list_item_header;
        else
            return R.layout.mem_list_item;
    }

    @Override
    public ProgressDialog onPreExecute(int taskKind) {
        return null;//super.onPreExecute(taskKind);//屏蔽等待框
    }

    @Override
    public boolean isItemEnabled(int position) {
        return !mAdapter.getItem(position).isSeparator();
    }

    @Override
    public BaseService getService() {
        return null;
    }

    private static void fillMemberItem(Ihuman human, View listItemView) {
        FineImgView fv = (FineImgView)listItemView.findViewById(R.id.member_headImage);
        if (fv == null)
            return;
        fv.setFao(FineImgView.getHeadImgFao());
        fv.setNeedSample(true);
        fv.setSrc(human.getHeadimage());//"f984264aa7c89b48fc2d08a64c21bd08.jpg"
        TextView text1 = (TextView)listItemView.findViewById(android.R.id.text1);
        text1.setText(human.getName());
    }

    /**
     * 填充一个通讯录Item
     * @param kvBean
     * @param listItemView
     */
    public static void fillMemberItemSimple(KvBean<Ihuman> kvBean, View listItemView) {
        Ihuman human = kvBean.getBean();
        fillMemberItem(human, listItemView);
    }

    /**
     * 填充一个通讯录
     * @param kvBean
     * @param listItemView
     */
    public static void fillMemberItem(KvBean<Human> kvBean, View listItemView) {
        Human human = kvBean.getBean();
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

    @Override
    public void fillListItemView(KvBean<Human> kvBean, View listItemView, int position, ViewGroup parent) {
        if (kvBean.isSeparator()) {
            TextView text1 = (TextView)listItemView.findViewById(android.R.id.text1);
            text1.setText(kvBean.getSeparatTitle());
        }
        else {
            fillMemberItem(kvBean, listItemView);
        }
    }

    @Override
    protected boolean isAsyncDao() {
        return false;
    }

    @Override
    protected List<KvBean<Human>> readListPageData(String searchToken,
                                                      PageInfo pageInfo, NetProcessor.QueryRsProcessor callBack) {
        Long ownerId = ls.getUserId();
        List<Human> humans = memberService.getDao().queryMemsByOwnerId(ownerId, null, pageInfo);

        List<KvBean<Human>> ret = new ArrayList<KvBean<Human>>();
        for (Human bean : humans) {
            String lx = bean.genLastLetterIndex();
            if (!lx.equals(preLetter)) {
                //自动加上分隔bean
                String labelCaption = bean.genSeperatorLabel();
                KvBean<Human> hm = new KvBean<Human>(labelCaption);
                ret.add(hm);
                preLetter = lx;
            }
            ret.add(new KvBean<Human>(new EntityWrapper(bean)));
        }
        return ret;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (this.getArguments()!=null)
            clsName = this.getArguments().getString(MemberConstants.PMB_MEMBER_DETAIL_ACTIVITY_CLASS_NAME);
        try {
            itemForwardActivity = (Class<? extends Activity>)Class.forName(clsName);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        if (itemForwardActivity == null)
            itemForwardActivity = MemberDetailActivity.class;
        Intent intent = new Intent(this.getContext(), itemForwardActivity);
        KvBean<Human> humanBean = (KvBean<Human>)view.getTag();
        intent.putExtra("humanId", humanBean.getBean().getId());
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        if (memReceiver != null)
            getActivity().unregisterReceiver(memReceiver);
        super.onDestroy();
    }
}
