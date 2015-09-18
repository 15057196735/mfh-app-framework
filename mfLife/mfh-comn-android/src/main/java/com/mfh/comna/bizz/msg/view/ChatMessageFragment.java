package com.mfh.comna.bizz.msg.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mfh.comna.api.Constants;
import com.mfh.comna.api.helper.AppHelper;
import com.mfh.comna.api.helper.SharedPreferencesHelper;
import com.mfh.comna.api.utils.ImageUtil;
import com.mfh.comna.api.utils.MLog;
import com.mfh.comna.api.utils.StringUtils;
import com.mfh.comna.bizz.login.logic.MfhLoginService;
import com.mfh.comna.bizz.material.ResourceUtil;
import com.mfh.comna.bizz.material.entity.MsgAttr;
import com.mfh.comna.bizz.material.view.MaterialTwItemView;
import com.mfh.comna.bizz.msg.MsgConstants;
import com.mfh.comna.bizz.msg.entity.EmbMsg;
import com.mfh.comna.bizz.msg.entity.ImageParam;
import com.mfh.comna.bizz.msg.entity.ImageTextParam;
import com.mfh.comna.bizz.msg.entity.TextParam;
import com.mfh.comna.bizz.msg.entity.WxParam;
import com.mfh.comna.bizz.msg.logic.EmbMsgService;
import com.mfh.comna.bizz.msg.logic.MsgHelper;
import com.mfh.comna.bizz.msg.logic.MsgSetUtil;
import com.mfh.comn.bean.PageInfo;
import com.mfh.comn.utils.DateUtil;
import com.mfh.comna.R;
import com.mfh.comna.comn.bean.KvBean;
import com.mfh.comna.comn.database.dao.NetProcessor;
import com.mfh.comna.comn.logic.BaseService;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.select_time.TimeUtil;
import com.mfh.comna.utils.FaceUtil;
import com.mfh.comna.utils.FullScreenActivity;
import com.mfh.comna.view.BaseListFragment;
import com.mfh.comna.view.img.FineImgView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;



/**
 * 对话
 * Created by Shicy on 14-4-19.
 */
public class ChatMessageFragment extends BaseListFragment<EmbMsg>  {
    private EmbMsgService msgService;

