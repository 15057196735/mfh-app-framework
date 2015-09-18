package com.mfh.comna.bizz.msg.view;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.TextView;

import com.mfh.comna.api.helper.UIHelper;
import com.mfh.comn.bean.EntityWrapper;
import com.mfh.comn.net.data.IResponseData;
import com.mfh.comn.net.data.RspQueryResult;
import com.mfh.comna.R;
import com.mfh.comna.comn.database.dao.NetCallBack;
import com.mfh.comna.comn.database.dao.NetProcessor;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.view.BaseComnActivity;
import com.mfh.comna.view.img.FineImgView;
import com.mfh.comna.bizz.member.MemberConstants;
import com.mfh.comna.bizz.member.view.MemberSelectionActivity;
import com.mfh.comna.bizz.msg.MsgConstants;
import com.mfh.comna.bizz.msg.entity.EmbSession;
import com.mfh.comna.bizz.msg.entity.SessionGroupMember;
import com.mfh.comna.bizz.msg.logic.EmbMsgService;
import com.mfh.comna.bizz.msg.logic.EmbSessionService;
import com.mfh.comna.bizz.msg.logic.MsgSetUtil;
import com.mfh.comna.bizz.msg.logic.SessionGroupService;

import java.util.List;

/**
 * 对话参与者列表信息，以及控制入口
 * Created by Shicy on 14-4-11.
 */
public class ChatDetailActivity extends BaseComnActivity implements AdapterView.OnItemClickListener {
    private GridView gridView;
    private MyGridAdapter gridAdapter;
    private SessionGroupService groupService;
    private EmbSessionService sessionService;
    private Long sessionId;//当前会话标识
    private BroadcastReceiver receiver = null;//接收器
    private String[] existGuids = null;//已经存在的会话人员Guid
    private int msgMode = -1;
    private EmbSession embSession;

    @Override
    public int getLayoutId() {
        return R.layout.activity_chat_detail;
    }

