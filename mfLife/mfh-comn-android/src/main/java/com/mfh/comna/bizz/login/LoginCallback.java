package com.mfh.comna.bizz.login;

import com.mfh.comna.bizz.login.entity.UserMixInfo;

/**
 * Created by Administrator on 14-5-6.
 */
public interface LoginCallback {
    /**
     * 登录成功后的回调函数
     * @param user
     */
    void loginSuccess(UserMixInfo user);

    /**
     * 登录失败
     * @param errMsg 错误信息
     * */
    void loginFailed(String errMsg);
}
