package com.mfh.comna.bizz.msg.dao;

import android.widget.Toast;

import com.mfh.comna.bizz.msg.MsgConstants;
import com.mfh.comna.bizz.msg.entity.EmbMsg;
import com.mfh.comn.bean.PageInfo;
import com.mfh.comn.bean.Pair;
import com.mfh.comn.bean.msg.MsgBean;
import com.mfh.comn.utils.DateUtil;
import com.mfh.comna.comn.database.dao.BaseDbDao;

import net.tsz.afinal.db.sqlite.SqlBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 消息dao类
 * Created by Administrator on 14-5-6.
 */
public class EmbMsgDao extends BaseDbDao<EmbMsg, String> {
    private static final String TABLE_NAME = "emb_msg";
    private static final String TABLE_NAME_CH = "消息表";

    //CRUD
    private static final String SQL_UPDATE = "update " + TABLE_NAME;
    private static final String SQL_DELETE = "delete from " + TABLE_NAME;
    private static final String SQL_QUERY_ALL = "select * from " + TABLE_NAME;

    private static final SimpleDateFormat SDF_INNER_DATAFORMAT = new SimpleDateFormat(DateUtil.INNER_DATAFORMAT);


    @Override
    protected Pair<String, String> initTableChName() {
        return new Pair<String, String>(TABLE_NAME_CH, TABLE_NAME);
    }

    @Override
    protected Class<EmbMsg> initPojoClass() {
        return EmbMsg.class;
    }

    @Override
    protected Class<String> initPkClass() {
        return String.class;
    }

    /**
     * 设置该消息已读
     * @param msgId
     */
    public void setHaveRead(Long msgId) {
        getDb().exeSql(SQL_UPDATE + " set isRead = 1 where id = ?", new Object[]{msgId});
    }

    /**
     * 设置指定会话中所有消息已读
     * @param sessionId
     */
    public void setHaveReadBySessionId(Long sessionId) {
        getDb().exeSql(SQL_UPDATE + " set isRead = 1 where sessionId = ?", new Object[]{sessionId});
    }

    /**
     * 设置该消息未读
     * @param msgId
     */
    public void resetHaveRead(Long msgId) {
        getDb().exeSql(SQL_UPDATE + " set isRead = 0 where id = ?", new Object[]{msgId});
    }

    /**
     * 清理当前用户的所有消息
     * @param ownerId
     */
    public void clearMsgs(String ownerId) {
        try {
            getDb().exeSql(SQL_DELETE + " where ownerId = ?", new Object[]{ownerId});
        }
        catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * 生成where语句
     * @param sessionId 会话Id
     * @param searchToken 查询token
     * @return
     */
    private String genWhereSql(Long sessionId, String searchToken) {
        String sql = "sessionId = " + sessionId;
        if (searchToken != null && searchToken.length() > 0) {
            searchToken = "%" + searchToken + "%";
            sql += " and (msgInfo like '" + searchToken
                    + "' or humanName like '" + searchToken + "')";
        }
        return sql;
    }

    /**
     * 查询指定session下的消息类比，按照逆序
     * @param sessionId
     * @param pageInfo
     * @return
     */
    public List<EmbMsg> queryMsgsBySessionId(Long sessionId, String searchToken, PageInfo pageInfo) {
        String sql = genWhereSql(sessionId, searchToken);
        return getDb().findAllByWhere(EmbMsg.class, sql, "createdDate asc", pageInfo);//"id desc"
    }

    /**
     * 查询消息总数
     * @param sessionId
     * @param searchToken
     * @return
     */
    public Integer queryMsgCountBySessionId(Long sessionId, String searchToken) {
        String strWhere = genWhereSql(sessionId, searchToken);
        String countSql = SqlBuilder.getSelectCountSQLByWhere(EmbMsg.class, strWhere);
        return getDb().findTotalCount(countSql, null);
    }

    public Date queryTheOldTime(Long sessionId) {
       // String createDate = ().("select min(formatCreateTime) from emb_msg where sessionId=5771");
        String sql = SQL_QUERY_ALL + " where sessionId=" + sessionId;
        EmbMsg msg = getDb().findBySql(EmbMsg.class, sql, null);
        try {
            if (msg.getCreatedDate() != null)
                return msg.getCreatedDate();
            else
                return SDF_INNER_DATAFORMAT.parse("0000-00-00 00:00:00");
        } catch (ParseException e) {
            Toast.makeText(getContext(), "EmbMsgDao_queryTheOldTime", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据传过来的字符串，创建系统消息
     * @param nDate
     */
    public void createSystemMsg(String nDate) {
        EmbMsg msg = new EmbMsg();
        msg.setSessionid(MsgConstants.SystemSessionId);
        MsgBean msgBean = new MsgBean("{\"content\":" + "\"" + nDate + "\"" + ",\"type\":\"text\"}");
        msg.setMsgInfo("{\"content\":" + "\"" + nDate + "\"" + ",\"type\":\"text\"}");
        msg.setCreatedDate(new Date());
        //msg.setFormatCreateTime(SDF_INNER_DATAFORMAT.format(new Date()));
        //msg.setMsgBean(msgBean);
        saveOrUpdate(msg);
    }
}
