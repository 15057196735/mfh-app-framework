package com.mfh.comna.bizz.msg.logic;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mfh.comna.api.Constants;
import com.mfh.comna.bizz.config.URLConf;
import com.mfh.comna.api.helper.SharedPreferencesHelper;
import com.mfh.comna.api.helper.UIHelper;
import com.mfh.comna.api.utils.MLog;
import com.mfh.comna.bizz.login.logic.MfhLoginService;
import com.mfh.comna.bizz.msg.MsgConstants;
import com.mfh.comna.bizz.msg.dao.EmbSessionDao;
import com.mfh.comna.bizz.msg.dao.EmbSessionNetDao;
import com.mfh.comna.bizz.msg.entity.EmbMsg;
import com.mfh.comna.bizz.msg.entity.EmbSession;
import com.mfh.comna.bizz.msg.entity.RSBizSessionWithMsgParam;
import com.mfh.comna.bizz.msg.view.ChatActivity;
import com.mfh.comn.bean.PageInfo;
import com.mfh.comn.bean.TimeCursor;
import com.mfh.comn.bean.msg.BizSessionBean;
import com.mfh.comn.bean.msg.MsgParameterBean;
import com.mfh.comn.net.data.IResponseData;
import com.mfh.comn.net.data.RspBean;
import com.mfh.comn.net.data.RspQueryResult;
import com.mfh.comna.bizz.BizApplication;
import com.mfh.comna.comn.database.dao.NetCallBack;
import com.mfh.comna.comn.database.dao.NetProcessor;
import com.mfh.comna.comn.logic.BaseService;
import com.mfh.comna.comn.logic.DataSyncStrategy;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.network.NetFactory;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 消息会话服务类
 * Created by Administrator on 14-5-6.
 */
public class EmbSessionService extends BaseService<EmbSession, Long, EmbSessionDao> {
    private static final int MAX_PAGE_SIZE = 100;
    private static final int MSG_BRIDGE_VERSION = 1;
    private static final Boolean QUERY_RESULT_SYNC = false;
    private EmbSessionNetDao netDao = new EmbSessionNetDao();
    private EmbMsgService msgService = ServiceFactory.getService(EmbMsgService.class);

    private MsgSetUtil msgSet = new MsgSetUtil(MfhLoginService.get().getLoginName());
    private Long cursorValue = -1L;//临时游标，因为要多次分页查询，故需要暂存此轮查询得到的最大游标值
    private boolean bHaveAlert = false;
    private int msgMode;

    private static EmbSessionService instance = null;
    /**
     * 返回 EmbSessionService 实例
     * @return
     */
    public static EmbSessionService get() {
        String lsName = EmbSessionService.class.getName();
        if (ServiceFactory.checkService(lsName))
            instance = ServiceFactory.getService(lsName);
        else {
            instance = new EmbSessionService();//初始化登录服务
        }
        return instance;
    }

    @Override
    protected Class<EmbSessionDao> getDaoClass() {
        return EmbSessionDao.class;
    }

    @Override
    public DataSyncStrategy getDataSyncStrategy() {
        return null;
    }

    /**
     * 保存一个消息会话
     *
     * @param embSession
     */
    public void save(EmbSession embSession) {
        getDao().save(embSession);
    }



    public void updateTopOrder(Long sessionId) {
        getDao().updateTopOrder(sessionId);
    }

    public void resetTopOrder(Long sessionId) {
        getDao().resetTopOrder(sessionId);
    }

    /**
     * 设置消息模式
     *
     * @param msgMode
     */
    public void setMsgMode(int msgMode) {
        this.msgMode = msgMode;
        netDao.setMsgMode(msgMode);
    }

    public int getMsgMode(){
        return this.msgMode;
    }


