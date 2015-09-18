package com.mfh.comna.utils.nfc;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.view.View;
import android.widget.EditText;

/**
 * Nfc的服务类，实现对NfcActivity中广播消息的接收，用于获取IC卡的Token
 *
 * @author yxm
 * @version 1.0
 */
public class NfcUtil extends Service {
    private String Token;//Intent对象经处理后获取的特定标记
    private ISetNfcCardNum nfcListen;//接口类的对象
    private static NfcUtil service = null;

    /**
     * 构造方法
     */
    public NfcUtil() {
        service = this;
    }

    /**
     * 获取NfcService自身的方法
     */
    public static NfcUtil getService() {
        if (service == null)
            throw new RuntimeException("null");
        return service;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 注册监听器
     *
     * @param listen 接口类的对象
     */
    public void setNfcListener(ISetNfcCardNum listen) {
        this.nfcListen = listen;
    }

    /**
     * 移除监听器
     */
    public void removeListener() {
        this.nfcListen = null;
    }

    /**
     * 服务被开启时调用的方法
     *
     * @param intent  用于启动该service的intent
     * @param flags   启动操作选项，通常为0
     * @param startId 标识此次启动请求
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //接收广播消息
        IntentFilter filter = new IntentFilter("com.mfh.nfc.NFC_BROADCAST");
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Token = intent.getStringExtra("Token");

                if (nfcListen != null)
                    nfcListen.setNfcCardNum(Token);
            }
        };
        this.registerReceiver(receiver, filter);//注册广播

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 设置IC卡号的公共方法，如需扩展可重载
     *
     * @param activity 监听NFC事件的组件
     * @param Token    包含Tag的Intent对象经处理后获取的特定标记
     */
    public void setNum(Activity activity, String Token) {
        View rootView = activity.getWindow().getDecorView();
        int viewId = rootView.findFocus().getId();
        EditText edit;
        View v = activity.findViewById(viewId);
        if (v instanceof EditText) {
            edit = (EditText) v;
            edit.setText(Token);
        }
    }
}
