package com.mfh.comna.view;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.mfh.comna.api.utils.MLog;
import com.mfh.comna.comn.logic.IBaseViewComponent;
import com.mfh.comna.comn.logic.IFragmentSelect;
import com.mfh.comna.comn.logic.ISubActivityMenuCheck;
import com.mfh.comna.comn.logic.MyMultiAsyncTask;
import com.mfh.comna.comn.logic.UpdateResultsRunable;
import com.mfh.comna.utils.DialogUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.Date;

import butterknife.ButterKnife;


/**
 * 自定义的FragmentActive基类，包含自定义actionbar等
 * 
 * @author zhangyz created on 2013-4-10
 * @since Framework 1.0
 */
public abstract class BaseFragmentActive extends FragmentActivity
    implements OnClickListener, IBaseViewComponent<Object, Object>, IFragmentSelect, ISubActivityMenuCheck {

    protected ActionBar mActionBar; 
    private Handler mHandler = null;//用于异步更新界面    
    private DialogUtil dialogUtil;    
    protected BaseFragment curFragment = null;
    
    /**
     * 获取面板的layout布局号
     * @return
     * @author zhangyz created on 2013-4-5
     */
    public abstract int getLayoutId();
    
    /**
     * 设置标题
     * @author zhangyz created on 2013-4-10
     *
     */
    protected void initActionBar(ActionBar actionBar) {
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
       
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());


        //使用ButterKnife工具
        ButterKnife.bind(this);
        
        mActionBar = this.getActionBar();// (ActionBar)this.findViewById(R.id.gd_action_bar);
        if (mActionBar != null)
            initActionBar(mActionBar);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    /**
     * 在指定的位置显示一个fragment
     * @param fragment
     * @param targetContainer
     * @param bAdd 是增加还是替换
     * @return 返回添加后的fragment的id
     * @author zhangyz created on 2013-4-16
     */
    public int showFragment(Integer targetContainer, BaseFragment fragment, boolean bAdd) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fc = fm.beginTransaction();
        String tag = Long.toString(new Date().getTime());//tag，下面两个地方都用到，重用一个名。便于再后面backUpFragment中使用。
        if (bAdd) {
            fc.add(targetContainer, fragment, tag);
        }
        else {
            //java.lang.IllegalStateException: Can't change tag of fragment SurroundFragment{43311878 #0 id=0x7f07006e 1430104071999}: was 1430104071999 now 1430104446542
            fc.replace(targetContainer, fragment, tag);
        }
        
        //注：使用 popBackStack()可将fragment从后台堆栈中弹出 (模拟用户按下BACK 命令).
        fc.addToBackStack(tag);//把当前操作的这个fragment加入返回堆栈,tag代表起个名字;后面按回退键时需要。
        fc.commit();
        curFragment = fragment;
        return fragment.getId();
    }

    public int showFragment(Integer targetContainer, BaseFragment fragment, boolean tagEnabled, boolean bPopBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fc = fm.beginTransaction();
        if(tagEnabled){
            String tag = Long.toString(new Date().getTime());//tag，下面两个地方都用到，重用一个名。便于再后面backUpFragment中使用。
            fc.replace(targetContainer, fragment, tag);

            if(bPopBackStack){
                fc.addToBackStack(tag);//把当前操作的这个fragment加入返回堆栈,tag代表起个名字;后面按回退键时需要。
            }
        }
        else{
            fc.replace(targetContainer, fragment);
        }

        fc.commit();
        curFragment = fragment;
        return fragment.getId();
    }

    /**
     * 显示指定Fragment
     * TODO:java.lang.IllegalStateException: Fragment already added: SurroundFragment{43243e60 #1 id=0x7f07006e}
     * */
    public int showFragmentWithoutTag(Integer targetContainer, BaseFragment fragment, boolean bAdd) {
        FragmentManager fm = getSupportFragmentManager();
//        fm.popBackStack();

        FragmentTransaction fc = fm.beginTransaction();
        if (bAdd) {
            fc.add(targetContainer, fragment);
        }
        else {
            fc.replace(targetContainer, fragment);
        }

        //注：使用 popBackStack()可将fragment从后台堆栈中弹出 (模拟用户按下BACK 命令).
//        fc.addToBackStack(null);//把当前操作的这个fragment加入返回堆栈,tag代表起个名字;后面按回退键时需要。
        fc.commit();

        curFragment = fragment;
        return fragment.getId();
    }

    public int showFragmentWithTag(Integer targetContainer, BaseFragment fragment, boolean bAdd, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fc = fm.beginTransaction();
        if (bAdd) {
            fc.add(targetContainer, fragment, tag);
        }
        else {
            fc.replace(targetContainer, fragment, tag);
        }


        //注：使用 popBackStack()可将fragment从后台堆栈中弹出 (模拟用户按下BACK 命令).
        fc.addToBackStack(tag);//把当前操作的这个fragment加入返回堆栈,tag代表起个名字;后面按回退键时需要。
        fc.commit();

        curFragment = fragment;
        return fragment.getId();
    }

    @Override
    public void OnFragmentSelected(Fragment fmt) {
        //invalidateOptionsMenu();//当fragmentActive下面的fragment本身也createOptionMenu时，系统框架会自动失效。
        if (fmt instanceof BaseFragment) {
            curFragment = (BaseFragment)fmt;
        }
    }
    
    protected BaseFragmentActive getThis() {
        return this;
    }
    
    /**
     * 提供一个给子类继承的机会。
     * @return
     * @author zhangyz created on 2013-4-20
     */
    public BaseFragment getCurFragment() {
        return curFragment;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == IBaseViewComponent.RETURN_CODE_NULL)
            return; //说明直接返回。
        BaseFragment newerFmt = getCurFragment();
        if (newerFmt != null)
            curFragment = newerFmt;
        
        if (curFragment != null) {
            curFragment.onFragmentResult(requestCode, resultCode, intent);
        }
    }
    
    protected DialogUtil getDialogUtil() {
        if (dialogUtil == null)
            dialogUtil = new DialogUtil(this);
        return dialogUtil;
    }
    
    /**
     * 回退fragment
     * @return
     * @author zhangyz created on 2013-5-18
     */
    protected boolean backUpFragment() {
        FragmentManager fm = getSupportFragmentManager();
        int haveCount = fm.getBackStackEntryCount();
        if (curFragment != null && haveCount > 1) {//保留第一个   
            if (fm.popBackStackImmediate()) {//立即弹出
                haveCount = fm.getBackStackEntryCount();
                if (haveCount > 0) {
                    String fmTagName = fm.getBackStackEntryAt(haveCount - 1).getName();//addBackStack时使用的名字
                    curFragment = (BaseFragment)fm.findFragmentByTag(fmTagName);//加入Fragment时使用的名字
                }
                else
                    curFragment = null;
            }
            return true;
        }
        else
            return false;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                /*
                 * Intent intent = new Intent(this, MainActivity.class);
                 * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); startActivity(intent);
                 */
                if (!backUpFragment()) {   
                    this.setResult(RETURN_CODE_NULL, null);//返回为0
                    this.finish();//结束当前activity
                }
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
    public ProgressDialog onPreExecute(int taskKind) {
        // 开启进度条
        return ProgressDialog.show(this, "请稍等...", "正在处理中...", true);
    }
    
    @Override
    public void onPostExecute(int taskKind, Object result, Object...params) {
        
    }

    @Override
    public void onProgressUpdate(int taskKind, Integer... values) {
                
    }

    @Override
    public void onClick(View arg0) {
        
    }

    @Override
    public void doInBackgroundException(int taskKind, Throwable ex, Object... params) {
        ex.printStackTrace();
//        Toast.makeText(this, "执行出错:" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        MLog.e(ex.toString());
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
