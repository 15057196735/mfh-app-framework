package com.mfh.comna.bizz.login;

import com.mfh.comna.bizz.config.URLConf;
import com.mfh.comna.api.helper.SharedPreferencesHelper;
import com.mfh.comna.api.utils.MLog;
import com.mfh.comna.api.utils.StringUtils;
import com.mfh.comna.bizz.login.logic.MfhLoginService;
import com.mfh.comna.bizz.msg.entity.ImageParam;
import com.mfh.comna.bizz.msg.entity.TextParam;
import com.mfh.comn.bean.msg.FromInfo;
import com.mfh.comn.bean.msg.MsgChanneltypeConst;
import com.mfh.comn.bean.msg.MsgConstant;
import com.mfh.comn.bean.msg.MsgData;
import com.mfh.comn.bean.msg.MsgParameter;
import com.mfh.comn.bean.msg.PhysicalPoint;
import com.mfh.comn.bean.msg.param.RegisterParam;
import com.mfh.comn.bean.msg.param.ResourceParam;
import com.mfh.comna.network.NetFactory;

import net.tsz.afinal.http.AjaxParams;

import java.util.Date;

/**
 * 注册到消息桥,获得请求的参数
 * Created by 李潇阳 on 2014/11/18.
 */
public class MsgBridgeUtil {

    private static final int CHANNEL_ID = 68;

    /**
     * 获得注册或者注销所用的参数
     * @param cpid
     * @param guid
     * @param //eventType
     * @return
     */
    public static MsgParameter getRegisterRequestParam(String cpid, Long guid, int biztype){
        MsgParameter msgParameter = new MsgParameter();

        PhysicalPoint fromPhysicalPoint = new PhysicalPoint();
        fromPhysicalPoint.setCtype(MsgChanneltypeConst.APP);
        fromPhysicalPoint.setCpt(cpid);//

        FromInfo from = new FromInfo();
        from.setGuid(guid);//
        from.setPp(fromPhysicalPoint);
        msgParameter.setFrom(from);

        String exparam = "-1";
        RegisterParam param = new RegisterParam();
        param.setBind(1);
        param.setParam(exparam);

        MsgData msgBean = new MsgData();
        msgBean.setTime(new Date());
        msgBean.setType(MsgConstant.MSG_TECHTYPE_JSON);
        msgBean.setBizType(biztype);//
        msgBean.setBody(param);

        msgParameter.setMsgBean(msgBean);

        return msgParameter;
    }

    /**
     * 注册到消息桥
     */
    public static void registerMsg(){
        String clientId = SharedPreferencesHelper.getPushClientId();
        String guidStr = MfhLoginService.get().getCurrentGuId();

        if(clientId == null || guidStr == null){
            return;
        }

        MsgParameter jsonStr = getRegisterRequestParam(clientId, Long.valueOf(guidStr), MsgConstant.MSG_BIZTYPE_REGISTER);
        AjaxParams params = new AjaxParams();
        String channelId = NetFactory.getChannelId();
        MLog.d(String.format("[POST]registerMsg:channelId = %s", channelId));
        MLog.d(String.format("[POST]registerMsg:PARAM_VALUE_CHANNEL_ID_DEF=%s", String.valueOf(URLConf.PARAM_VALUE_CHANNEL_ID_DEF)));
        if (StringUtils.isEmpty(channelId)){
            params.put(URLConf.PARAM_KEY_CHANNEL_ID, String.valueOf(URLConf.PARAM_VALUE_CHANNEL_ID_DEF));
        }else{
            params.put(URLConf.PARAM_KEY_CHANNEL_ID, NetFactory.getChannelId());
        }
        params.put(URLConf.PARAM_KEY_QUEUE_NAME, URLConf.PARAM_VALUE_QUEUE_NAME_DEF);
        params.put(URLConf.PARAM_KEY_JSONSTR, String.valueOf(jsonStr));

        String registerUrl = URLConf.getUrlForMessage();
        MLog.d(String.format("[POST]registerMsg:%s?%s", registerUrl, params.toString()));
        NetFactory.getHttp().post(registerUrl, params, null);
    }


   /**
     * 发送文本消息所用的参数
     * @param cpid
     * @param //guid
     * @param //content
     * @return
     *//*
    public static String getTextRequestParam(String cpid, Long guid, String content, Long sessionId){
        TextRequest request = new TextRequest();
        request.setContent(content);
        request.setFromGuid(guid);
        PhysicalPoint physicalPoint = new PhysicalPoint(CHANNEL_ID, MsgChanneltypeConst.APP, cpid);
        request.setFromPhysicalPoint(physicalPoint);
        try {
            //return String.valueOf(ConverterToMsgParameter(request, cpid, guid, content, sessionId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    public static String getSendTextParam(String cpid) {
        return null;
    }

    public static MsgParameter ConverterToMsgParameter(String cpid, Long guid, String content, Long sessionId){
        MsgParameter msgParameter = new MsgParameter();

        FromInfo from = new FromInfo();
        from.setGuid(guid);
        PhysicalPoint fromPhysicalPoint = new PhysicalPoint();
        fromPhysicalPoint.setCtype(MsgChanneltypeConst.APP);
        fromPhysicalPoint.setCpt(cpid);
        from.setPp(fromPhysicalPoint);
        msgParameter.setFrom(from);

        MsgData msgBean = new MsgData();
        msgBean.setBizType(MsgConstant.MSG_BIZTYPE_CS);//
        msgBean.setType(MsgConstant.MSG_TECHTYPE_TEXT);
        msgBean.setTime(new Date());
        TextParam textParam = new TextParam(content);
        msgBean.setBody(textParam);
        msgParameter.setMsgBean(msgBean);

        msgParameter.setSessionId(sessionId);

		/*
		if(MsgConstant.MSG_BIZTYPE_REGISTER == request.getBiztype()){
			String exparam = "-1";
			//获取关注时二维码参数
			if(!StringUtils.isEmpty(((TextRequest) request).getContent())){
				exparam = ((TextRequest) request).getContent();
			}
			RegisterParam param = new RegisterParam();
			param.setBind(1);
			param.setParam(exparam);
			msgBean.setTechType(MsgConstant.MSG_TECHTYPE_JSON);
			msgBean.setMsgBody(param);
		}else if(MsgConstant.MSG_BIZTYPE_UNREGISTER == request.getBiztype()){
			msgBean.setTechType(MsgConstant.MSG_TECHTYPE_TEXT);

			TextParam textParam = new TextParam();
			textParam.setContent("取消关注");
			msgBean.setMsgBody(textParam);
		}else{
			msgBean.setTechType(MsgConstant.MSG_TECHTYPE_TEXT);
			TextParam textParam = new TextParam();
			textParam.setContent(((TextRequest) request).getContent());
			msgBean.setMsgBody(textParam);
		}*/

