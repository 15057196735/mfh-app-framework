package com.mfh.comna.bizz;

import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import com.mfh.comna.api.Constants;
import com.mfh.comna.api.helper.SensoroHelper;
import com.mfh.comna.api.utils.MLog;
import com.mfh.comna.comn.ComnApplication;
import com.sensoro.beacon.kit.Beacon;
import com.sensoro.beacon.kit.BeaconManagerListener;
import com.sensoro.cloud.SensoroManager;

import org.kymjs.kjframe.bitmap.BitmapConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 业务层应用类，根据实际需求继承!
 * 
 * @author zhangyz created on 2013-5-25
 * @since Framework 1.0
 */
public class BizApplication extends ComnApplication {
    private static final String TAG = BizApplication.class.getSimpleName();

//    private static AssetManager am;
    private static Map<String, Object> contextHolder = new HashMap<>();

    /**云子·摇一摇*/
    /*
	 * Sensoro Manager
	 */
    public SensoroManager sensoroManager;
    /*
	 * Beacon Manager lister,use it to listen the appearence, disappearence and
	 * updating of the beacons.
	 */
    private BeaconManagerListener beaconManagerListener;
    /*
    * store beacons in onUpdateBeacon
    */
    public CopyOnWriteArrayList<Beacon> beacons;
    private String beaconFilter;

