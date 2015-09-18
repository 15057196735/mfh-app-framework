package com.mfh.comna.bizz.base;


import android.content.Intent;
import android.content.res.TypedArray;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.mfh.comna.api.utils.MLog;
import com.mfh.comna.location.MfLocationManagerProxy;

import butterknife.ButterKnife;


/**
 * Activity基类·定位·动画
 *
 * */
public class BaseActivity extends AppCompatActivity implements AMapLocationListener {
    private static final String TAG = BaseActivity.class.getSimpleName();

    //高德地图定位
    private static final int LOCATION_INTERVAL = 2;
    private LocationManagerProxy mAMapLocationManager;
    //满分定位
    private MfLocationManagerProxy mMfLocationManager;

    //窗口动画
    public static final int ANIM_TYPE_NEW_NONE = -1;//无动画
    public static final int ANIM_TYPE_NEW_FLOW = 0;//新流程，底部弹出
    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;

    protected int getLayoutResId(){return 0;}
    protected void initToolBar(){
    }
    protected boolean isAMapLocationEnable(){
        return false;
    }
    protected boolean isMfLocationEnable(){
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        retrieveAnimations();

        super.onCreate(savedInstanceState);


        setContentView(getLayoutResId());

        ButterKnife.bind(this);

        initToolBar();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isAMapLocationEnable()){
            initAMapLocation();
        }

        if (isMfLocationEnable()){
            initMfLocation();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAMapLocation();
        stopMfLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }

    /**
     * 监听返回--是否退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // TODO,双击退出应用
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    /**
     * Retrieve the animations set in the theme applied to this activity in the manifest..
     * */
    private void retrieveAnimations(){
        TypedArray activityStyle = getTheme().obtainStyledAttributes(new int[] {android.R.attr.windowAnimationStyle});
        int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);
        activityStyle.recycle();

// Now retrieve the resource ids of the actual animations used in the animation style pointed to by
// the window animation resource id.
        activityStyle = getTheme().obtainStyledAttributes(windowAnimationStyleResId,
                new int[] {android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation});
        activityCloseEnterAnimation = activityStyle.getResourceId(0, 0);
        activityCloseExitAnimation = activityStyle.getResourceId(1, 0);
        activityStyle.recycle();
    }


    /**
     * 初始化定位
     */
    protected void initMfLocation(){
        mMfLocationManager = MfLocationManagerProxy.getInstance(this);
        mMfLocationManager.requestLocationData(LOCATION_INTERVAL * 1000, 15, mfLocationListener);
    }

    protected void stopMfLocation(){
        if (mMfLocationManager != null) {
            mMfLocationManager.removeUpdates(mfLocationListener);
        }
        mMfLocationManager = null;
    }

    private LocationListener mfLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            MLog.d(MfLocationManagerProxy.getLocationInfo(location));
            //TODO,保存位置信息
            MfLocationManagerProxy.saveLocationInfo(BaseActivity.this, location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //Provider状态在可用、暂不可用、无服务三个状态之间直接切换时触发此函数
            MLog.d(String.format("%s, status:%d", provider, status));
        }

        @Override
        public void onProviderEnabled(String provider) {
            //Provider被enable时触发此函数,比如GPS被打开
            MLog.d(provider + " enabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            //Provider被disable时触发此函数,比如GPS被关闭
            MLog.d(provider + " disabled");
        }
    };

    /**
     * 初始化定位
     */
    protected void initAMapLocation() {

        mAMapLocationManager = LocationManagerProxy.getInstance(this);

        //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
        //在定位结束后，在合适的生命周期调用destroy()方法
        //其中如果间隔时间为-1，则定位只定一次
        mAMapLocationManager.requestLocationData(
                LocationProviderProxy.AMapNetwork, LOCATION_INTERVAL*1000, 15, this);

        mAMapLocationManager.setGpsEnable(false);
    }

    /**
     * 停止定位，并销毁定位资源
     * */
    private void stopAMapLocation() {
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destory();
        }
        mAMapLocationManager = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getAMapException().getErrorCode() == 0){
//            MLog.d("onLocationChanged: " + aMapLocation.toString());
            //step 1: save last location info
            MfLocationManagerProxy.saveLastLocationInfo(BaseActivity.this,
                    aMapLocation.getLatitude(), aMapLocation.getLongitude());

            //step 2: query surround subdis
            //TODO
            //locatioin failed
            //location success
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
