package com.mfh.comna.bizz.member.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import com.mfh.comn.net.data.IResponseData;
import com.mfh.comna.comn.database.dao.NetCallBack;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.utils.DensityUtil;
import com.mfh.comna.utils.DialogUtil;
import com.mfh.comna.view.BaseFragmentActive;
import com.mfh.comna.R;
import com.mfh.comna.view.img.FineImgView;
import com.mfh.comna.bizz.member.MemberConstants;
import com.mfh.comna.bizz.member.entity.Ihuman;
import com.mfh.comna.bizz.msg.MsgConstants;
import com.mfh.comna.bizz.msg.logic.SessionGroupService;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 会话成员选择Activity
 * Created by Shicy on 14-4-12.
 */
public class MemberSelectionActivity extends BaseFragmentActive {
    private MemberCheckableFragment memberFragment = null;
    private BroadcastReceiver memSelectReceiver = null;
    private LinearLayout selectedMems = null;
    private SessionGroupService sgService;
    private Long sessionId;
    private Boolean checkModel = true;//增加还是删除模式
    private int msgMode = -1;
    private SessionGroupService groupService;

    @Override
    public int getLayoutId() {
        return R.layout.activity_member_selection;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sgService = ServiceFactory.getService(SessionGroupService.class, this);
        super.onCreate(savedInstanceState);

        groupService = ServiceFactory.getService(SessionGroupService.class, this);
        msgMode = this.getIntent().getIntExtra("msgMode", -1);
        if (msgMode != -1){
            groupService.setMsgMode(msgMode);
        }else {
            //由于工单是最先写的，所以默认就是工单了
            groupService.setMsgMode(MsgConstants.MSG_MODE_APART);
        }

        sessionId = this.getIntent().getLongExtra("sessionId", -1L);
        checkModel = this.getIntent().getBooleanExtra("checkMode", Boolean.valueOf(true));
        String[] existIds = this.getIntent().getStringArrayExtra("existIds");

        if (checkModel)
            this.setTitle(R.string.title_activity_member_add);
        else
            this.setTitle(R.string.title_activity_member_delete);

        selectedMems = (LinearLayout)findViewById(R.id.member_selected_humans);
        selectedMems.removeAllViews();

        memberFragment = new MemberCheckableFragment();
        Bundle param = new Bundle();
        param.putBoolean("checkMode", checkModel);//增加成员
        param.putLong("sessionId", sessionId);
        if (existIds != null)
            param.putStringArray("existIds", existIds);
        memberFragment.setArguments(param);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.memberContainer, memberFragment);
        transaction.commit();

        final NetCallBack.NormalNetTask callback = new NetCallBack.NormalNetTask<String>(String.class, this) {
            /*@Override
            protected void processFailure(Throwable t, String errMsg) {
                super.processFailure(t, errMsg);
                Intent intent = new Intent();
                intent.setAction(MemberConstants.ACTION_MEMBER_ADDED);
                sendBroadcast(intent);
                finish();
            }*/

            @Override
            public void processResult(IResponseData rspData) {
                if (checkModel)
                    DialogUtil.showHint(this.context, "加入会话成功!");
                else
                    DialogUtil.showHint(this.context, "会话人员移除成功!");
                Intent intent = new Intent();
                intent.setAction(MemberConstants.ACTION_MEMBER_ADDED);
                sendBroadcast(intent);
                finish();
            }
        };

        //选择确定
        findViewById(R.id.member_select_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selCount = selectedMems.getChildCount();
                if (selCount <= 0) {
                    showHint("请先选择人员!");
                    return;
                }
                List<String> guids = new ArrayList<String>();
                for (int ii = 0; ii < selCount; ii++) {
                    Ihuman human = (Ihuman)(selectedMems.getChildAt(ii).getTag());
                    String guid = human.getGuid();
                    if (StringUtils.isBlank(guid))
                        continue;
                    guids.add(guid);
                }
                if (checkModel)
                    sgService.getDao().joinSessionGroup(sessionId, guids, callback);
                else
                    sgService.getDao().leaveSessionGroup(sessionId, guids, callback);
            }
        });

        registerMsgReceiver();
    }

    /**
     * 注册消息会话改变监听器
     */
    private void registerMsgReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MemberConstants.ACTION_MEMBER_SELECTED);
        intentFilter.addAction(MemberConstants.ACTION_MEMBER_UNSELECTED);
        final MemberSelectionActivity that = this;
        memSelectReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                if (intent.getAction().equals(MemberConstants.ACTION_MEMBER_SELECTED)) {
                    //加入一个会话人员
                    //返回的是selectedMems，不是imgView
                    LayoutInflater.from(that).inflate(R.layout.mem_selected_human, selectedMems);
                    FineImgView imgView = (FineImgView)selectedMems.getChildAt(selectedMems.getChildCount() - 1);
                    Ihuman human = (Ihuman)intent.getSerializableExtra("human");
                    imgView.setFao(FineImgView.getHeadImgFao());
                    imgView.setNeedSample(true);
                    imgView.setMaxWidth(DensityUtil.dip2px(that, 32));
                    imgView.setSrc(human.getHeadimage());
                    imgView.setTag(human);
                }
                else if (intent.getAction().equals(MemberConstants.ACTION_MEMBER_UNSELECTED)) {
                    //移除一个会话人员
                    Long humanId = intent.getLongExtra("humanId", -1L);
                    for (int ii = 0; ii < selectedMems.getChildCount(); ii++) {
                        FineImgView imgView = (FineImgView)selectedMems.getChildAt(ii);
                        Ihuman human = (Ihuman)imgView.getTag();
                        if (human != null && human.getId().equals(humanId)) {
                            selectedMems.removeView(imgView);
                            break;
                        }
                    }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.member_add, menu);
        return super.onCreateOptionsMenu(menu);
    }


}
