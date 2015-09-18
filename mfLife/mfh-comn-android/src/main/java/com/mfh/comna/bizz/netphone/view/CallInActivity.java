package com.mfh.comna.bizz.netphone.view;

import android.app.ActionBar;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.hisun.phone.core.voice.Device;
import com.hisun.phone.core.voice.Device.CallType;
import com.hisun.phone.core.voice.DeviceListener.Reason;
import com.hisun.phone.core.voice.listener.OnVoIPListener;
import com.mfh.comna.R;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.view.BaseComnActivity;
import com.mfh.comna.bizz.netphone.logic.NetPhoneService;


/**
 * 网络通话电话界面
 * */
public class CallInActivity extends BaseComnActivity implements OnClickListener {
    private static final int NOTIFICATION_ID = 10;
	
	private String	mCurrentCallId;
	private String mVoipAccount;
	private Button mBtnRefuse;
	private Button mBtnReceive;
	private Button mBtnRelease;
	private TextView mTvPhoneNumber;
    private TextView mTvTime;
    private boolean isReceive;  //判断是否已经接听
    private NotificationManager manager;
    private Notification notification;
    private NetPhoneService netPhoneService;

    private Handler handler = new Handler() {
        private int second = 0;
        private int minute = 0;
        private int hour = 0;
        @Override
        public void handleMessage(Message msg) {
            if (isReceive) {
                second++;
                if (60 == second) {
                    minute++;
                    second = 0;
                    if (60 == minute) {
                        hour++;
                        minute = 0;
                    }
                }
                mTvTime.setText(formatTime(new int[]{hour,minute,second}));
                handler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };

    private String formatTime(int[] time) {
        StringBuilder builder = new StringBuilder();

        for(int item : time){
            if (item < 10) {
                builder.append("0" + item + " : ");
            } else {
                builder.append(item + " : ");
            }
        }
        String timeStr = builder.toString();
        return timeStr.substring(0, timeStr.length() - 2);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_netphone;
    }

    @Override
    protected void initActionBar(ActionBar actionBar) {
        super.initActionBar(actionBar);
        actionBar.setIcon(R.drawable.logo_mfh);
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        mBtnRefuse = (Button) findViewById(R.id.refuse);
        mBtnReceive = (Button) findViewById(R.id.recieve);
        mBtnRelease = (Button) findViewById(R.id.release);
        mTvPhoneNumber = (TextView) findViewById(R.id.phone_number);
        mTvTime = (TextView) findViewById(R.id.phone_time);
        netPhoneService =  ServiceFactory.getService(NetPhoneService.class, this);

        mBtnReceive.setOnClickListener(this);
        mBtnRefuse.setOnClickListener(this);
        mBtnRelease.setOnClickListener(this);

		Intent intent = getIntent(); 
		Bundle extras = intent.getExtras(); 
		mVoipAccount = extras.getString(Device.CALLER); 
		mCurrentCallId = extras.getString(Device.CALLID);

        mTvPhoneNumber.setText(mVoipAccount);

        registListener();
        initNotification();
	}

    @Override
    protected void onResume() {
        super.onResume();
        if (isReceive) {
            manager.cancel(NOTIFICATION_ID);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return false;
    }

    /**
     * 初始化任务栏通知
     * */
    private void initNotification() {
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new Notification();
        notification.when = System.currentTimeMillis();
        notification.icon = R.drawable.colour_logo;
        notification.tickerText = getString(R.string.netphone_on_calling);
        notification.flags |= Notification.FLAG_ONGOING_EVENT; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
        notification.flags |= Notification.FLAG_NO_CLEAR; // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
        Intent intent = new Intent(this, CallInActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(this, getString(R.string.netphone_name), getString(R.string.netphone_name), pi);
    }

    private void showNotification() {
        manager.notify(NOTIFICATION_ID, notification);
    }

    @Override
	public void onClick(View v) {
        if (getDevice() == null)
            return;
        int id = v.getId();
        if (id == R.id.refuse) {
            getDevice().rejectCall(mCurrentCallId, 3);
        } else if (id == R.id.recieve) {
            mBtnRefuse.setVisibility(View.GONE);
            mBtnReceive.setVisibility(View.GONE);
            mBtnRelease.setVisibility(View.VISIBLE);
            getDevice().acceptCall(mCurrentCallId);
            isReceive = true;
            handler.sendEmptyMessage(0);
        } else if (id == R.id.release) {
            getDevice().releaseCall(mCurrentCallId);
        }
    }


    @Override
    public void onBackPressed() {
        if (isReceive) {
            showNotification();
            moveTaskToBack(true);
        }
    }

    @Override
    public void finish() {
        isReceive = false;
        super.finish();
        manager.cancel(NOTIFICATION_ID);
        handler = null;
    }

    /**
     * 注册通话监听
     * */
	private void registListener() {
        if (null == getDevice())
            return;
        getDevice().setOnVoIPListener(new OnVoIPListener() {

			@Override
			public void onCallAlerting(String arg0) {
			}

			@Override
			public void onCallAnswered(String arg0) {
			}

			@Override
			public void onCallMediaInitFailed(String arg0, int arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onCallMediaInitFailed(String arg0, CallType arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onCallMediaUpdateRequest(String arg0, int arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onCallMediaUpdateResponse(String arg0, int arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onCallPaused(String arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onCallPausedByRemote(String arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onCallProceeding(String arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onCallReleased(String arg0) {
				// TODO Auto-generated method stub
				finish();
			}

			@Override
			public void onCallTransfered(String arg0, String arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onCallVideoRatioChanged(String arg0, String arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onCallback(int arg0, String arg1, String arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onMakeCallFailed(String arg0, Reason arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSwitchCallMediaTypeRequest(String arg0, CallType arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSwitchCallMediaTypeResponse(String arg0, CallType arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTransferStateSucceed(String arg0, boolean arg1) {
				// TODO Auto-generated method stub
			}
			
		});
	}

	private Device getDevice() {
		return netPhoneService.getDevice();
	}
	
}