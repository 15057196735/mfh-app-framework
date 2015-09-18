package com.mfh.comna.view;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.mfh.comna.comn.logic.IBaseViewComponent;
import com.mfh.comna.comn.logic.ISubActivityMenuCheck;
import com.mfh.comna.comn.logic.MyMultiAsyncTask;
import com.mfh.comna.comn.logic.UpdateResultsRunable;
import com.mfh.comna.utils.DialogUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 * 普通activity基类
 * 采用自定义actionbar，home按钮默认返回到上一个页面。
 * @author zhangyz created on 2013-4-5
 * @since Framework 1.0
 */
public abstract class BaseComnActivity extends Activity implements OnClickListener, 
        IBaseViewComponent<Object, Object>,ISubActivityMenuCheck {

    protected ActionBar mActionBar;//自定义actionBar
    private Handler mHandler = null;//用于异步更新界面
    private DialogUtil dialogUtil;

    private final String mPageName = this.getClass().getSimpleName();
    
    /**
     * 获取面板的layout布局号
     * @return
     * @author zhangyz created on 2013-4-5
     */
    public abstract int getLayoutId();
    
    /**
     * 设置标题，返回null默认使用AndroidManifest.xml中设置的标题
     * @return
     * @author zhangyz created on 2013-4-10
     */
    protected void initActionBar(ActionBar actionBar) {
        actionBar.setDisplayHomeAsUpEnabled(true); //默认Activity可返回
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*android.view.ActionBar aBar = this.getActionBar();
        if (aBar != null)
            aBar.hide();*/
        setContentView(getLayoutId());

        ButterKnife.bind(this);
        
        mActionBar = this.getActionBar();// (ActionBar)this.findViewById(R.id.gd_action_bar);
        if (mActionBar != null)
            initActionBar(mActionBar);

        //SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        //然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);

        //MobclickAgent.setAutoLocation(true);
        //MobclickAgent.setSessionContinueMillis(1000);

        //发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率
        MobclickAgent.updateOnlineConfig(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }

    protected DialogUtil getDialogUtil() {
        if (dialogUtil == null)
            dialogUtil = new DialogUtil(this);
        return dialogUtil;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                /*
                 * Intent intent = new Intent(this, MainActivity.class);
                 * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); startActivity(intent);
                 */
                this.setResult(RETURN_CODE_NULL, null);//返回为0
                this.finish();//结束当前activity
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 提示消息，稍后自动关闭
     * @param message 提示消息
     * @author zhangyz created on 2013-4-12
     */
    protected void showHint(CharSequence message) {
        getDialogUtil().showHint(message);
    }
    
    /** 
     * 提示消息，点击确定后关闭 
     * @param message 提示消息
     */
    protected void showMessage(CharSequence message){ 
        getDialogUtil().showMessage(message);
    }
    
    protected void showYesNoDialog(CharSequence message, DialogInterface.OnClickListener listen) {
        getDialogUtil().showYesNoDialog(message, listen);
    }
    
    /**
     * 显示一个典型的两按钮的询问对话框
     * @param title
     * @param message
     * @return ture：选择了是: false:选择了否
     * @author zhangyz created on 2013-4-5
     */
    protected void showYesNoDialog(CharSequence title, CharSequence message, 
            DialogInterface.OnClickListener listen) {
        getDialogUtil().showYesNoDialog(title, message, listen);
    }
    
    @Override
    public void doAsyncTask() {
        new MyMultiAsyncTask<Object, Object>(this).execute();        
    }

    @Override
    public void doAsyncTaskWithParam(Object... param) {
        new MyMultiAsyncTask<Object, Object>(this).execute(param);        
    }
    
    @Override
    public void doAsyncTask(int taskKind) {
        new MyMultiAsyncTask<Object, Object>(this, taskKind).execute();        
    }
    
    @Override
    public void doAsyncTask(int taskKind, Object... param) {
        new MyMultiAsyncTask<Object, Object>(this, taskKind).execute(param);        
    }
    
    @Override
    public void doAsyncUpdateUi(Object... param) {
        if (mHandler == null)
            mHandler = new Handler();
        mHandler.post(new UpdateResultsRunable<Object>(this, param));
    }
    
    @Override
    public Object doInBackground(int taskKind, Object... params) {
        return null;
    }
    
    @Override
    public void onPostExecute(int taskKind, Object result, Object...params) {
        
    }    
    
    @Override
    public ProgressDialog onPreExecute(int taskKind) {
        // 开启进度条
        return ProgressDialog.show(this, "请稍等...", "正在处理中...", true);
    }

    @Override
    public void onProgressUpdate(int taskKind, Integer... values) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doInBackgroundException(int taskKind, Throwable ex, Object... params) {
        Toast.makeText(this, "执行出错:" + ex.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        
    }

    @Override
    public boolean OnOptionsMenuItemCheck(int groupId, int menuId) {
        return true;
    }
    
    @Override
    public boolean setGroupVisible(Menu menu) {
        return false;
    }
}
