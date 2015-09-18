package com.mfh.comna.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.mfh.comna.bizz.BizApplication;
import com.mfh.comna.comn.logic.IService;
import com.mfh.comna.comn.logic.ServiceFactory;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 网络状态服务监听类
 * Created by Administrator on 14-6-12.
 */
public class NetStateService implements IService {
    private static String serviceName = NetStateService.class.getName();
    private boolean connected = true;

    /**
     * 网络事件回调接口
     */
    public interface NetStateListener {
        /**
         * 接通时执行
         */
        void onConnected();

        /**
         * 断开时执行
         */
        void onDisConnected();
    }

    private Set<NetStateListener> listeners = new HashSet<NetStateListener>();

    /**
     * 增加一个网络监听事件
     * @param listener
     * @param connectivityType 哪种网络类型
     */
    public static void addNetListener(NetStateListener listener, int... connectivityType) {
        NetStateService ns = ServiceFactory.getService(serviceName);
        ns.addListener(listener, connectivityType);
    }

    /**
     * 增加一个监听事件
     * @param listener
     * @param connectivityType 对哪种网络感兴趣，备用
     */
    public void addListener(NetStateListener listener, int... connectivityType) {
        synchronized (listeners) {
            listeners.add(listener);
        }
        if (connected) {
            listener.onConnected();
        }
        else
            listener.onDisConnected();
    }

    /**
     * 移除一个监听事件
     * @param listener
     */
    public void removeListener(NetStateListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    /**
     * 直接注册
     */
    public static void registerReceiver() {
        NetStateService ns = ServiceFactory.getService(serviceName);
        ns.registerNetStateReceiver();
    }

    /**
     * 注册网络状态事件接收器
     */
    public void registerNetStateReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo workInfo = connManager.getActiveNetworkInfo();
                NetworkInfo.State state = null;
                if (workInfo != null)
                    state = workInfo.getState();
                //ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI

                if (state != null && NetworkInfo.State.CONNECTED == state) {
                    connected = true;
                    synchronized (listeners) {
                        Iterator<NetStateListener> iter = listeners.iterator();
                        while (iter.hasNext()) {
                            iter.next().onConnected();
                        }
                    }
                }
                else {
                    connected = true;
                    synchronized (listeners) {
                        Iterator<NetStateListener> iter = listeners.iterator();
                        while (iter.hasNext()) {
                            iter.next().onDisConnected();
                        }
                    }
                }
            }
        };
        BizApplication.getAppContext().registerReceiver(networkStateReceiver, filter);
    }
}
