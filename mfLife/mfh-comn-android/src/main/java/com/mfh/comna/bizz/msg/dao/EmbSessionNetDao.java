package com.mfh.comna.bizz.msg.dao;

import android.content.Intent;

import com.mfh.comna.bizz.msg.MsgConstants;
import com.mfh.comna.bizz.msg.entity.RSBizSessionWithMsgParam;
import com.mfh.comna.comn.database.dao.BaseNetDao;
import com.mfh.comna.comn.database.dao.DaoUrl;

/**
 * 基于网络访问session会话
 * Created by Administrator on 14-5-7.
 */
public class EmbSessionNetDao extends BaseNetDao<RSBizSessionWithMsgParam, Long> {
    private boolean downLoading = false;//正在下载标志
    private int errorCount = 0;//连续出错次数

   // LoginService ls = ServiceFactory.getService(LoginService.class.getName());

    @Override
    protected void initUrlInfo(DaoUrl daoUrl) {
        //两个接口模式，其中第一个需要传递subdisid，第二个需要传递guid
        //setMsgMode(MsgConstants.MSG_MODE_TAX);
        daoUrl.setListUrl("/biz/msg/getSessionList");
    }

    public void setMsgMode(int msgMode) {
        if (msgMode == MsgConstants.MSG_MODE_APART)
            daoUrl.setListUrl("/biz/msg/getSessionList ");
        else if (msgMode == MsgConstants.MSG_MODE_TAX)
            daoUrl.setListUrl("/mobile/msg/getSessionListBySubdisid");
        else
            daoUrl.setListUrl("/biz/msg/getSessionList");
    }

    @Override
    protected Class<Long> initPkClass() {
        return Long.class;
    }

    public boolean isDownLoading() {
        return downLoading;
    }

    /**
     * 成功后复原下载状态
     */
    public void resetDownLoading() {
        this.downLoading = false;
        //通知结束
        //Intent intent = new Intent(MsgConstants.ACTION_DOWNLOAD_FINISH);
        //this.getContext().sendBroadcast(intent);
    }

    /**
     * 复原出错次数
     */
    protected void resetErrorCount() {
        errorCount = 0;
    }

    /**
     * 出错时复原下载状态
     */
    protected void resetDownLoadingOnError() {
        resetDownLoading();
        errorCount ++;
        if (errorCount <= 2) {//5次失败后停止轮询
            try {
                Intent intent = new Intent(MsgConstants.ACTION_MSG_SERVERERROR);
                this.getContext().sendBroadcast(intent);
            }
            catch (Throwable ex) {
                ;
            }
        }
    }

    public void restDownLoadingWithNoIntent() {
        this.downLoading = false;
    }


   /* *//**
     * 把json转换成bean
     * @param //json
     * @return
     *//*
    protected EmbSession changeJsonToBean(JSONObject json) {
        try {
            if (json == null)
                return null;
            EmbSession es = new EmbSession();
            String strParamJson = json.getString("param");
            JSONObject mainParam = JSONObject.parseObject(strParamJson);
            if (mainParam != null) {//最后会话可能为空
                strParamJson = mainParam.getString("msgBean");
                JSONObject msgMainJson = JSONObject.parseObject(strParamJson);//消息主体内容
                //JSONObject msgMainJson = json.getJSONObject("param").getJSONObject("param");
                String strMsgJso = msgMainJson.getString("msgBody");
                es.setSessionType(msgMainJson.getInteger("type"));
                es.setMsgInfo(strMsgJso);
            }
            else
                es.setUnReadCount(0);
            //es.setUnReadCount(-1);

            //JSONObject msgJson =  JSONObject.parseObject(strMsgJso);// msgMainJson.getJSONObject("param");//消息本身内容
            //es.setMsgContent(msgJson.getString("content"));
            //es.setMediaType(msgJson.getString("type"));

            es.setId(json.getLongValue("id"));
            es.setMsgType(json.getInteger("type"));
            es.setSubdisId(json.getLong("subdisid"));
            es.setSubdisName(json.getString("subdisname"));
            es.setHumanName(json.getString("humanname"));
            es.setChannelkey(json.getString("channelkey"));
            es.setChannelpointid(json.getString("channelpointid"));
            es.setHumanId(json.getLong("humanid"));
            es.setOwnerId( getContext().getSharedPreferences("login", Activity.MODE_PRIVATE).getString("app.login.name", ""));

            es.setAddrvalsBind(json.getString("addrvalsbind"));
            es.setRemark(json.getString("remark"));
            es.setFormatCreateTime(json.getString("createtime"));
            //es.setMsgTime(NetTaskCallBack.defaultFormat.parse(json.getString("createtime")));
            es.setHeadImageUrl(json.getString("headimageurl"));
            es.setLastUpdate(json.getLong("lastupdate"));
            es.setSpokesMan(json.getString("spokesman"));
            es.setNickname(json.getString("nicknamebin"));
            es.setStatus(json.getInteger("status"));
            es.setCreatedBy(json.getString("createdBy"));
            es.setUpdatedBy(json.getString("updatedBy"));

            String cdTemp = json.getString("createdDate");
            if (cdTemp != null)
                es.setCreatedDate(JsonParser.defaultFormat.parse(cdTemp));
            cdTemp = json.getString("updatedDate");
            if (cdTemp != null)
                es.setUpdatedDate(JsonParser.defaultFormat.parse(cdTemp));

            return es;
        }
        catch(Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }*/

    /*@Override
    protected NetTaskCallBack<EmbSession, QueryRsProcessor<EmbSession>> genQueryCallBack(QueryRsProcessor<EmbSession> callBack) {
        final EmbSessionNetDao that = this;
        return new QueryRsCallBackOfJson<EmbSession>(callBack, this.getPojoClass(), this.getContext()){
            @Override
            protected EmbSession changeJsonToBean(JSONObject json) {
                return that.changeJsonToBean(json);
            }

            @Override
            protected void doSuccessInner(IResponseData rspData) {
                super.doSuccessInner(rspData);
                resetErrorCount();//这句话放在后面可能会把本地运行出现异常也算进去,放在前面则只要网络调用成功即可。
            }

            @Override
            protected void doWhenAnyException(Throwable ex) {
                super.doWhenAnyException(ex);
                resetDownLoadingOnError();//停止下载,以便允许重新开始。
            }

            @Override
            protected void doFailure(Throwable t, String errMsg) {
                super.doFailure(t, errMsg);
                resetDownLoadingOnError();//停止下载,以便允许重新开始。
            }
        };
    }
*/
    @Override
    protected Class<RSBizSessionWithMsgParam> initPojoClass() {
        return RSBizSessionWithMsgParam.class;
    }

}