    private Long sessionId;
    private String class_name = "";//获得类名
    private BroadcastReceiver msgReceiver;
    private int msgMode = -1;
    private Date date = null;
    private SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.INNER_FOR_MESSAGE_SHOW_DATAFORMAT);//时间工具类


    /**
     * 构造函数
     */
    public ChatMessageFragment() {
        super();
        this.cacheDataItem = false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void createViewInner(View rootView, ViewGroup container, Bundle savedInstanceState) {
        //注意sessionId不能通过构造函数,在activity中通过set设置也不靠谱，因为一旦存在activity状态切换时就会导致set不会被调用到。
        //通过在activity设置setBound参数进行传递是官方推荐的，此时也不需要在fragment中实现onSaveInstanceState了
        Bundle args = getArguments();
        if (args != null){
            sessionId = args.getLong("sessionId");//savedInstanceState.getLong()需要先调用
            class_name = args.getString("msg_pmc_member");
            msgMode = args.getInt("msgMode");
        }

        if (savedInstanceState != null) {
            //其他运行时值可以从其中恢复。
            //savedInstanceState.getString()
        }
        super.createViewInner(rootView, container, savedInstanceState);

        msgService = ServiceFactory.getService(EmbMsgService.class);
        msgService.setMsgFragment(this);
        //registerMsgReceiver();
        msgService.setMsgMode(msgMode);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected PageInfo initPageInfo() {
        PageInfo pageInfo = new PageInfo(true, PAGE_SIZE_DEF);//反向看消息，老消息往前翻。
        return pageInfo;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * 注册消息会话改变监听器
     */
    private void registerMsgReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MsgConstants.ACTION_RECEIVE_MSG);
        intentFilter.addAction(MsgConstants.GE_TUI_MSG_SHOW);
        msgReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                MLog.d(String.format("ChatMessageFragment.action= %s", intent.getAction()));
                if (intent.getAction().equals(MsgConstants.ACTION_RECEIVE_MSG)){
                    Bundle bundle = intent.getExtras();
                    if(bundle != null){
                        String ids = bundle.getString("ids");
                        if(ids != null){
                            showSendMsgAgain(ids);
                        }
                    }
                }
                else if (intent.getAction().equals(MsgConstants.GE_TUI_MSG_SHOW)) {
                    String id = intent.getStringExtra("ge_id");
                    MLog.d(String.format("id=%s", id));

                    EmbMsg msg = msgService.getDao().getEntityById(id);
                    if (msg != null){
                        Long sid = msg.getSessionid();
                        MLog.d(String.format("sessionId = %s(%s)" , String.valueOf(sid), String.valueOf(sessionId)));
                        if(sid == sessionId || sid.equals(sessionId)){
                            msg.setIsRead(1);
                            msgService.getDao().saveOrUpdate(msg);
                            showSendMsgAgain(msg);
                        }
                    }
                }
                //doLoadAndRefreshStart();
            }
        };
        //使用LocalBroadcastManager.getInstance(getActivity())反而不行，接收不到。
        getActivity().registerReceiver(msgReceiver, intentFilter);
    }

    /**
     * 滚动到最新
     */
    public void scrollToLast() {
//        scroolToPos(mAdapter.getCount() - 1);
        ListView listView = (ListView)getListView();
        listView.setSelection(listView.getCount() - 1);
//        listView.smoothScrollToPosition(listView.getCount() - 1);//直接滚动，数据多的时候滚动时间较长
    }

    public void scrollToBottom(){
        ListView listView = (ListView)getListView();
//        listView.setSelection(listView.getBottom());
        listView.setSelection(0);
    }


    /**
     * 显示一个新消息（非初始化时）
     * @param msg
     */
    public void showSendMsgAgain(EmbMsg msg) {
        if (!completeFlag){
            return;
        }

        MLog.d("showSendMsgAgain: " + msg.getMsgInfo());
        showMessageTime(msg);//每隔15分钟显示一次消息
        //msgService.getDao().setHaveRead(msg.getId());
        KvBean<EmbMsg> param = new KvBean<EmbMsg>(msg);
        addDataAndNotify(param);
        scrollToLast();
    }

    /**
     * 显示一组新消息(非初始化时)
     * @param msgIds
     */
    private void showSendMsgAgain(String msgIds) {
        if (!completeFlag || org.apache.commons.lang3.StringUtils.isBlank(msgIds))
            return;

        /*Message message = new Message();
        this.mHandler.sendMessageDelayed(message, 0);*/
        String[] ids = org.apache.commons.lang3.StringUtils.splitByWholeSeparator(msgIds, ",");
        EmbMsg bean;
        List<KvBean<EmbMsg>> addList = new ArrayList<KvBean<EmbMsg>>();
        for (String id : ids) {
            bean = msgService.getDao().getEntityById(id);
            showMessageTime(bean);//每隔15分钟显示一次消息
            //所有显示过的msg都设置成已读，并且刷新本session的未读个数
            //msgService.getDao().setHaveRead(bean.getId());
            MsgHelper.changeSessionUnReadCount(getContext(), this.sessionId, 0);
            if (sessionId.longValue() == bean.getSessionid().longValue()){//判断是否为当前会话，主要用于初始化加载数据的时候
                addList.add(new KvBean<EmbMsg>(bean));
            }
        }
        addDatasAndNotify(addList);
        scrollToLast();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerMsgReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (msgReceiver != null) {
            getActivity().unregisterReceiver(msgReceiver);
        }
    }

    @Override
    public void onDestroy() {
        if (msgReceiver != null){
//            getActivity().unregisterReceiver(msgReceiver);
        }
        super.onDestroy();
    }

    @Override
    public ProgressDialog onPreExecute(int taskKind) {
        return null;// super.onPreExecute(taskKind);//屏蔽等待框
    }

    @Override
    public int getItemResLayoutId(int position) {
        if (this.getListAdapter() == null)
            return R.layout.chat_msg_respond;

        KvBean<EmbMsg> item = this.getListAdapter().getItem(position);
        EmbMsg bean = item.getBean();

            if (MsgHelper.isMySelf(bean))
                return R.layout.chat_msg_reply;//我说的话
            else
                return R.layout.chat_msg_respond;//别人说的话
    }

    @Override
    protected boolean isAsyncDao() {
        return false;
    }

    @Override
    public void refreshToLoadMore() {
        super.refreshToLoadMore();
        doLoadAndRefreshNext();
    }

    //显示大图片
    private void showBigImage(final Activity context, String imgSrc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        FineImgView imgView = new FineImgView(this.getContext(), null);
        imgView.setNeedSample(true);
        imgView.setMaxWidth(ImageUtil.getScreenWidth(context));
        imgView.setFao(MsgConstants.getMsgImgFao());
        imgView.setSrc(imgSrc);
        builder.setView(imgView);
        builder.show();
    }


    @Override
    public void fillListItemView(KvBean<EmbMsg> kvBean, View listItemView,
                                 int position, ViewGroup parent) {
        EmbMsg bean = kvBean.getBean();
        if (bean == null) {
            return;
        }

        if (MsgHelper.isMySelf(bean)) {
            showReceiveTime(listItemView, bean, R.id.ms_chat_time);
//            showNickName(listItemView, bean, R.id.ms_myReplyTime);
            showImgHead(listItemView, bean, R.id.ms_myHeadImage);
            showMsgContent(listItemView, bean, R.id.ms_myReplyMsg, R.id.ms_msgOuterFrame);
        }
        else {
            showReceiveTime(listItemView, bean, R.id.ms_other_chat_time);
            showNickName(listItemView, bean, R.id.ms_otherSayTime);
            showImgHead(listItemView, bean, R.id.ms_otherHeadImg);
            showMsgContent(listItemView, bean, R.id.ms_otherSayMsg, R.id.ms_msgOuterFrame);
        }
    }

    /**
     * 显示提醒时间
     * */
    private void showReceiveTime(View listItemView, EmbMsg bean, int resId){
        TextView tv = (TextView)listItemView.findViewById(resId);
        if(tv != null && bean != null){
            Date createDate = bean.getCreatedDate();
            if (createDate != null){
                tv.setText(MsgConstants.getCaptionTime(sdf.format(createDate), "in"));
                tv.setVisibility(View.VISIBLE);//显示提醒时间
            }
        }
    }

    /**
     * 显示昵称
     * */
    private void showNickName(View listItemView, EmbMsg bean, int resId){
        TextView tv = (TextView)listItemView.findViewById(resId);
        if(tv != null && bean != null){
            String creator = bean.getCreatedBy();
            if (StringUtils.isEmpty(creator)){
                tv.setText("新用户");
            }else{
                tv.setText(bean.getCreatedBy());
            }
        }
    }

    /**
     * 显示头像图片
     * @param listItemView
     * @param bean
     * @param resId
     */
    private void showImgHead(View listItemView, final EmbMsg bean, int resId) {
        String headImageUrl = bean.getLocalheadimageurl();
        if (headImageUrl != null && headImageUrl.length() > 0) {
//            Log.d("Nat: MessageHeadImageUrl", String.format("%s,%s",bean.getCreatedBy(), headImageUrl));
            FineImgView hv = (FineImgView)listItemView.findViewById(resId);
            if (hv == null) {
                return;
            }

            hv.setNeedSample(true);
            hv.setMaxWidth(96);
            hv.setSrc(headImageUrl);
            final Activity context = this.getMyActivity();
            hv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if ("".equals(class_name) || class_name == null) {
                            return;
                        }
                        Intent intent = new Intent(context, (Class<? extends Activity>) Class.forName(class_name));
                        Long fromGuid = bean.getFromguid();
                        if (fromGuid != null && StringUtils.isDigit(String.valueOf(fromGuid))) {
                            intent.putExtra(Constants.BUNDLE_EXTRA_KEY_HUMAN_ID, Long.parseLong(String.valueOf(fromGuid)));
                            startActivity(intent);
                        } else {
                            showHint("信息不足，无法进入通讯录");
                        }

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 具体显示消息，支持多种媒体类型
     * @param listItemView
     * @param bean
     * @param oldTxtId
     * @param frameId 容器ResId
     */
    private void showMsgContent(View listItemView, EmbMsg bean, int oldTxtId, int frameId) {
        TextView tv = (TextView)listItemView.findViewById(oldTxtId);
        FrameLayout layout = (FrameLayout)listItemView.findViewById(frameId);

        String msgInfo = bean.getMsgInfo();
        if (msgInfo==null || !msgInfo.startsWith("{")){
            return;
        }
        WxParam msgParam = WxParam.fromJson(msgInfo);

        if (msgParam instanceof ImageParam) {
            ImageParam imgParam = (ImageParam)msgParam;
            final String picUrl = imgParam.getPicurl();

            if (picUrl != null && org.apache.commons.lang3.StringUtils.isNotBlank(picUrl)) {
//                Log.d("Nat: 图片信息", picUrl);
                FineImgView imgView = new FineImgView(this.getContext(), null);
                imgView.setNeedSample(true);
                imgView.setMaxWidth(400);
                imgView.setFao(MsgConstants.getMsgImgFao());
                imgView.setSrc(picUrl);
                final Activity context = this.getMyActivity();
                imgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(context, FullScreenActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("imgUrl", picUrl);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

                if(layout != null){
                    layout.removeView(tv);
                    layout.addView(imgView);
                    layout.setPadding(40,40,40,40);
                }
            }
            else{
                String msgContent = msgParam.getContent();
                if (msgContent != null){
//                    Log.d("Nat: 文本信息", msgParam.getContent());
                    tv.setText(msgParam.getContent());
                }
            }
        }
        else if (msgParam instanceof TextParam) {
            if (tv != null) {
                String content = "";
                if (msgParam.getContent() != null){
                    content = msgParam.getContent();
                }

                SpannableString spannableString = FaceUtil.getSpannable(getContext(),content,23,23);
                tv.setText(spannableString);
            }
            /*if (bean.getSessionid().equals(MsgConstants.SystemSessionId)) {
                String data = msgParam.getContent();
                //String content = data.substring(data.lastIndexOf(":") + 1);

              *//*  if (!StringUtil.isDigit(content))
                    return;*//*

                //tv.setText(data.substring(0, data.lastIndexOf(",")) + "（点击回复）");
                tv.setText(data.trim());
                *//*final Long id = Long.valueOf(content);
                final EmbSessionService sessionService = ServiceFactory.getService(EmbSessionService.class);
                final Context that = this.getContext();
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EmbSession session = sessionService.getDao().getEntityById(id);
                        if (session != null && sessionService.getDao().entityExistById(session.getId())){
                            Intent intent = new Intent(that, ChatActivity.class);
                            intent.putExtra("sessionId", session.getSessionId());
                            intent.putExtra("humanName", session.getHumanName());
                            intent.putExtra("humanId", session.getHumanId());
                            intent.putExtra("headImgUrl", session.getHeadImageUrl());
                            startActivity(intent);
                        }
                    }
                });*//*
            }*/
        }
        else if (msgParam instanceof ImageTextParam) {
            /*TextView tv1 = (TextView)layout.findViewById(R.id.ms_myReplyTime);
            TextView tv2 = (TextView)layout.findViewById(R.id.ms_myReplyMsg);
            if(msgParam.getContent() != null)
                tv2.setText(msgParam.getContent());
            if(msgParam.getSummary() != null)
                tv1.setText(msgParam.getSummary());*/
            if (((ImageTextParam) msgParam).getData().get(0).getPicurl() == null
                    || ((ImageTextParam) msgParam).getData().get(0).getPicurl().equals("")   ){
                String description = ((ImageTextParam) msgParam).getData().get(0).getDescription();
                description = ((ImageTextParam) msgParam).getData().get(0).getTitle()+ "\r\n" + description;
                tv.setText(description);
            }
            else {
                MaterialTwItemView view = new MaterialTwItemView(this.getContext(), null);
                List<MsgAttr> attrs = ResourceUtil.toMsgAttrs((ImageTextParam) msgParam);
                view.setMsgAttrs(attrs);
                layout.removeView(tv);
                layout.addView(view);
            }
        }
        else if (tv != null) {
            tv.setText(msgParam.getContent());
        }
    }

    private void showMsgContent(View listItemView, EmbMsg bean,
                                WxParam msgParam, int oldTxtId) {
        //showMessageTime(bean);
        TextView tv = (TextView)listItemView.findViewById(oldTxtId);
        if (msgParam instanceof ImageParam) {
            ImageParam imgParam = (ImageParam)msgParam;
            final String picUrl = imgParam.getPicurl();
//            Log.d("Nat: picUrl", picUrl);
            if (org.apache.commons.lang3.StringUtils.isNotBlank(picUrl)) {
                FrameLayout layout = (FrameLayout)listItemView.findViewById(R.id.ms_msgOuterFrame);
                FineImgView imgView = new FineImgView(this.getContext(), null);
                imgView.setNeedSample(true);
                imgView.setMaxWidth(400);
                imgView.setFao(MsgConstants.getMsgImgFao());
//                final String imgUrl = imgParam.getPicurl();
                imgView.setSrc(picUrl);
                if(layout != null){
                    layout.removeView(tv);
                    layout.addView(imgView);
                    layout.setPadding(40,20,40,20);
                }else{
                    MLog.d("FrameLayout is null");
                }

                final Activity context = this.getMyActivity();
                imgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(context, FullScreenActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("imgUrl", picUrl);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
            else{
                tv.setText(msgParam.getContent());
            }
        }
        else if (msgParam instanceof TextParam) {
            if (tv != null) {
                String content;
                if (msgParam.getContent() == null)
                    content = "空消息";
                else
                    content = msgParam.getContent();
                SpannableString spannableString = FaceUtil.getSpannable(getContext(),content,23,23);
                tv.setText(spannableString);
            }
            /*if (bean.getSessionid().equals(MsgConstants.SystemSessionId)) {
                String data = msgParam.getContent();
                //String content = data.substring(data.lastIndexOf(":") + 1);

              *//*  if (!StringUtil.isDigit(content))
                    return;*//*

                //tv.setText(data.substring(0, data.lastIndexOf(",")) + "（点击回复）");
                tv.setText(data.trim());
                *//*final Long id = Long.valueOf(content);
                final EmbSessionService sessionService = ServiceFactory.getService(EmbSessionService.class);
                final Context that = this.getContext();
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EmbSession session = sessionService.getDao().getEntityById(id);
                        if (session != null && sessionService.getDao().entityExistById(session.getId())){
                            Intent intent = new Intent(that, ChatActivity.class);
                            intent.putExtra("sessionId", session.getSessionId());
                            intent.putExtra("humanName", session.getHumanName());
                            intent.putExtra("humanId", session.getHumanId());
                            intent.putExtra("headImgUrl", session.getHeadImageUrl());
                            startActivity(intent);
                        }
                    }
                });*//*
            }*/

        }
        else if (msgParam instanceof ImageTextParam) {
            FrameLayout layout = (FrameLayout)listItemView.findViewById(R.id.ms_msgOuterFrame);
            /*TextView tv1 = (TextView)layout.findViewById(R.id.ms_myReplyTime);
            TextView tv2 = (TextView)layout.findViewById(R.id.ms_myReplyMsg);
            if(msgParam.getContent() != null)
                tv2.setText(msgParam.getContent());
            if(msgParam.getSummary() != null)
                tv1.setText(msgParam.getSummary());*/
            if (((ImageTextParam) msgParam).getData().get(0).getPicurl() == null
                    || ((ImageTextParam) msgParam).getData().get(0).getPicurl().equals("")   ){
                String description = ((ImageTextParam) msgParam).getData().get(0).getDescription();
                description = ((ImageTextParam) msgParam).getData().get(0).getTitle()+ "\r\n" + description;
                tv.setText(description);
            }
            else {
                MaterialTwItemView view = new MaterialTwItemView(this.getContext(), null);
                List<MsgAttr> attrs = ResourceUtil.toMsgAttrs((ImageTextParam) msgParam);
                view.setMsgAttrs(attrs);
                layout.removeView(tv);
                layout.addView(view);
            }
        }
        else if (tv != null) {
            tv.setText(msgParam.getContent());//bean.getId() + "_" +
        }
    }

    @Override
    protected List<KvBean<EmbMsg>> readListPageData(String searchToken,
                                   PageInfo pageInfo, NetProcessor.QueryRsProcessor<EmbMsg> callBack) {
        if (msgService == null){
            Log.e("Nat", "msgService is null");
            return null;
        }
        List<EmbMsg> msgs = msgService.getDao().queryMsgsBySessionId(sessionId, searchToken, pageInfo);
        //boolean haveSetRead = false;
        int setCount = 0;
//        boolean bSaveCreatedDate = false;
        for(EmbMsg msg : msgs){
//            if (bSaveCreatedDate){
//                showMessageTime(msg);//每隔15分钟显示
//            }else{
//                if (msg.getCreatedDate() != null){
//                    date = msg.getCreatedDate();
//                }
//            }
            //标记已读
            if(msg.getIsRead() == 0){
                msgService.getDao().setHaveReadBySessionId(msg.getSessionid());
                setCount ++;
            }
        }

        //if (setCount > 0) {
            //msgService.changeSessionUnReadCount(this.sessionId, -1);//若要精确控制未读数，使用：(0 - setCount)
        //}
        return KvBean.exportToKvsDirect(msgs);
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AppHelper.hideSoftInput(getMyActivity());
        Intent intent = new Intent(MsgConstants.ACTION_HIDE_MEDIAINPUT);
        this.getActivity().sendBroadcast(intent);
    }



    @Override
    public boolean isItemEnabled(int position) {
        return true;
    }


    @Override
    public BaseService getService() {
        return null;
    }

    /*
    * 这个方法用来做正确显示消息的提示时间之间相隔15分钟显示
    * params embMsg
    * */
    private void showMessageTime(EmbMsg embMsg) {
        Date current = null;

        if (embMsg != null){
             if (embMsg.getCreatedDate() != null)
                 current = embMsg.getCreatedDate();

            if (date == null && current != null){
                date = current;
            }else {
                if (null != current && current.getTime() - date.getTime() > 60*1000*5) {
                    date = current;
                }
                else {
                    if (null != current)
                        date = current;
                    embMsg.setCreatedDate(null);
                }
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 数据已经全部加载，或数据为空时，或正在加载，不处理滚动事件
        if (mState == STATE_LOADMORE || mState == STATE_REFRESH) {
            return;
        }

//        // 判断是否滚动到底部
//        boolean scrollEnd = false;
//        try {
//            if (view.getPositionForView(mAdapter.getFooterView()) == view
//                    .getLastVisiblePosition())
//                scrollEnd = true;
//        } catch (Exception e) {
//            scrollEnd = false;
//        }
//
//        if (mState == STATE_NONE && scrollEnd) {
//            if (mAdapter.getState() == ListBaseAdapter.STATE_LOAD_MORE
//                    || mAdapter.getState() == ListBaseAdapter.STATE_NETWORK_ERROR) {
//                mCurrentPage++;
//                mState = STATE_LOADMORE;
//                requestData(false);
//                mAdapter.setFooterViewLoading();
//            }
//        }

        super.onScrollStateChanged(view, scrollState);

        AppHelper.hideSoftInput(getMyActivity());
        Intent intent = new Intent(MsgConstants.ACTION_HIDE_MEDIAINPUT);
        this.getActivity().sendBroadcast(intent);

        if (isTop && !(mLoadedCount < mPageInfo.getTotalCount()) && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            MsgSetUtil msgSet = new MsgSetUtil(MfhLoginService.get().getLoginName());
            msgSet.saveMaxCreateTime(sessionId, TimeUtil.monthBefore(msgService.getDao().queryTheOldTime(sessionId)));
            //dialog = DialogUtil.genProgressDialog(getContext(), true, null);
            List<Long> sessionIds = new ArrayList<Long>();
            sessionIds.add(sessionId);
            Iterator<Long> iter = sessionIds.iterator();
            EmbMsgService.MyPageInfo pageInfoParam = msgService.new MyPageInfo(1, 100);//要求第一页从1开始
            pageInfoParam.setSessionIdIter(iter);
            msgService.queryFromNet(sessionId, TimeUtil.monthBefore(msgService.getDao().queryTheOldTime(sessionId)), pageInfoParam);
            //showHint("正在请求网络，请稍候...");
        }
    }

    @Override
    public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCoun) {
        super.onScroll(arg0, firstVisibleItem, visibleItemCount, totalItemCoun);
        if (isTop && !(mLoadedCount < mPageInfo.getTotalCount())) {

        }
    }





}
