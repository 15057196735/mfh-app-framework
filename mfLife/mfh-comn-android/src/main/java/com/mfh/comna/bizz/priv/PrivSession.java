package com.mfh.comna.bizz.priv;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import com.mfh.comn.priv.bean.IUser;
import com.mfh.comn.priv.bean.TUser;
import com.mfh.comn.utils.UuidUtil;
import com.mfh.comna.bizz.BizApplication;
import com.mfh.comna.bizz.priv.dao.UserDao;

/**
 * 用于管理登陆用户的会话
 * 
 * @author zhangyz created on 2013-6-12
 * @since Framework 1.0
 */
public class PrivSession {
    private Random random = null;
    //private static final ThreadLocal<PrivSession> threadPrivSession = new ThreadLocal<PrivSession>();
    private String privSessionId = null;//在通过程序登录时，保存本privSession所对应的sessionId
    protected Date lastAccessTime = new Date();//最近一次访问的时间 add by zhulm 20101118
    protected IUser currentUser = null;//当前登录用户的信息;    
    private UserDao userDao = new UserDao();
    private static final String PRIVKEY = "privSesion";
    
    /**
     * 获取权限会话对象
     * @return
     * @author zhangyz created on 2013-6-12
     */
    public static PrivSession getPrivSession() {
        PrivSession pv = BizApplication.getObject(PRIVKEY, PrivSession.class);
        //PrivSession pv = threadPrivSession.get();
        if (pv == null || pv.currentUser == null)
            throw new RuntimeException("您还没有登录！");
        return pv;
    }
    
    /**
     * 使用默认的系统管理员登录
     * @return
     * @author zhangyz created on 2013-6-12
     */
    public static PrivSession getDefaultSession() {
        PrivSession pv = initPrivSession();
        int code = pv.login(TUser.USER_SYS, "123456", null);
        if (code == -2){
            pv.genSysUser();
        }
        pv.loginWithException(TUser.USER_SYS, "123456", null);
        return pv;
    }
    
    /**
     * 初始化权限会话
     * @return
     * @author zhangyz created on 2013-6-12
     */
    public static PrivSession initPrivSession() {
        PrivSession pv = BizApplication.getObject(PRIVKEY, PrivSession.class);
        if (pv == null) {
            pv = new PrivSession();
            setPrivSession(pv);
        }
        return pv;
    }
    
    /**
     * 设置权限会话对象
     * @param privSession
     * @author zhangyz created on 2013-6-12
     */
    public static void setPrivSession(PrivSession privSession) {
        BizApplication.putObject(PRIVKEY, privSession);
        //threadPrivSession.set(privSession);
    }
    
    /**
     * 获取当前登录的用户;
     * @param currentUser IUser;
     */
    public IUser getCurrentUser() {
        return currentUser;
    }
    
    public void setCurrentUser(IUser currentUser) {
        this.currentUser = currentUser;
    }
    
    /**
     * 执行用户登陆，若失败抛出异常，若成功设置当前登陆用户。
     * @param strLoginName 登陆名
     * @param strPwd 登陆密码
     * @param params 登陆参数
     */
    public void loginWithException (String strLoginName, String strPwd, Map<?, ?> params) {
        int code = login(strLoginName, strPwd, params);
        if (code == -2)
            throw new RuntimeException("没有此用户!");
        else if (code == -3)
            throw new RuntimeException("该用户被禁用!");
        else if (code == -1)
            throw new RuntimeException("用户名或密码不对!");
    }
    
    /**
     * @param strLoginName 登录名(非用户标识);
     * @param strPwd 密码;
     * @param params 额外的参数,可以为空
     * @return 0 成功; -1 密码错误; -2 没有此用户; -3:该用户状态被禁用。
     * @throws exception 其他异常,数据库连接错误等;
     */
    public int login(String strLoginName, String strPwd, Map<?, ?> params) {
        currentUser = null;
        try{
            currentUser = userDao.getUserByLoginname(strLoginName);
            if (currentUser == null)
                return -2;
            if (currentUser.getPassword().equals(strPwd)){
                if (currentUser.getState() == 0)
                    return -3;
                this.setCurrentUser(currentUser);
                setPrivSessionId(this.genSessionId());
                return 0;
            }
            return -1;
        }
        catch(Exception ex){
            throw new RuntimeException("登录时验证出错:" + ex.getMessage(), ex);
        }
    }
    
    /**
     * 生成系统管理员
     * 
     * @author zhangyz created on 2013-6-12
     */
    public TUser genSysUser() {
        TUser user = new TUser();
        user.setLoginname("sys");
        user.setPassword("123456");
        user.setType(TUser.USERTYPE_SYS);
        user.setFirstname("系统管理员");
        user.setId(UuidUtil.getUuid());
        this.userDao.save(user);
        return user;
    }
    
    /**
     * 获取当前登录的用户名;
     * @return IUser;
     */
    public String getCurrentUserId(){
        if(currentUser == null)
            return null;
        else
            return currentUser.getId();
    }
    
    /**
     * 获取会话session编号
     * @return
     * @author zhangyz created on 2013-6-12
     */
    private String genSessionId() {
        if (random == null)
            random = new Random();
        String sessionId = (Integer.toString(random.nextInt()) + '-' + Long.toString((new Date()).getTime()));
        return sessionId;
    }
    
    public void setPrivSessionId(String privSessionId) {
        this.privSessionId = privSessionId;
    }

    public String getPrivSessionId() {
        return privSessionId;
    }
}
