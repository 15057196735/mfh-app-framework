package com.mfh.comna.bizz.msg.dao;

import android.text.TextUtils;

import com.mfh.comna.bizz.login.LoginService;
import com.mfh.comn.bean.PageInfo;
import com.mfh.comn.bean.Pair;
import com.mfh.comna.comn.database.dao.BaseDbDao;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.bizz.msg.entity.EmbSession;

import java.util.List;

/**
 * 会话
 * Created by Administrator on 14-5-6.
 */
public class EmbSessionDao extends BaseDbDao<EmbSession, Long> {

    private static final String TABLE_NAME = "emb_session";
    private static final String TABLE_NAME_CH = "消息会话表";

    @Override
    protected Pair<String, String> initTableChName() {
        return new Pair<>(TABLE_NAME_CH, TABLE_NAME);
    }

    @Override
    protected Class<EmbSession> initPojoClass() {
        return EmbSession.class;
    }

    @Override
    protected Class<Long> initPkClass() {
        return Long.class;
    }

    /**
     * 查询自该时间戳以来的新的会话Id
     *
     * @param lastUpdate
     * @return
     */
    public List<Long> getNewSessionIds(String ownerId, long lastUpdate) {
//        Log.d("Nat: EmbSessionDao", String.format("getNewSessionIds.ownerId=%s, lastUpdate=%s", ownerId, String.valueOf(lastUpdate)));
        //TODO
        List<Long> ret = getDb().findAllBySql(Long.class,
                "select id from emb_session where lastUpdate > ? and ownerId = ?", new String[]{Long.toString(lastUpdate), ownerId});
        return ret;
    }

    public List<EmbSession> getNewSessions(String ownerId, long lastUpdate) {
//        Log.d("Nat: EmbSessionDa", String.format("getNewSessions.ownerId=%s, lastUpdate=%s", ownerId, String.valueOf(lastUpdate)));
        //TODO
        List<EmbSession> ret = getDb().findAllBySql(EmbSession.class,
                "select id from emb_session where lastUpdate > ? and ownerId = ?", new String[]{Long.toString(lastUpdate), ownerId});
        return ret;
    }



    /**
     * 获取指定人员所有未读消息数
     *
     * @param ownerId
     * @return
     */
    public Integer getTotalUnReadCount(String ownerId) {
        if (this.getDb().tableIsExist("emb_session") && this.getDb().tableIsExist("emb_msg"))
            return this.getDb().findBySql(Integer.class,
                    "select sum(unReadCount) from emb_session where ownerId=?", new String[]{ownerId});
        else
            return 0;
    }

    /**
     * 查询属于本人会话的个数
     *
     * @param ownerId
     * @return
     */
    public Integer getMyCount(String ownerId) {
        getDb().checkTableExist(EmbSession.class);

        if(TextUtils.isEmpty(ownerId)){
            return 0;
        }
        return this.getDb().findBySql(Integer.class,
                "select count(*) from emb_session where ownerId=?", new String[]{ownerId});
    }

    /**
     * 获取指定session的未读消息数
     *
     * @param sessionId
     * @return
     */
    public Integer getSessionUnReadCount(Long sessionId) {
        getDb().checkTableExist(EmbSession.class);
        if (sessionId == null){
            return null;
        }

//        "select unReadCount from emb_session where id=?"
        return this.getDb().findBySql(Integer.class,
                "select unReadCount from emb_session where id=?", new String[]{sessionId.toString()});
    }

    /**
     * 增加或减少未读消息个数
     *
     * @param sessionId 会话Id
     * @param unCount   未读数
     */
    public void addUnReadCount(Long sessionId, int unCount) {
        EmbSession session = getEntityById(sessionId);
        if (session == null)
            return;
        int oldCount = Integer.valueOf(String.valueOf(session.getUnreadcount()));
        if (oldCount == -1)
            oldCount = 0;

        if (unCount < 0) {//对应于点击会话进去的场景，无论多少只要点进去就认为全部读过了，直接置0.
            oldCount = 0;
        }
        else {
            oldCount += unCount;
            if (oldCount < 0)
                oldCount = 0;//防止小于0出现。
        }
        getDb().exeSql("update emb_session set unReadCount = ? where id = ?", new Object[]{oldCount, sessionId});
    }

    /**
     * 将未读消息数置为0
     *
     * @param sessionId
     */
    public void resetUnReadMsgCount(Long sessionId) {
        getDb().exeSql("update emb_session set unReadCount = 0 where id = ?", new Object[]{sessionId});
    }

    /**
     * 按照时间逆序查询我的所有会话，支持分页信息
     *
     * @param ownerId
     * @param pageInfo
     */
    public List<EmbSession> queryMySessions(String ownerId, String searchToken, PageInfo pageInfo) {
        String sqlWhere = "ownerId='" + ownerId + "'";
        if (searchToken != null && searchToken.length() > 0) {
//            sqlWhere += "and humanName or nickName like '%" + searchToken + "%'";
            sqlWhere += "and humanName like '%" + searchToken + "%'";
        }
        return getDb().findAllByWhere(EmbSession.class, sqlWhere, "topSessionOrder desc, lastUpdate desc", pageInfo);
    }

