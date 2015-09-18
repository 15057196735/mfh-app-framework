package com.mfh.comna.api.ui;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.mfh.comna.api.Constants;
import com.mfh.comna.R;
import com.mfh.comna.utils.DialogUtil;
import com.mfh.comna.view.BaseFragmentActive;


/**
 * 地图·定位地点并显示气泡
 * */
public class CustomMapActivity extends BaseFragmentActive implements AMap.OnMarkerClickListener {
    private TextView tvTopBarTitle;
    private ImageButton ibBack;

    private MapView mapView;
    private AMap aMap;
    private Marker marker;

    public static void actionStart(Activity context, String topBarTitle,
                                   String latitude, String longitude,
                                   String marketTitle, String markerSnippet){
        Intent intent = new Intent(context, CustomMapActivity.class);
        intent.putExtra(Constants.BUNDLE_EXTRA_KEY_TOPBAR_TITLE, topBarTitle);
        intent.putExtra(Constants.BUNDLE_EXTRA_KEY_LATITUDE, latitude);
        intent.putExtra(Constants.BUNDLE_EXTRA_KEY_LONGITUDE, longitude);
        intent.putExtra(Constants.BUNDLE_EXTRA_KEY_MARKER_TITLE, marketTitle);
        intent.putExtra(Constants.BUNDLE_EXTRA_KEY_MARKER_SNIPPET, markerSnippet);
        context.startActivity(intent);

//        if(bAnimation){
//            //Activity切换动画,缩放+透明
//            context.overridePendingTransition(com.mfh.comna.R.anim.zoom_in, com.mfh.comna.R.anim.zoom_out);
//        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_custom_map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        initAMap();

        Intent intent = this.getIntent();
        if(intent != null){
            String topbarTitle = intent.getStringExtra(Constants.BUNDLE_EXTRA_KEY_TOPBAR_TITLE);
            String latitude = intent.getStringExtra(Constants.BUNDLE_EXTRA_KEY_LATITUDE);
            String longitude = intent.getStringExtra(Constants.BUNDLE_EXTRA_KEY_LONGITUDE);
            String markerTitle = intent.getStringExtra(Constants.BUNDLE_EXTRA_KEY_MARKER_TITLE);
            String marketSnippet = intent.getStringExtra(Constants.BUNDLE_EXTRA_KEY_MARKER_SNIPPET);

            initTopBar(topbarTitle);
            if(latitude != null && longitude != null){
                LatLng latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                // 在地图上添加marker
                marker =aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                        .position(latLng).title(markerTitle)
                        .snippet(marketSnippet).draggable(true));
                marker.showInfoWindow();

                aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
            }
        }else{
            initTopBar("");
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 初始化导航栏视图
     * */
    private void initTopBar(String title){
        tvTopBarTitle = (TextView) findViewById(R.id.topbar_title);
        ibBack = (ImageButton) findViewById(R.id.ib_back);

        tvTopBarTitle.setText(title);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 初始化AMap对象
     */
    private void initAMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    private void setUpMap() {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(13));//默认显示地图缩放级别为10

//        aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
//        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
//        aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
//        aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
    }

    /**
     * 对marker标注点点击响应事件
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        if (aMap != null) {
            jumpPoint(marker);
        }

        DialogUtil.showHint("click " + marker.getTitle());
        return false;
    }

    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker) {
        final LatLng latLng = marker.getPosition();
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        Point startPoint = proj.toScreenLocation(latLng);
        startPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * latLng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * latLng.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                aMap.invalidate();// 刷新地图
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });

    }
}
