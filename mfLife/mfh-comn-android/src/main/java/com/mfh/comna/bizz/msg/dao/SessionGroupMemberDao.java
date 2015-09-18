package com.mfh.comna.bizz.msg.dao;

import android.app.Activity;

import com.mfh.comna.bizz.login.LoginService;
import com.mfh.comna.comn.database.dao.BaseNetDao;
import com.mfh.comna.comn.database.dao.DaoUrl;
import com.mfh.comna.comn.database.dao.NetCallBack;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.network.NetFactory;
import com.mfh.comna.bizz.msg.MsgConstants;
import com.mfh.comna.bizz.msg.entity.SessionGroupMember;


import net.tsz.afinal.http.AjaxParams;

import java.util.List;

/**
 * 获取会话人的网络dao
 * Created by Administrator on 14-5-21.
 */
public class SessionGroupMemberDao extends BaseNetDao<SessionGroupMember, Long> {
    private int msgMode = -1;
    @Override
    protected void initUrlInfo(DaoUrl daoUrl) {
        /*daoUrl.setListUrl("/mobile/msg/getGroupMembersBySessionid");
        daoUrl.setCreateUrl("/mobile/msg/joinSessionGroup");
        daoUrl.setDeleteUrl("/mobile/msg/leaveSessionGroup");*/
        //setMsgMode(MsgConstants.MSG_MODE_TAX);
        if (msgMode != -1)
            setMsgMode(msgMode);

    }

    public void setMsgMode(int msgMode) {
        this.msgMode = msgMode;
        if (msgMode == MsgConstants.MSG_MODE_TAX){
            daoUrl.setListUrl("/mobile/msg/getGroupMembersBySessionid");
            daoUrl.setCreateUrl("/biz/msg/joinSessionGroup");
            daoUrl.setDeleteUrl("/mobile/msg/leaveSessionGroup");
        }
        else{
            daoUrl.setListUrl("/biz/msg/getGroupMembersBySessionid");
            daoUrl.setCreateUrl("/biz/msg/joinSessionGroup");
            daoUrl.setDeleteUrl("/biz/msg/leaveSessionGroup");
        }
    }

    @Override
    protected Class<SessionGroupMember> initPojoClass() {
        return SessionGroupMember.class;
    }

    private AjaxParams genParams(Long sessionId, List<String> guidList) {
        AjaxParams params = new AjaxParams();
        if (guidList != null) {
            StringBuilder guids = new StringBuilder();
            for (String item : guidList) {
                if (guids.length() > 0)
                    guids.append(",");
                guids.append(item);
            }
            params.put("guids", guids.toString());
        }
        params.put("sessionid", sessionId.toString());
        LoginService ls = ServiceFactory.getService(LoginService.class.getName());
        params.put("channelpointid", ls.getCpId());
        params.put("cpid", ls.getCpId());//服务器端不一致，后面改好可以删除
        params.put("guid", ls.getCurrentGuId());
        params.put("companyId", getContext().getSharedPreferences("login", Activity.MODE_PRIVATE).getString("app.spid", null));
        return params;
    }

    /**
     * 解散会话，把非固定人员解散,待实现
     * @param sessionId
     * @param callBack
     */
    public void releaseSessionGroup(Long sessionId, NetCallBack.NormalNetTask callBack) {
        AjaxParams params = genParams(sessionId, null);
        NetFactory.getHttp().get(getFullUrl(null, "/biz/msg/leaveAllSessionGroup"), params, callBack);
    }

    /**
     * 移除会话
     * @param sessionId
     * @param guidList
     * @param callBack
     */
    public void leaveSessionGroup(Long sessionId, List<String> guidList, NetCallBack.NormalNetTask callBack) {
        AjaxParams params = genParams(sessionId, guidList);
        NetFactory.getHttp().get(getFullUrl(DaoUrl.DaoType.multiDelete), params, callBack);
    }

    /**
     * 加入会话
     * @param sessionId
     * @param guidList
     * @param callBack
     */
    public void joinSessionGroup(Long sessionId, List<String> guidList, NetCallBack.NormalNetTask callBack) {
        AjaxParams params = genParams(sessionId, guidList);
        NetFactory.getHttp().get(getFullUrl(DaoUrl.DaoType.create), params, callBack);
    }

    @Override
    protected Class<Long> initPkClass() {
        return Long.class;
    }
}
