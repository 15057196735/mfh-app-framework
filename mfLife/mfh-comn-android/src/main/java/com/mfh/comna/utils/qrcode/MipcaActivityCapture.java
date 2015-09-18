package com.mfh.comna.utils.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.mfh.comna.R;
import com.mfh.comna.utils.DialogUtil;
import com.mfh.comna.utils.qrcode.camera.CameraManager;
import com.mfh.comna.utils.qrcode.decoding.CaptureActivityHandler;
import com.mfh.comna.utils.qrcode.decoding.InactivityTimer;
import com.mfh.comna.utils.qrcode.view.ViewfinderView;

import java.io.IOException;
import java.util.Vector;

/**
 * Initial the camera
 * @author Ryan.Tang
 */
public class MipcaActivityCapture extends Activity implements Callback {
	private ImageButton ibBack;
	private TextView tvTopBarTitle;

	private ImageView ivFlash;
	private boolean flashEnable;

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private QRBeepManager beepManager;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);

		//ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
		CameraManager.init(getApplication());

		initTopBar();

		ivFlash = (ImageView) findViewById(R.id.ivFlash);
		ivFlash.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flashEnable == true) {
					flashEnable = false;
					// 开闪光灯
					CameraManager.get().openLight();
					ivFlash.setImageResource(R.drawable.ic_flashlight_on);
				} else {
					flashEnable = true;
					// 关闪光灯
					CameraManager.get().offLight();
					ivFlash.setImageResource(R.drawable.ic_flashlight_off);
				}
			}
		});
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		beepManager = new QRBeepManager(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}

		beepManager.close();
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * 初始化导航栏视图
	 * */
	private void initTopBar(){
		tvTopBarTitle = (TextView) findViewById(R.id.topbar_title);
		ibBack = (ImageButton) findViewById(R.id.ib_back);

		tvTopBarTitle.setText(R.string.topbar_title_qrcode);
		ibBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}

	/**
	 * 处理扫描结果
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		beepManager.playBeepSoundAndVibrate();
		String resultString = result.getText();
		if (resultString.equals("")) {
			DialogUtil.showHint("扫描失败!");
		}else {
            //java.lang.SecurityException: Unable to find app for caller android.app.ApplicationThreadProxy
            try{
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("result", resultString);
//                bundle.putParcelable("bitmap", barcode);
                resultIntent.putExtras(bundle);
                setResult(RESULT_OK, resultIntent);
            }
            catch(Exception ex){
                Log.e("Nat", ex.toString());
            }
		}
		finish();
	}
	
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

}