    @Override
    public void onCreate() {
        super.onCreate();

        // Bitmap缓存地址
        BitmapConfig.CACHEPATH = AppConfig.DEFAULT_KJBITMAP_CACHEPATH;

//        //Log
//        MLog.DEBUG = true;//AppConfig.DEBUG;

        //CrashHandler crashHandler = CrashHandler.getInstance();
        //crashHandler.init(context);
        /*Intent intent = new Intent(this, ErrorService.class);
        startService(intent);*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            initSensoro();
        }
    }

    @Override
    public void onTerminate() {
        stopSensoro();
        super.onTerminate();
    }

    /**
     * 获取缓存的对象
     * @param objName
     * @param objClass
     * @return
     * @author zhangyz created on 2013-6-12
     */
    @SuppressWarnings("unchecked")
    public static <T> T getObject(String objName, Class<T> objClass) {
        return (T)contextHolder.get(objName);
    }

    /**
     * 保存缓存对象
     * @param objName
     * @param value
     * @author zhangyz created on 2013-6-12
     */
    public static void putObject(String objName, Object value) {
        contextHolder.put(objName, value);
    }

    /**
     * initialize Sensoro SDK
     * */
    private void initSensoro() {
        sensoroManager = SensoroManager.getInstance(getApplicationContext());
        /**
         * Enable cloud service (upload sensor data, including battery status, UMM, etc.)。Without setup, it keeps in closed status as default.
         **/
        sensoroManager.setCloudServiceEnable(false);
        /**
         * 设置云子防蹭用密钥 (如果没有可以不设置)
         **/
//        sensoroManager.addBroadcastKey("7b4b5ff594fdaf8f9fc7f2b494e400016f461205");

        beacons = new CopyOnWriteArrayList<Beacon>();

        initSensoroListener();
        // Nat_20150421 如果在这里启动sensoro Service, 会报错。
//        startSensoroService();
    }

    /*
	 * Start sensoro service.
	 * SDK 是基于蓝牙 4.0 的服务，启动前请先检查蓝牙是否开启，否则 SDK 无法正常工作。
	 */
    public void startSensoroService() {
        // set a tBeaconManagerListener.
        sensoroManager.setBeaconManagerListener(beaconManagerListener);
        /**
         * 检查蓝牙是否开启
         **/
        if (sensoroManager.isBluetoothEnabled()){
            /**
             * 启动 SDK 服务
             **/
            try {
                sensoroManager.startService();
            } catch (Exception e) {// 捕获异常信息
                MLog.d("startSensoroService failed, " + e.toString());
//            e.printStackTrace();
            }
        }
    }

    /**
     * stop Sensoro SDK
     * */
    private void stopSensoro(){
        if (sensoroManager != null) {
            sensoroManager.stopService();
        }

        if(beacons != null){
            beacons.clear();
        }
    }

    /**
     * 传感器信息更新频率为 1 秒；发现一个新的传感器后，如果在 8 秒内没有再次扫描到这个设备，则会回调传感器消失。
     * serialNumber	SN，设备唯一标识
     major	iBeacon协议中的 major 信息
     minor	iBeacon协议中的 minor 信息
     proximityUUID	iBeacon协议中的 UUID 信息
     rssi	信号强度
     accuracy	距离（米）
     proximity	范围（很远，附近，很近，未知）
     temperature	芯片温度
     light	光线
     movingState	移动状态
     accelerometerCount	移动计数器
     batteryLevel	电池电量
     hardwareModelName	硬件版本
     firmwareVersion	固件版本
     measuredPower	1 米处测量 rssi
     transmitPower	广播功率
     advertisingInterval	广播间隔
     * */
    private void initSensoroListener() {
        beaconManagerListener = new BeaconManagerListener() {

            @Override
            public void onUpdateBeacon(final ArrayList<Beacon> arg0) {
//                MLog.d("onUpdateBeacon" + arg0.toString());
                StringBuilder sb = new StringBuilder();
                sb.append("Beacons Update:\n");
                int i = 0;

				//Add the update beacons into the grid.
                for (Beacon beacon : arg0) {
                    if (beacons.contains(beacon)) {
                        continue;
                    }

					//filter
                    if (TextUtils.isEmpty(beaconFilter)) {
                        beacons.add(beacon);
                    } else {
                        String matchString = String.format(SensoroHelper.MATCH_FORMAT,
                                beacon.getSerialNumber(), beacon.getMajor(), beacon.getMinor());
                        if (matchString.contains(beaconFilter)) {
                            beacons.add(beacon);
                        }
                    }

                    sb.append(String.format("%d  %s\n", i++, beacon.toString()));
                }
//                MLog.d("onUpdateBeacon" + sb.toString());

                //TODO
                notifyBeaconsUpdate();
            }

            @Override
            public void onNewBeacon(Beacon arg0) {
                final String beanconInfo = arg0.toString();
//                MLog.d("onNewBeacon" + beanconInfo);
				//A new beacon appears.
//                String key = getKey(arg0);
//                boolean state = sharedPreferences.getBoolean(key, false);
//                if (state) {
//					//show notification
//                    showNotification(arg0, true);
//                }
//
//                runOnUiThread(new Runnable() {
//                    public void run() {
////                        DialogUtil.showHint(String.format("New Beacons<%s>", beanconInfo));
//                    }
//                });
            }

            @Override
            public void onGoneBeacon(Beacon arg0) {
                final String beanconInfo = arg0.toString();
//                MLog.d("onGoneBeacon> " + beanconInfo);
				/*
				 * A beacon disappears.
				 */
                if (beacons.contains(arg0)) {
                    beacons.remove(arg0);
                }
                notifyBeaconsUpdate();

//                String key = getKey(arg0);
//                boolean state = sharedPreferences.getBoolean(key, false);
//                if (state) {
//					//show notification
//                    showNotification(arg0, false);
//                }

            }
        };
    }

    public String getKey(Beacon beacon) {
        if (beacon == null) {
            return null;
        }
        String key = beacon.getProximityUUID() + beacon.getMajor() + beacon.getMinor() + beacon.getSerialNumber();
        return key;
    }

    /**
     * 附近是否有云子
     * */
    public boolean existBeacons(){
        if(beacons != null && beacons.size() > 0){
            return true;
        }
        return false;
    }

    /**
     * 云子状态发生改变，通知改变摇一摇周边选项显示/隐藏
     * */
    private void notifyBeaconsUpdate(){
        Intent intent = new Intent(Constants.ACTION_BEACONS_UPDATE);
        intent.putExtra(Constants.KEY_BEACONS_EXIST, existBeacons());
        sendBroadcast(intent);

//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        for (OnBeaconChangeListener listener : beaconListeners) {
//                            if (listener == null) {
//                                continue;
//                            }
//                            listener.onBeaconChange(arg0);
//                        }
//                    }
//                });
    }

}