    /**
     * 按照时间逆序查询我的所有会话，支持分页信息
     *
     * @param ownerName
     * @param pageInfo
     */
    public List<EmbSession> queryForSearch(String ownerName, String searchToken, PageInfo pageInfo) {
        String sqlWhere = "ownerId='" + ownerName + "'";
        if (searchToken != null && searchToken.length() > 0) {
            sqlWhere += "and (humanName like '%" + searchToken + "%'" + " or nicknamebin like '%" + searchToken + "%')";
            return getDb().findAllByWhere(EmbSession.class, sqlWhere, "topSessionOrder desc,sessionOrder,lastUpdate desc", pageInfo);
        }
        return null;
    }

    /**
     * 清理当前用户的所有会话
     *
     * @param ownerName
     */
    public void clearSessions(String ownerName) {
        try {
            String sqlWhere = "ownerId='" + ownerName + "'";
            getDb().deleteByWhere(EmbSession.class, sqlWhere);
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * 获取置顶序号
     */
    public Long getTopOrder(Long sessionId) {
        return this.getEntityById(sessionId).getTopSessionOrder();
    }

    /**
     * 判断指定次序是否为最小次序
     *
     * @param sessionId 会话标识
     * @return
     */
    public boolean isTopOrder(Long sessionId) {
        /*Integer curOrder = this.getEntityById(sessionId).getSessionOrder();
        if (EmbSession.DEFAULT_ORDER == curOrder.intValue())
            return false;
        Integer minOrder = getDb().findBySql(Integer.class, "select min(sessionOrder) from emb_session", null);
        if (curOrder.equals(minOrder))
            return true;
        else
            return false;*/
        Long curTopSessionOrder = this.getEntityById(sessionId).getTopSessionOrder();
        if (curTopSessionOrder != EmbSession.DEFAULT_NOT_TOP_ORDER)
            return true;
        else
            return false;
    }

    /**
     * 将对话置顶
     *
     * @param sessionId
     */
    public void updateTopOrder(Long sessionId) {
        /*Integer minOrder = getDb().findBySql(Integer.class, "select min(sessionOrder) from emb_session", null);
        minOrder = minOrder - 1;
        getDb().exeSql("update emb_session set sessionOrder = ? where id = ?", new Object[]{minOrder, sessionId});*/
        getDb().exeSql("update emb_session set topSessionOrder = ? where id = ?", new Object[]{System.currentTimeMillis(), sessionId});
    }

    /**
     * 取消置顶
     *
     * @param sessionId 会话标识
     */
    public void resetTopOrder(Long sessionId) {
        /*getDb().exeSql("update emb_session set sessionOrder = ? where id = ?",
                new Object[]{EmbSession.DEFAULT_ORDER, sessionId});*/
        getDb().exeSql("update emb_session set topSessionOrder = ? where id = ?", new Object[]{EmbSession.DEFAULT_NOT_TOP_ORDER, sessionId});
    }

    public List<String> getCpointNoRepeat() {
        List<String> c_pointId = this.getDb().findAllBySql(String.class, "select channelpointid from emb_session group by channelpointid", null);
        return c_pointId;
    }

    public boolean getListByCpointId(String s) {
        //通过channelpointid这个字段来取值，如果能够取超过两个值，那就是已经绑定的用户了
        List<EmbSession> list = this.getDb().findAllBySql(EmbSession.class, "select * from emb_session where channelpointid = '" + s + "'", null);
        if (list.size() >= 2)
            return true;
        else
            return false;
    }

    public EmbSession getSessionByHumanId(Long id) {
        //通过用户id获取到session对话
        return getDb().findBySql(EmbSession.class, "select * from emb_session where humanId =" + id, null);
    }

    /**
     * 给定一个系统Id，创建系统会话
     *
     * @param sessionId
     */
    public void createSystemSession(Long sessionId) {
        LoginService ls = ServiceFactory.getService(LoginService.class.getName());
        EmbSession session = new EmbSession();
        session.setId(sessionId);
        session.setHumanname("系统通知");
        session.setNicknamebin("系统通知");
        session.setOwnerId(ls.getLoginName());
        session.setLastupdate(20000000000L);
        saveOrUpdate(session);
    }

    public List<EmbSession> getGroupList(String ownerId, String searchToken, PageInfo pageInfo) {
        String where = "ownerId='" + ownerId + "'" + " and isGroup = 1";
        if (!TextUtils.isEmpty(searchToken)) {
            where += " and nicknamebin like '%" + searchToken + "%'";
        }
        return getDb().findAllByWhere(EmbSession.class, where, "humanId", pageInfo);
    }
}
