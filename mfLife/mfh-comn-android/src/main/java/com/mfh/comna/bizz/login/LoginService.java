package com.mfh.comna.bizz.login;

import com.mfh.comna.comn.logic.IService;

/**
 * 登录服务接口
 * Created by Administrator on 14-5-5.
 */
public interface LoginService extends IService {
    String CLIENTSESSION = "JSESSIONID";//ShiroHttpSession.DEFAULT_SESSION_ID_NAME
    String OPERATORID = "operatorId";
    /**
     * 执行登录。所用用户名和密码自己存储在本地存储中。
     * @return 返回会话Id
     */
    String doLogin();

    /**
     * 获取当前登录用户
     * @return
     */
    String getLoginName();

    /**
     * 获取当前消息通讯号
     * @return
     */
    String getCurrentGuId();

    /**
     * 通讯标识号
     * @return
     */
    String getCpId();

    /**
     * 获取用户号
     * @return
     */
    Long getUserId();

    /**
     * 获取当前会话
     * @return
     */
    String getCurrentSessionId();

    /**
     * 获取功能列表
     * @return
     */
    String getModuleNames();

    String  getSubdisNames();
}
