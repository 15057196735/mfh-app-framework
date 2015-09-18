package com.mfh.comna.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.mfh.comna.api.utils.MLog;
import com.mfh.comna.api.utils.SharedPreferencesUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 定位服务
 * Created by Nat.ZZN(bingshanguxue) on 2015/8/8.
 */
public class MfLocationManagerProxy {
    public static final String PREF_NAME = "LOCATION";
    public static final String KEY_LASTUPDATE_LATITUDE = "KEY_LASTUPDATE_LATITUDE";
    public static final String KEY_LASTUPDATE_LONGITUDE = "KEY_LASTUPDATE_LONGITUDE";

    public static final String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    private static final long MIN_INTERVAL = 5 * 1000;
    private static final float MIN_DISTANCE = 5;

    private Context mContext;
    private LocationManager locMgr;

    private static MfLocationManagerProxy instance = null;
    /**
     * MfLocationManagerProxy
     * @return MfLocationManagerProxy
     */
    public static MfLocationManagerProxy getInstance(Context context) {
        if (instance == null){
            instance = new MfLocationManagerProxy(context);
        }
        return instance;
    }

    public MfLocationManagerProxy(Context context) {
        this.mContext = context;


        //创建LocationManager实例，指向定位服务
        this.locMgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void requestLocationData(long interval, float distance, LocationListener listener){
        //获取缓存中的位置信息
        Location location = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
//            Log.d("Nat: LastKnownLocation", String.format("Latitude:%f, longigude:%f", (location.getLatitude() * 1E6), (location.getLongitude() * 1E6)));
            MLog.d(String.format("LastKnownLocation: Latitude:%f, longigude:%f", location.getLatitude(), location.getLongitude()));
        }

        //GPS定位，在室内反应迟缓，比较耗时，精确
//        java.lang.IllegalArgumentException: provider doesn't exist: gps
        if (locMgr.getAllProviders().indexOf(LocationManager.GPS_PROVIDER) >= 0) {
            //注册位置更新监听(最小时间间隔为5秒,最小距离间隔为5米)
            locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, interval, distance, listener);
            MLog.d("requestLocationUpdates: GPS_PROVIDER");
        }
        //网络定位，在没有网络的时候无法获得位置信息。(推荐和GPS定位同时使用)
        if (locMgr.getAllProviders().indexOf(LocationManager.NETWORK_PROVIDER) >= 0) {
            //注册位置更新监听(最小时间间隔为5秒,最小距离间隔为5米)
            locMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, interval, distance, listener);
            MLog.d("requestLocationUpdates: NETWORK_PROVIDER");
        }
    }

    /**
     * 停止监听
     * */
    public void removeUpdates(LocationListener listener){
        locMgr.removeUpdates(listener);
    }


    /**
     * 保存最后一次定位信息
     * */
    public static void saveLocationInfo(Context context, Location location){
        if (location == null){
            return;
        }
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        saveLastLocationInfo(context, latitude, longitude);
    }
    /**
     * 保存最后一次定位信息
     * */
    public static void saveLastLocationInfo(Context context, double latitude, double longitude){
        SharedPreferencesUtil.set(context, PREF_NAME, KEY_LASTUPDATE_LATITUDE, String.valueOf(latitude));
        SharedPreferencesUtil.set(context, PREF_NAME, KEY_LASTUPDATE_LONGITUDE, String.valueOf(longitude));
    }

    /**
     * 获取位置信息
     * */
    public static String getLocationInfo(Location location){
        StringBuilder sbInfo = new StringBuilder();
        if (location != null){
            //获取位置信息
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            double accuracy = location.getAccuracy();//定位精度半径
            SimpleDateFormat df = new SimpleDateFormat(PATTERN_DATE_TIME);
            Date date = new Date(location.getTime());

            sbInfo.append(String.format("latitude=%f", latitude));
            sbInfo.append(String.format("longitude=%f", longitude));
            sbInfo.append(String.format("accuracy=%f", accuracy));
            sbInfo.append(String.format("time=%s", df.format(date)));

            //GPS
            if(location.getProvider().equalsIgnoreCase(LocationManager.GPS_PROVIDER)){
                double speed = location.getSpeed();//定位速度
                double bearing = location.getBearing();//定位方向

                sbInfo.append(String.format("speed=%f", speed));
                sbInfo.append(String.format("bearing=%f", bearing));
            }

            if(location.getProvider().equalsIgnoreCase(LocationManager.NETWORK_PROVIDER)){
                String desc = "";//位置描述信息
                Bundle locBundle = location.getExtras();
                if (locBundle != null) {
                    desc = locBundle.getString("desc");
                }
                sbInfo.append(String.format("desc=%s", desc));
            }
        }

        return sbInfo.toString();
    }
    public static String getLastLatitude(Context context){
        return SharedPreferencesUtil.get(context, PREF_NAME, KEY_LASTUPDATE_LATITUDE, "0");
    }
    public static String getLastLongitude(Context context){
        return SharedPreferencesUtil.get(context, PREF_NAME, KEY_LASTUPDATE_LONGITUDE, "0");
    }

}
