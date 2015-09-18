package com.mfh.comna.bizz.msg.logic;

import android.os.Handler;
import android.os.Message;
import com.mfh.comna.bizz.BizApplication;
import com.mfh.comna.comn.logic.IService;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.network.NetStateService;
import com.mfh.comna.utils.DialogUtil;
import org.slf4j.LoggerFactory;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 从服务器端轮询消息会话和消息列表
 * Created by Administrator on 14-5-14.
 */
public class MsgTimer implements IService, NetStateService.NetStateListener {
    private Timer timer = null;
    private TimerTask task;

    private EmbSessionService sessionService;
    private EmbMsgService msgService;
    private static int defaultPeriod = 5;
    private int sessionPeriod = defaultPeriod;//轮询session 5倍周期
    private int sessionPausePeriod = sessionPeriod * 5;//待机时轮询周期加长
    private int msgPeriod = sessionPeriod / 5;//消息轮询周期是会话轮询周期的1/5

    private int sessionCount = 0;//session定时器周期计数
    private int msgCount = 0;//msg定时器周期计数

    private Integer askType = null; //0-轮询会话；1-轮询消息;
    public static int TYPE_SESSION = 0;
    public static int TYPE_MSG = 1;

    private Long sessionId = null;
    private org.slf4j.Logger logger = null;

    /**
     * 切换至会话轮询，并使用默认周期
     */
    public void changeToSession() {
        subSessionPeroid();
        this.askType = 0;
    }

    /**
     * 切换至会话轮询
     */
    public void changeToSessionDirect() {
        this.askType = 0;
    }

    /**
     * 切换至会话内消息轮询
     */
    public void changeToMsg() {
        subSessionPeroid();
        this.askType = 1;
    }

    /**
     * 现在是否在后台运行
     * @return
     */
    public boolean isRunAtBack() {
        return sessionPeriod == sessionPausePeriod;
    }

    /**
     * 待机时增加session循环周期
     */
    public void addSessionPeroid() {
        sessionPeriod = sessionPausePeriod;
        //msgPeriod = sessionPeriod / 5;
    }

    /**
     * 减少session循环周期
     */
    public void subSessionPeroid() {
        sessionPeriod = defaultPeriod;
       // msgPeriod = sessionPeriod / 5;
    }

    /**
     * 取消该类型轮询
     * @param oldType 老的类型
     */
    public void changeNothing(int oldType) {
        if (askType != null && askType == oldType)
            this.askType = null;
    }

    /**
     * 是否为轮询消息模式
     * @return
     */
    public boolean isMsgType() {
        return (askType != null && askType == TYPE_MSG);
    }

    public boolean isSessionType() {
        return (askType != null && askType == TYPE_SESSION);
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * 开始消息轮询
     */
    public void start() {
        synchronized (this) {
            if (timer == null) {//防止重复调用
                task = new TimerTask() {
                    @Override
                    public void run() {
                        if (askType == null)
                            return;
                        //防止相互引用
                        if (sessionService == null)
                            sessionService = ServiceFactory.getService(EmbSessionService.class);
                        if (msgService == null)
                            msgService = ServiceFactory.getService(EmbMsgService.class);

                        if (askType == TYPE_SESSION) {//代表会话轮询
                            if (sessionCount % sessionPeriod == 0) {
                                sessionCount = 1;
                            }
                            else {
                                sessionCount++;
                                return;
                            }
                        }
                        else {//代表消息轮询
                            if (sessionId == null)
                                return;
                            if (msgCount % msgPeriod == 0) {
                                msgCount = 1;
                            }
                            else {
                                msgCount++;
                                return;
                            }
                        }
                        //发送消息触发
                        Message message = new Message();
                        handler.sendMessage(message);
                    }
                };
                timer = new Timer();
                timer.schedule(task, 2000, 2000);//2秒后，2秒一次
            }
        }
    }

    /**
     * 停止
     */
    public void stop() {
        synchronized (this) {
            if (timer == null)
                return;
            task.cancel();
            timer.cancel();//终止
            task = null;
            timer = null;
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        this.askType = null;
    }

    /**
     * 构造函数，并且直接启动调度
     */
    public MsgTimer() {
        logger = LoggerFactory.getLogger(this.getClass());
        NetStateService.addNetListener(this);
    }

    /**
     * handler处理类,依附于主线程执行;handleMessage在主线程中执行；
     * 因为queryFromNet中本身内部又采用异步处理，需要其运行的线程有looper。
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (askType == TYPE_SESSION) {//代表会话轮询
                    sessionService.queryFromNet();
                }
                else if (sessionId != null) {//代表消息轮询
                    msgService.queryFromNet(sessionId);
                }
            }
            catch (Throwable ex) {
                DialogUtil.showHint(BizApplication.getAppContext(), ex.getMessage());
            }
        }
    };

    @Override
    public void onConnected() {
        start();
    }

    @Override
    public void onDisConnected() {
        stop();//终止
    }
}