    /**
     * 清理当前用户的所有本地会话
     */
    public void clearMsgs() {
        String ownerName = MfhLoginService.get().getLoginName();
        try {
            getDao().clearSessions(ownerName);
            msgService.getDao().clearMsgs(ownerName);
            msgSet.clearConfig();
        }
        catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * 把所有会话都变成已读
     */
    public void isRead() {
        List<EmbSession> list = getDao().queryMySessions(MfhLoginService.get().getLoginName(),
                "", new PageInfo(0, 1000));
        for(EmbSession session : list){
            session.setUnreadcount(0L);
            MsgHelper.changeSessionUnReadCount(getContext(), session.getId(), -1);
        }

        MsgHelper.sendBroadcastForUpdateUnread(getContext(), 0);
    }

    private void getSessionListBySessionId(Long sessionid) {
        PageInfo pageInfo = new PageInfo(1, 100);//要求第一页从1开始
        final String lastCursor = Long.toString(msgSet.getLastUpdate());
        AjaxParams params = new AjaxParams();
//        params.put("guid", SharedPreferencesHelper.getUserGuid());
        params.put("sessionid", String.valueOf(sessionid));
        //params.put("subdisid", SharedPreferencesHelper.getUserSubdisId());
        netDao.query(params, new NetProcessor.QueryRsProcessor<RSBizSessionWithMsgParam>(pageInfo) {
            @Override
            public void processQueryResult(RspQueryResult<RSBizSessionWithMsgParam> rs) {
                new SaveQueryResultAsync(lastCursor, pageInfo).execute(rs);
            }
        }, URLConf.URL_GET_SESSION_BY_ID);
    }

    private void getSessionBySessionId(Long sessionid) {
//        PageInfo pageInfo = null;//不支持分页
//        final String lastCursor = Long.toString(msgSet.getLastUpdate());

        AjaxParams params = new AjaxParams();
//        params.put("guid", SharedPreferencesHelper.getUserGuid());
        params.put("sessionid", String.valueOf(sessionid));
        //params.put("subdisid", SharedPreferencesHelper.getUserSubdisId());

        //回调
        NetCallBack.NetTaskCallBack callback = new NetCallBack.NetTaskCallBack<RSBizSessionWithMsgParam,
                NetProcessor.Processor<RSBizSessionWithMsgParam>>(new NetProcessor.Processor<RSBizSessionWithMsgParam>() {
            @Override
            public void processResult(IResponseData rspData) {
                RspBean<RSBizSessionWithMsgParam> retValue = (RspBean<RSBizSessionWithMsgParam>)rspData;

                EmbSession bean = saveRSBizSessionWithMsgParam(retValue.getValue());

                if(bean != null){
                    msgSet.saveLastUpdate(bean.getLastupdate());
                }
            }
        }, RSBizSessionWithMsgParam.class, BizApplication.getAppContext()) {
        };

        MLog.d(String.format("[POST]getSessionById:%s?%s", URLConf.URL_GET_SESSION_BY_ID, params.toString()));
        NetFactory.getHttp().post(URLConf.URL_GET_SESSION_BY_ID, params, callback);
    }


    /**
     * 向后台执行一次查询请求
     */
    public void queryFromNet() {
        /*if (netDao.isDownLoading())
            return;*/
        PageInfo pageInfoParam = new PageInfo(1, MAX_PAGE_SIZE);//要求第一页从1开始
        String lastCursor = Long.toString(msgSet.getLastUpdate());
        //如果最后一次更新时间为空，则使用登录时间
        if (lastCursor.equals("-1") || lastCursor.equals("")) {
            try {
//                TimeCursor.INNER_DATAFORMATSHORT
                lastCursor = String.valueOf(new SimpleDateFormat(TimeCursor.INNER_DATAFORMAT)
                        .parse(msgSet.getMaxMsgUpdateDate()).getTime() / 1000);
            } catch (ParseException e) {
                MLog.e("queryFromNet:" + e.toString());
//                e.printStackTrace();
            }
        }

        //满足一定条件使用新的查询，否则用老的
        if (MSG_BRIDGE_VERSION > 0) {
            queryFromNewMsgBridge(lastCursor, pageInfoParam);
        } else {
            queryFromNet(lastCursor, pageInfoParam);
        }
    }

    /**
     * 获取消息列表
     * */
    private void queryFromNewMsgBridge(String lastCursor, PageInfo pageInfoParam) {
        AjaxParams params = new AjaxParams();
        //个人账号：guid + type(0)
        //客服账号：createguid(在管家会话里，创建人就是粉丝) + type(0)
        params.put(Constants.PARAM_KEY_CREATE_GUID, MfhLoginService.get().getCurrentGuId());
        //createguid, 在管家会话里，参与成员
//        params.put("guid", SharedPreferencesHelper.getUserGuid());

//        其中type代表非客服类会话类型，包括： 0、两人会话； 2：群组会话
        params.put(Constants.PARAM_KEY_TYPE, "101");
//        bind代表要查询客服类会话并且其中的客户类型为:0:未绑定； 1:已绑定；  2:未关联
//
        //params.put("isgroup", "1");
        params.put(Constants.PARAM_KEY_BIND, "1");

        //params.put("subdisid", SharedPreferencesHelper.getUserSubdisId());
        params.put(Constants.PARAM_KEY_LASTUPDATE, lastCursor);
        //params.put("tenantId", getContext().getSharedPreferences("login", Activity.MODE_PRIVATE).getString("app.spid", null));
        queryByNetDao(params, pageInfoParam, lastCursor, URLConf.URL_SESSION_LIST);
    }


    /**
     * 执行网络请求
     *
     * @param pageInfoParam
     */
    private void queryFromNet(final String lastCursor, PageInfo pageInfoParam) {
        AjaxParams params = new AjaxParams();
        params.put(Constants.PARAM_KEY_LASTUPDATE, lastCursor);
        try {
            String subdisIds = MfhLoginService.get().getCurrentSessionId();
            if (StringUtils.isBlank(subdisIds))
                throw new RuntimeException("所辖小区为空!");

            if (msgMode == MsgConstants.MSG_MODE_APART) {
                params.put(Constants.PARAM_KEY_CREATE_GUID, MfhLoginService.get().getCurrentGuId());
                params.put(Constants.PARAM_KEY_SUBDIS_ID, subdisIds);
            } else if (msgMode == MsgConstants.MSG_MODE_TAX) {
                params.put(Constants.PARAM_KEY_BUREAD_UID, subdisIds);
            } else {
                params.put(Constants.PARAM_KEY_SUBDIS_ID, subdisIds);
            }
            queryByNetDao(params, pageInfoParam, lastCursor, null);
        } catch (Throwable e) {
            netDao.resetDownLoading();
            throw new RuntimeException(e);
        }
    }

    public void queryByNetDao(AjaxParams params, PageInfo pageInfoParam, final String lastCursor, String url) {
        final EmbSessionService that = this;

        netDao.query(params, new NetProcessor.QueryRsProcessor<RSBizSessionWithMsgParam>(pageInfoParam) {
            @Override
            public void processQueryResult(RspQueryResult<RSBizSessionWithMsgParam> rs) {//此处在主线程中执行。
                //考虑到修改本地数据库也比较耗时，故再采用异步。
                if (QUERY_RESULT_SYNC) {
                    that.saveQueryResult(rs, pageInfo);
                    if (continueOrBreak(pageInfo)) {//若还有继续发起请求,并且最多下载500个会话。
                        pageInfo.moveToNext();
                        queryFromNewMsgBridge(lastCursor, pageInfo);
                    }
                } else {
                    new SaveQueryResultAsync(lastCursor, pageInfo).execute(rs);
                }


                //判断结果集中是否存在未读数据，存在则提示手机震动
                boolean isRead = false;

                //提示手机震动
                if (rs.getReturnNum() > 0) {
                    //专门为提示框做的广播机制
                    Intent intent = new Intent(MsgConstants.ACTION_RECEIVE_MSG);
                    rs.getRowEntity(0);//调到第一个session
                    getContext().sendBroadcast(intent);
//                    NoticeUtil.showNotification(g, MsgConstants.MSG_NOTIFICATION, "消息提示", "您有新的消息");
                    for (int i = 0; i < rs.getReturnNum(); i++) {
                        RSBizSessionWithMsgParam bean = rs.getRowEntity(i);
                        if (bean.getSession().getUnreadcount() > 0) {
                            isRead = true;
                        }
                    }
                    if (isRead) {
                           /* MsgTimer msgTimer = ServiceFactory.getService(MsgTimer.class.getName());
                       *//* if (!bHaveAlert && msgTimer.isRunAtBack()) {*//*
                            Activity context = (Activity) that.getContext();
                            if (!NoticeUtil.noticeVoice(context))
                                NoticeUtil.noticeMusic(context, R.raw.msg);
                            NoticeUtil.Vibrate(context, 2000);*/
                        bHaveAlert = true;
                        // showNotification();
                    }
                    //}
                }
            }
        }, url);
    }

    /**
     * 设置没有震动过了
     */
    public void resetHaveAlert() {
        this.bHaveAlert = false;
    }

    public void getSystemMessage() {
        AjaxParams params = new AjaxParams();
        PageInfo pageInfoParam = new PageInfo(1, 100);//要求第一页从1开始
        final String lastCursor = Long.toString(msgSet.getLastUpdate());
//        Integer myCount = getDao().getMyCount(SharedPreferencesHelper.getLoginUsername());
        params.put("guid", "1");
        params.put("subdisid", "12");

        netDao.query(params, new NetProcessor.QueryRsProcessor<RSBizSessionWithMsgParam>(pageInfoParam) {
            @Override
            public void processQueryResult(RspQueryResult<RSBizSessionWithMsgParam> rs) {
                new SaveQueryResultAsync(lastCursor, pageInfo).execute(rs);
            }
        }, "/getSysSessionItems");
    }

    /**
     * 将后台返回的结果集保存到本地,同步执行
     *
     * @param rs       结果集
     * @param pageInfo 分页信息
     */
    private void saveQueryResult(RspQueryResult<RSBizSessionWithMsgParam> rs, PageInfo pageInfo) {//此处在主线程中执行。
        try {
            if (rs == null){
                return;
            }

            //保存下来
            int retSize = rs.getReturnNum();
            MLog.d(String.format("receive %d sessions", retSize));
            if(retSize <= 0){
                return;
            }

            for (int ii = 0; ii < retSize; ii++) {
                saveRSBizSessionWithMsgParam(rs.getRowEntity(ii));
            }

            getContext().sendBroadcast(new Intent(MsgConstants.ACTION_REFRESH_UNREAD_COUNT_MAIN));
            getContext().sendBroadcast(new Intent(MsgConstants.ACTION_RECEIVE_SESSION));
        } catch (Throwable ex) {
            netDao.resetDownLoading();
//            throw new RuntimeException(ex);
            MLog.e(String.format("saveQueryResult failed, %s", ex.toString()));
        }
    }

    /**
     * 保存查询到的session
     * */
    private EmbSession saveRSBizSessionWithMsgParam(RSBizSessionWithMsgParam sessionWithMsgParam){
        if (sessionWithMsgParam == null){
            return null;
        }
        MLog.d(String.format("saveRSBizSessionWithMsgParam: %s", sessionWithMsgParam.toString()));

        EmbSession bean = new EmbSession();

        BizSessionBean bizSessionBean = sessionWithMsgParam.getSession();
        Long sessionId = null;
        if(bizSessionBean != null){
            sessionId = bizSessionBean.getId();
            Long lastUpdate = bizSessionBean.getLastupdate();
            if (lastUpdate != null && lastUpdate > cursorValue){
                cursorValue = lastUpdate;
            }

            bean.setId(sessionId);
            bean.setSessionid(sessionId);
            bean.setLastupdate(lastUpdate);
            bean.setHumanname(bizSessionBean.getHumanname());//会话创建者姓名
            bean.setLocalheadimageurl(bizSessionBean.getHeadimageurl());//会话创建者头像
            bean.setUnreadcount(bizSessionBean.getUnreadcount());
        }

        MsgParameterBean msgParameterBean = sessionWithMsgParam.getLastMsg();
        if(msgParameterBean != null){
            bean.setFormatCreateTime(msgParameterBean.getFormatCreateTime());
            bean.setParam(msgParameterBean.getMsgBean().toString());
            bean.setSpokesman(msgParameterBean.getSpokesman());
        }

        Integer oldCount = getDao().getSessionUnReadCount(sessionId);
        if (oldCount == null) {
            bean.setOwnerId(MfhLoginService.get().getLoginName());

            getDao().save(bean);
        } else {
            bean.setUnreadcount(oldCount + 1L);

            //对话有更新时防止topSessionOrder这个字段被重写所以要判定一下
            bean.setTopSessionOrder(getDao().isTopOrder(bean.getId()) ?
                    getDao().getEntityById(bean.getId()).getTopSessionOrder() :
                    EmbSession.DEFAULT_NOT_TOP_ORDER);

            getDao().update(bean);
        }

        return bean;
    }
    /**
     * 是否继续下载,若不需要则持久化保存此轮查询涉及到的最大游标。
     *
     * @param pageInfo
     * @return
     */
    private boolean continueOrBreak(PageInfo pageInfo) {
        if (pageInfo.hasNextPage()
                && (pageInfo.getHavedCount() < msgSet.getMaxSessionNum()))//若还有继续发起请求,并且最多下载500个会话。
            return true;
        else {
            //下载完毕
            UIHelper.sendBroadcast(MsgConstants.ACTION_DOWNLOAD_FINISH);
            try {
                if (pageInfo.getTotalCount() > 0 && cursorValue > -1) {
                    //保存最新游标
                    long lastCursor = msgSet.getLastUpdate();
                    msgSet.saveLastUpdate(cursorValue);

                    //触发下载每个session的消息
                    List<EmbSession> sessionList = getDao().getNewSessions(MfhLoginService.get().getLoginName(), lastCursor);
                    List<Long> sessionIds = new ArrayList<Long>();
                    for(EmbSession session : sessionList){
                        sessionIds.add(session.getId());
                    }
                    msgService.queryFromNets(sessionIds);
                }
            } catch (Throwable ex) {
                logger.error(ex.getMessage(), ex);
            } finally {
                netDao.restDownLoadingWithNoIntent();
            }
            return false;
        }
    }

    /**
     * 请求后台，保留最大的游标
     */
    public void queryFromNetToSaveMaxUpDateDate() {
        AjaxParams params = new AjaxParams();

        params.put("lastupdate", String.valueOf(msgSet.getLastUpdate()));
        try {
            String subdisIds = MfhLoginService.get().getMySubdisIds();
            if (StringUtils.isBlank(subdisIds))
                throw new RuntimeException("所辖小区为空!");
            if (msgMode == MsgConstants.MSG_MODE_APART) {
                params.put(Constants.PARAM_KEY_GUID, MfhLoginService.get().getCurrentGuId());
                params.put(Constants.PARAM_KEY_SUBDIS_ID, subdisIds);
            } else if (msgMode == MsgConstants.MSG_MODE_TAX) {
                params.put(Constants.PARAM_KEY_BUREAD_UID, subdisIds);
            } else {
                params.put(Constants.PARAM_KEY_SUBDIS_ID, subdisIds);
            }
            PageInfo pageInfoParam = new PageInfo(1, 100);//要求第一页从1开始
            netDao.query(params, new NetProcessor.QueryRsProcessor<RSBizSessionWithMsgParam>(pageInfoParam) {
                @Override
                public void processQueryResult(RspQueryResult<RSBizSessionWithMsgParam> rs) {//此处在主线程中执行。
                    cursorValue = rs.getRowDatas().get(0).getBean().getSession().getLastupdate();
                    msgSet.saveLastUpdate(cursorValue);
                    if (msgSet.getMaxMsgUpdateDate().equals(""))
                        msgSet.setMaxMsgUpdateDate(MsgHelper.SDF_INNER_DATAFORMAT.format(new Date()));
                }
            });

        } catch (Throwable e) {
            netDao.resetDownLoading();
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过humanId发送消息给业主
     *
     * @param humanId
     */
    public void replayByHumanId(Long humanId, final String subdisId) {
        AjaxParams params = new AjaxParams();
        params.put("guid", humanId + "");
        params.put("subdisid", subdisId);
        NetFactory.getHttp().post(NetFactory.getServerUrl() + "/biz/msg/queryCsSessionId", params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                try {
                    JSONObject jsonObject = JSONObject.parseObject(o.toString());
                    JSONObject object = jsonObject.getJSONObject("data");
                    String msg = jsonObject.getString("msg");
                    String sessionId = object.getString("val");
                    if ("null".equals(msg)) {
                        Toast.makeText(getContext(), "用户绑定异常", Toast.LENGTH_SHORT).show();
                    } else {
                        replay(sessionId, subdisId);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "replayByHumanId方法异常,EmbSessionService", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 回复业主
     *
     * @param sessionId
     */
    private void replay(String sessionId, String subdisid) {

        AjaxParams params = new AjaxParams();
        params.put("sessionid", sessionId);
        params.put("subdisid", subdisid);
        netDao.getEntityParams(params, new NetProcessor.QueryRsProcessor<RSBizSessionWithMsgParam>(null) {
            @Override
            public void processQueryResult(RspQueryResult<RSBizSessionWithMsgParam> rs) {
                RSBizSessionWithMsgParam session = rs.getRowEntity(0);
//                session.setOwnerId(SharedPreferencesHelper.getLoginUsername());
                getDao().save(new EmbSession());
                if (session != null && getDao().entityExistById(session.getSession().getId())) {
                    ChatActivity.actionStart(getContext(), session.getSession().getId());
                }
            }
        }, "/biz/msg/getSessionListBySessionId");
    }

    /**
     * 保存新会话
     * */
    public void saveNewSession(String jsonString) {
        EmbSession session = changeJsonToSession(jsonString);
        if(session == null){
            return;
        }

//        Long sessionId = session.getSessionid();
        Long id = session.getId();
        MLog.d(String.format("receive new session %s", (id != null ? id : "")));

        if (id != null && getDao().entityExistById(id)) {
            //更新会话
            EmbSession tSession = getDao().getEntityById(id);
            tSession.setLastupdate(session.getLastupdate());
            tSession.setCreatetime(session.getCreatetime());
            tSession.setParam(session.getParam());
            tSession.setUnreadcount(tSession.getUnreadcount() + 1);//每一次个推，未读数量+1
            tSession.setOwnerId(MfhLoginService.get().getLoginName());
            getDao().update(tSession);
            //
            msgSet.saveLastUpdate(session.getLastupdate());

            UIHelper.sendBroadcast(MsgConstants.ACTION_RECEIVE_SESSION);
        } else {
            //TODO 查询新会话
//            getSessionListBySessionId(sessionId);
//            getSessionBySessionId(sessionId);
            //接收到新消息，需要做一次查询，然后更新显示最后一条消息内容。
            queryFromNet();
        }

        UIHelper.sendBroadcast(MsgConstants.ACTION_REFRESH_UNREAD_COUNT_MAIN);
        UIHelper.sendBroadcast(MsgConstants.ACTION_DOWNLOAD_FINISH);
    }

    /**
     * */
    public EmbSession changeJsonToSession(String jsonString) {
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        JSONObject msgObj = jsonObject.getJSONObject("msg");
        JSONObject msgBeanObj = msgObj.getJSONObject("msgBean");
//        JSONObject msgBodyObj = msgBeanObj.getJSONObject("body");
        JSONObject msgToObj = msgObj.getJSONObject("to");

        EmbSession session = new EmbSession();
        //收到的消息不包含会话id，会话id为空
        session.setSessionid(msgToObj.getLong("sid"));
        session.setId(msgToObj.getLong("sid"));
        if(msgBeanObj != null){
            session.setParam(com.alibaba.fastjson.JSONObject.toJSONString(msgBeanObj));
        }

        try {
            //"2015-06-30 14:39:39"
            String createTime = msgBeanObj.getString("time");
            if(createTime != null){
                Date create = new SimpleDateFormat(TimeCursor.INNER_DATAFORMAT).parse(createTime);
                if(create != null){
                    session.setLastupdate((long) (create.getTime() * 0.001));
                    session.setCreatetime(create);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("Nat", "时间格式不正确");
            return null;
        }

        return session;
    }


    /**
     * 内部类，执行异步保存到本地数据库
     */
    private class SaveQueryResultAsync extends AsyncTask<RspQueryResult<RSBizSessionWithMsgParam>, Integer, Long> {
        private PageInfo pageInfo;
        private String lastCursor;

        /**
         * 构造函数
         *
         * @param pageInfo
         */
        public SaveQueryResultAsync(String lastCursor, PageInfo pageInfo) {
            this.lastCursor = lastCursor;
            this.pageInfo = pageInfo;
        }

        @Override
        protected void onPostExecute(Long s) {//主线程执行
            super.onPostExecute(s);
            //若还有继续发起请求
            if (continueOrBreak(pageInfo)) {
                pageInfo.moveToNext();
                queryFromNewMsgBridge(lastCursor, pageInfo);
            }
        }

        @Override
        protected Long doInBackground(RspQueryResult<RSBizSessionWithMsgParam>... params) {//后台线程执行
            saveQueryResult(params[0], pageInfo);
            return -1L;
        }
    }

    /**
     * 改变session，用于发送消息之后，仅仅改变最后发言内容和时间戳
     * @param embMsg
     */
    public void changeSessionBySendMessage(EmbMsg embMsg) {
        EmbSession session = getDao().getEntityById(embMsg.getSessionid());
        if(session == null){
            return;
        }

        Date createdDate = embMsg.getCreatedDate();
        if(createdDate != null){
            session.setCreatetime(createdDate);
            session.setLastupdate((long) (createdDate.getTime() * 0.001));
        }

        session.setParam(embMsg.getParam());
        getDao().saveOrUpdate(session);
    }

    /**
     * 将未读消息数置为0
     * */
    public void resetUnReadMsgCount(Long sid){
        getDao().resetUnReadMsgCount(sid);
    }

}


