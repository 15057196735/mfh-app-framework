package com.mfh.comna.androidService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.mfh.comna.comn.logic.BaseService;
import com.mfh.comna.comn.logic.IAndroidService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2014/11/10.
 * app统一的android端的service
 */
public class ComnaAndroidService extends Service{

    private Map<BaseService, Long> serviceLongMap = new HashMap<BaseService, Long>();
    private boolean isRunning = false;
    private long time = 5*60*1000;
    private SharedPreferences sp;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            for (Map.Entry<BaseService, Long> entry : serviceLongMap.entrySet()){
                if (System.currentTimeMillis() - sp.getLong(entry.getKey().getClass().getSimpleName()+ "pre" ,0) >= entry.getValue()){
                    entry.getKey().syncDataFromFrontToEnd(2, null);
                    edit.putLong(entry.getKey().getClass().getSimpleName()+ "pre", System.currentTimeMillis());
                    edit.commit();
                }
            }
            if (isRunning) {
                handler.sendEmptyMessageDelayed(0, time / 4);
            }
        }
    };
    private SharedPreferences.Editor edit;

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        edit = sp.edit();
        isRunning = true;
        handler.sendEmptyMessage(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    public void setServiceLongMap(BaseService service, long time) {
        if (serviceLongMap.containsKey(service))
            return;
        if (time < this.time)
            this.time = time;
        serviceLongMap.put(service,time);
        edit.putLong(service.getClass().getSimpleName() + "delay", time); //延迟时间
        edit.putLong(service.getClass().getSimpleName() + "pre", System.currentTimeMillis());  //记录当前时间
        edit.commit();
        service.syncDataFromFrontToEnd(2, null);
    }

    public class MyBinder extends Binder implements IAndroidService{

        @Override
        public void setService(BaseService service, long time) {
            setServiceLongMap(service,time);
        }
    }
}
