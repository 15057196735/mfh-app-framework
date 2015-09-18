package com.mfh.comna.bizz.error;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.mfh.comna.bizz.login.logic.MfhLoginService;
import com.mfh.comna.R;
import com.mfh.comna.bizz.error.service.ErrorCollectService;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.api.utils.NetWorkUtil;
import com.mfh.comna.view.BaseComnActivity;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class SendErrorActivity extends BaseComnActivity {

    private Button ok;
    private String error_msg;
    private Throwable ex;
    private Dialog dialog;
    private BroadcastReceiver receiver = null;
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private MfhLoginService ls = MfhLoginService.get();
    private ErrorCollectService errorCollectService = ServiceFactory.getService(ErrorCollectService.class);

    @Override
    protected void initActionBar(ActionBar actionBar) {
        super.initActionBar(actionBar);
        actionBar.setIcon(R.drawable.white_logo);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_loginoutinfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        ok = (Button) findViewById(R.id.ok);
        error_msg = getIntent().getStringExtra("msg");
        ex = (Throwable) getIntent().getExtras().get("ex_entity");
        /*AlertDialog builder = new AlertDialog.Builder(this)
                .setTitle("退出程序")
                .setMessage("非常抱歉，程序运行异常，即将退出，我们正在积极收集错误原因并且在以后版本加以改进！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (NetWorkUtil.isConnect(SendErrorActivity.this)) {

                        } else
                            showHint("网络连接没有连接...");
                    }
                })
                .create();
        //if (!builder.isShowing())
        builder.show();*/
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorCollectService.getDao().save(errorCollectService.getNewEntity(ex));//保存到本地
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        register();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private void register() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(ErrorConstants.ERROR_DISMISS_DIALOG);
        intentFilter.addAction(ErrorConstants.ERROR_DISMISS_DIALOG_FAIL);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    if (NetWorkUtil.isConnect(context)) {
                        //网络连接恢复
                    }
                    else {
                        //网络连接断开
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                }
                else if (action.equals(ErrorConstants.ERROR_DISMISS_DIALOG)) {
                    showHint("日志上传成功");
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
                else if (action.equals(ErrorConstants.ERROR_DISMISS_DIALOG_FAIL)) {
                    showHint("日志上传异常");
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            }
        };
        registerReceiver(receiver, intentFilter);
    }

    /**
     * 获取程序的版本信息
     * @return
     */
    private String getVersionInfo(){
        try {
            PackageManager pm = SendErrorActivity.this.getPackageManager();
            PackageInfo info =pm.getPackageInfo(SendErrorActivity.this.getPackageName(), 0);
            return  info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "版本号未知";
        }
    }

    /**
     * 获取手机的硬件信息
     * @return
     */
    private String getMobileInfo() {
        StringBuffer sb = new StringBuffer();
        //通过反射获取系统的硬件信息
        try {
            Field[] fields = Build.class.getDeclaredFields();
            for(Field field: fields){
                //暴力反射 ,获取私有的信息
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                sb.append(name+"="+value);
                sb.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