    @Override
    protected void initActionBar(ActionBar actionBar) {
        super.initActionBar(actionBar);
        actionBar.setIcon(R.drawable.white_logo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        groupService = ServiceFactory.getService(SessionGroupService.class, this);
        sessionService = ServiceFactory.getService(EmbSessionService.class);
        super.onCreate(savedInstanceState);
        embSession = (EmbSession) getIntent().getSerializableExtra("embssion_bean");
        sessionId = this.getIntent().getLongExtra("sessionId", -1L);
        msgMode = this.getIntent().getIntExtra("msgMode", -1);
        if (msgMode != -1){
            groupService.setMsgMode(msgMode);
        }else {
            //由于工单是最先写的，所以默认就是工单了
            groupService.setMsgMode(MsgConstants.MSG_MODE_APART);
        }

        //加载历史消息
        Button button = (Button)findViewById(R.id.btn_detail_chat_history);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MsgSetUtil msgSet = new MsgSetUtil(getSharedPreferences("login", Activity.MODE_PRIVATE).getString("app.login.name", ""));
                msgSet.saveMaxCreateTime(sessionId, "0000-00-00 00:00:00");
                EmbMsgService msgService = ServiceFactory.getService(EmbMsgService.class);
                msgService.queryFromNet(sessionId);
                finish();
                groupService = null;
//                groupService.getDao();
                /*for (int i = 0; i< 100; i++){
                    msgService.sendMessage(sessionId, new TextParam(i + ""), null, null);
                }*/
            }
        });

        //置顶部分
        Switch swWiew = (Switch)findViewById(R.id.ms_switchSession);
        if (sessionService.getDao().isTopOrder(sessionId))
            swWiew.setChecked(true);
        else
            swWiew.setChecked(false);

        swWiew.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    sessionService.getDao().updateTopOrder(sessionId);
                else
                    sessionService.getDao().resetTopOrder(sessionId);
                UIHelper.sendBroadcast(MsgConstants.ACTION_RECEIVE_SESSION);
            }
        });

        //移除对话按钮
        findViewById(R.id.ms_userRemove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupService.getDao().releaseSessionGroup(sessionId, new NetCallBack.NormalNetTask<String>(String.class) {
                    public void processResult(IResponseData rspData) {
                        sessionService = ServiceFactory.getService(EmbSessionService.class);
                        showHint("解散对话人员成功!");
                        loadMemberList();
                        embSession.setIsGroup(2);
                        sessionService.getDao().saveOrUpdate(embSession);

                    }
                });
            }
        });

        //参与者头像列表部分
        gridView = (GridView)findViewById(R.id.ms_headGridView);
        gridView.setOnItemClickListener(this);
        loadMemberList();
        registerMyReceiver();
    }

    /**
     * 注册广播接收器
     */
    private void registerMyReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(MemberConstants.ACTION_MEMBER_ADDED);
        filter.addAction(MemberConstants.ACTION_MEMBER_DELETED);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loadMemberList();//重新刷新列表
            }
        };
        registerReceiver(receiver, filter);
    }

    /**
     * 加载会员列表
     */
    private void loadMemberList() {
        if (gridAdapter == null) {
            gridAdapter = new MyGridAdapter(this);
            gridView.setAdapter(gridAdapter); //显示
        }
        else {
            gridAdapter.clear();
        }
        groupService.queryFromNet(sessionId, new NetProcessor.QueryRsProcessor<SessionGroupMember>(null) {
            @Override
            public void processQueryResult(RspQueryResult<SessionGroupMember> rs) {
                showGrid(rs);
            }
        });
    }

    /**
     * 显示头像列表
     * @param rs
     */
    private void showGrid(RspQueryResult<SessionGroupMember> rs) {
        List<EntityWrapper<SessionGroupMember>> ret = rs.getRowDatas();
        if (ret == null) {
            //借用status作为view标志
            SessionGroupMember addTemp = new SessionGroupMember();
            addTemp.setStatus(10);
            gridAdapter.add(addTemp);
            SessionGroupMember subTemp = new SessionGroupMember();
            subTemp.setStatus(11);
            gridAdapter.add(subTemp);
            return;
        }
        existGuids = new String[rs.getReturnNum()];
        int ii = 0;
        for (EntityWrapper<SessionGroupMember> item : ret) {
            SessionGroupMember bean = item.getBean();
            gridAdapter.add(bean);
            if (bean.getGuid() != null)
                existGuids[ii++] = bean.getGuid();
        }
        //借用status作为view标志
        SessionGroupMember addTemp = new SessionGroupMember();
        addTemp.setStatus(10);
        gridAdapter.add(addTemp);
        SessionGroupMember subTemp = new SessionGroupMember();
        subTemp.setStatus(11);
        gridAdapter.add(subTemp);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long itemId) {
        SessionGroupMember member = (SessionGroupMember)view.getTag();
        if (member.getStatus() == 10) {
            Intent intent = new Intent(this, MemberSelectionActivity.class);
            intent.putExtra("sessionId", sessionId);
            intent.putExtra("checkMode", Boolean.valueOf(true));
            intent.putExtra("msgMode", msgMode);
            if (existGuids != null)
                intent.putExtra("existIds", existGuids);
            startActivity(intent);
        }
        else if (member.getStatus() == 11) {
            Intent intent = new Intent(this, MemberSelectionActivity.class);
            intent.putExtra("sessionId", sessionId);
            intent.putExtra("checkMode", Boolean.valueOf(false));
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        if (receiver != null)
            unregisterReceiver(receiver);
        super.onDestroy();
    }

    // ========================================================================================

    private static class MyGridAdapter extends ArrayAdapter<SessionGroupMember> {

        public MyGridAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1);
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            SessionGroupMember member = this.getItem(position);
            int status = member.getStatus();
            if (view == null) {
                if (status == 10)
                    view = LayoutInflater.from(getContext()).inflate(com.mfh.comna.R.layout.comn_grid_item_plus, null);
                else if (status == 11)
                    view = LayoutInflater.from(getContext()).inflate(com.mfh.comna.R.layout.comn_grid_item_minus, null);
                else {
                    view = LayoutInflater.from(getContext()).inflate(R.layout.chat_member_item, null);
                    View subview = view.findViewById(R.id.ms_imageFrame);
                    FineImgView iv = (FineImgView)subview.findViewById(R.id.ms_groupHeadImg);
                    iv.setNeedSample(true);
                    iv.setFao(FineImgView.getHeadImgFao());
                    iv.setSrc(member.getHeadimageurl());
                    TextView tv = (TextView)view.findViewById(R.id.ms_humanName);
                    tv.setText(member.getName());
                }
            }
            view.setTag(member);
            return view;
        }

       /* @Override
        public boolean isEnabled(int position) {
            return position >= 4;
        }*/
    }
}