        return msgParameter;
    }

    public static MsgParameter ConverterToMsgParameterForImage(String cpid, Long guid, Long sessionId, ImageParam wxParam) {
        MsgParameter msgParameter = new MsgParameter();

        FromInfo from = new FromInfo();
        from.setGuid(guid);

        PhysicalPoint fromPhysicalPoint = new PhysicalPoint();
        fromPhysicalPoint.setCtype(MsgChanneltypeConst.APP);
        PhysicalPoint physicalPoint = new PhysicalPoint(CHANNEL_ID, MsgChanneltypeConst.APP, cpid);
        fromPhysicalPoint.setCpt(physicalPoint.getCpt());
        //fromPhysicalPoint.setChannelpointid(request.getFromPhysicalPoint().getChannelpointid());
        //fromPhysicalPoint.setChannelid(channelid);
        from.setPp(fromPhysicalPoint);

        msgParameter.setFrom(from);

        msgParameter.setSessionId(sessionId);

        //msgParameter.fillDestInfo(request.getDestinfo());
/*        DestInfo destInfo = new DestInfo(sessionId);
        msgParameter.fillDestInfo(destInfo);*/

        MsgData msgBean = new MsgData();
        msgBean.setBizType(MsgConstant.MSG_BIZTYPE_CS);
        msgBean.setType(MsgConstant.MSG_TECHTYPE_IMAGE);
        msgBean.setTime(new Date());

        //TextParam textParam = new TextParam(content);
//        ResourceParam resourceParam = new ResourceParam(imageId);
        msgBean.setBody(wxParam);

        msgParameter.setMsgBean(msgBean);

        return msgParameter;
    }

    public static MsgParameter ConverterToMsgParameterForResource(String cpid, Long guid, Long sessionId, Integer imageId) {
        MsgParameter msgParameter = new MsgParameter();
//        Log.d("Nat: ConverterToMsgParameterForImage 1", msgParameter.toString());
        FromInfo from = new FromInfo();
        from.setGuid(guid);

        PhysicalPoint fromPhysicalPoint = new PhysicalPoint();
        fromPhysicalPoint.setCtype(MsgChanneltypeConst.APP);
        PhysicalPoint physicalPoint = new PhysicalPoint(CHANNEL_ID, MsgChanneltypeConst.APP, cpid);
        fromPhysicalPoint.setCpt(physicalPoint.getCpt());
        //fromPhysicalPoint.setChannelpointid(request.getFromPhysicalPoint().getChannelpointid());
        //fromPhysicalPoint.setChannelid(channelid);
        from.setPp(fromPhysicalPoint);

        msgParameter.setFrom(from);

        msgParameter.setSessionId(sessionId);

        //msgParameter.fillDestInfo(request.getDestinfo());
/*        DestInfo destInfo = new DestInfo(sessionId);
        msgParameter.fillDestInfo(destInfo);*/

        MsgData msgBean = new MsgData();
        msgBean.setBizType(MsgConstant.MSG_BIZTYPE_CS);
        msgBean.setType(MsgConstant.MSG_TECHTYPE_IMAGE);
        msgBean.setTime(new Date());

        //TextParam textParam = new TextParam(content);
        ResourceParam resourceParam = new ResourceParam(imageId);
        msgBean.setBody(resourceParam);


		/*MsgBean msgBean = new MsgBean();
		msgBean.setCreateTime(new Date());

		msgBean.setBizType(request.getBiztype());

		if(MsgConstant.MSG_BIZTYPE_REGISTER == request.getBiztype()){
			String exparam = "-1";
			//获取关注时二维码参数
			if(!StringUtils.isEmpty(((TextRequest) request).getContent())){
				exparam = ((TextRequest) request).getContent();
			}
			RegisterParam param = new RegisterParam();
			param.setBind(1);
			param.setParam(exparam);

			msgBean.setTechType(MsgConstant.MSG_TECHTYPE_JSON);

			msgBean.setMsgBody(param);

		}else if(MsgConstant.MSG_BIZTYPE_UNREGISTER == request.getBiztype()){

			msgBean.setTechType(MsgConstant.MSG_TECHTYPE_TEXT);

			TextParam textParam = new TextParam();
			textParam.setContent("取消关注");
			msgBean.setMsgBody(textParam);

		}else{

			msgBean.setTechType(MsgConstant.MSG_TECHTYPE_TEXT);

			TextParam textParam = new TextParam();
			textParam.setContent(((TextRequest) request).getContent());
			msgBean.setMsgBody(textParam);

		}*/

        msgParameter.setMsgBean(msgBean);
        return msgParameter;
    }
}
