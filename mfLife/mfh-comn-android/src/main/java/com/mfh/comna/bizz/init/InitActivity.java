package com.mfh.comna.bizz.init;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

import com.mfh.comna.comn.cfg.ServerConfig;
import com.mfh.comna.view.BaseComnActivity;

/**
 * 系统初始化界面，做版本检测、数据库安装等所有初始化工作
 *
 * @author zhangyz created on 2013-5-7
 * @since Framework 1.0
 */
public abstract class InitActivity extends BaseComnActivity {
    AlertDialog builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        doAsyncTask();
    }

    /**
     * 获取服务
     * @return
     * @author zhangyz created on 2013-6-6
     */
    protected InitService getService() {
        return InitService.getService(this);
    }

    /**
     * 初始化之前的准备工作
     */
    public void initBefore() {
        //super.();
    }

    /**
     * 其他初始化工作
     */
    protected void initOther() {
        ServerConfig config = ServerConfig.getServerConfig(this);
        config.init();
    }

    @Override
    public Object doInBackground(int taskKind, Object... params) {
        initBefore();
        getService().init(this);
        initOther();
        return 0;
    }

    @Override
    public ProgressDialog onPreExecute(int taskKind) {
        return null;//无需开启进度条，因为本身界面有。
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        Thread.currentThread().interrupt();//中断当前线程.
    }

    /**
     * 执行启动主界面
     */
    protected abstract void startMainActivity();

    @Override
    public void onPostExecute(int taskKind, Object result, Object... params) {
        super.onPostExecute(taskKind, result, params);
        if (builder == null) {//重点是这里,如果条件成立，表示没有新的更新程序，否则不能让他直接跳转
//            new CountDownTimer(2000, 100) {//延时三秒钟
//                @Override
//                public void onTick(long millisUntilFinished) {
//
//                }
//                @Override
//                public void onFinish() {
//                    startMainActivity();
//                    //finish();
//                }
//            }.start();
            startMainActivity();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
