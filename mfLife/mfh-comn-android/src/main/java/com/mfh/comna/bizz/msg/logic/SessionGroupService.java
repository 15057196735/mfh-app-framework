package com.mfh.comna.bizz.msg.logic;

import android.content.Intent;

import com.mfh.comn.net.data.IResponseData;
import com.mfh.comna.comn.bean.KvBean;
import com.mfh.comna.comn.database.dao.NetCallBack;
import com.mfh.comna.comn.database.dao.NetProcessor;
import com.mfh.comna.comn.logic.BaseService;
import com.mfh.comna.comn.logic.DataSyncStrategy;
import com.mfh.comna.bizz.member.MemberConstants;
import com.mfh.comna.bizz.msg.dao.EmbSessionDao;
import com.mfh.comna.bizz.msg.dao.SessionGroupMemberDao;
import com.mfh.comna.bizz.msg.entity.EmbSession;
import com.mfh.comna.bizz.msg.entity.SessionGroupMember;


import net.tsz.afinal.http.AjaxParams;

import java.util.List;

/**
 * 参与会话人员的列表服务
 * Created by Administrator on 14-5-21.
 */
public class SessionGroupService extends BaseService<SessionGroupMember, Long, SessionGroupMemberDao> {

    SessionGroupMemberDao netDao = new SessionGroupMemberDao();
    private int msgMode = -1;
    private int index = 0;
    private EmbSessionDao dao = new EmbSessionDao();

    @Override
    protected Class<SessionGroupMemberDao> getDaoClass() {
        return SessionGroupMemberDao.class;
    }

    @Override
    public DataSyncStrategy getDataSyncStrategy() {
        return null;
    }

    /**
     * 设置消息模式
     * @param msgMode
     */
    public void setMsgMode(int msgMode) {
        this.msgMode = msgMode;
        netDao.setMsgMode(msgMode);
    }

    /**
     * 从网络上读取参与会话的人员列表
     * @param sessionId 会话标识
     */
    public void queryFromNet(Long sessionId, NetProcessor.QueryRsProcessor<SessionGroupMember> callback) {
        AjaxParams params = new AjaxParams();
        params.put("sessionid", sessionId.toString());
        netDao.queryAll(params, callback);
    }

    public void releaseAllSessionGroup(final List<KvBean<EmbSession>> embSessions) {
        if (index >= embSessions.size()) {
            Intent intent = new Intent(MemberConstants.RELEASE_GROUP_FINISH);
            getContext().sendBroadcast(intent);
            index = 0;
            return;
        }
        if (index == 0) {
            Intent intent = new Intent(MemberConstants.RELEASE_GROUP_START);
            getContext().sendBroadcast(intent);
        }
        final EmbSession embSession = embSessions.get(index).getBean();
        getNetDao().releaseSessionGroup(embSession.getId(), new NetCallBack.NormalNetTask(String.class) {
            @Override
            public void processResult(IResponseData rspData) {
                index ++;
                embSession.setIsGroup(2);
                dao.saveOrUpdate(embSession);
                releaseAllSessionGroup(embSessions);
            }
        });
    }

    public SessionGroupMemberDao getNetDao() {
        return netDao;
    }

    public void setNetDao(SessionGroupMemberDao netDao) {
        this.netDao = netDao;
    }
}
