package com.mfh.comna.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.mfh.comna.bizz.BizApplication;
import com.mfh.comna.api.ui.dialog.DialogHelper;
import com.mfh.comna.R;
import com.mfh.comna.actionbar.ActionBar;
import com.mfh.comna.actionbar.BaseActionBar.OnActionBarListener;
import com.mfh.comna.comn.cfg.UConfigCache;
import com.mfh.comna.comn.logic.IBaseViewComponent;
import com.mfh.comna.comn.logic.MyMultiAsyncTask;
import com.mfh.comna.comn.logic.UpdateResultsRunable;
import com.mfh.comna.utils.DialogUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 * 自定义的Fragment基类，可以再包含自定义actionbar,名为R.id.gd_action_bar或使用父类的actionBar
 * 分别是myActionBar和mActionBar。这里的myActionBar不同于android系统自带的actionbar。
 * 另外封装了异步操作框架支持(统一处理异常、支持多异步任务区分等)、对话框支持等。
 * @author zhangyz created on 2013-4-10
 * @since Framework 1.0
*/
public abstract class BaseFragment extends Fragment implements OnClickListener,
    OnActionBarListener, IBaseViewComponent<Object, Object> {

    protected ActionBar myActionBar;//自定义actionBar
    protected android.app.ActionBar mActionBar;
    protected View rootView;

    //用于异步更新界面
    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private DialogUtil dialogUtil;

    private String TAG = this.getClass().getSimpleName();


    /**
     * 获取统一配置服务
     * @return
     * @author zhangyz created on 2013-5-25
     */
    protected UConfigCache getUconfig() {
        return BizApplication.getUconfig();
    }


    /**
     * 获取面板的layout布局号
     * @return
     * @author zhangyz created on 2013-4-5
     */
    public abstract int getLayoutId();
    
    /**
     * frament内部的初始化工作
     * @param rootView 根view
     * @param container
     * @param savedInstanceState
     * @author zhangyz created on 2014-3-11
     */
    protected abstract void createViewInner(View rootView, ViewGroup container, Bundle savedInstanceState);
    
    /**
     * 设置标题
     * @return
     * @author zhangyz created on 2013-4-10
     */

    public CharSequence initBarTitle() {
        return null;
    }
    
    /**
     * 类似于activity的onActivityResult，用以分担activity中的该方法逻辑
     * @param requestCode
     * @param resultCode
     * @param intent
     * @author zhangyz created on 2013-4-16
     */
    protected void onFragmentResult(int requestCode, int resultCode, Intent intent) {
        
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }

    /**
     * {@hide}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        /*android.view.ActionBar aBar = this.getActionBar();
        if (aBar != null)
            aBar.hide();*/

        rootView = inflater.inflate(getLayoutId(), container, false);

        ButterKnife.bind(this, rootView);

        CharSequence title = initBarTitle();
        myActionBar = (ActionBar)rootView.findViewById(R.id.gd_action_bar); 
        if (myActionBar != null) {
            if (title != null)
                myActionBar.setTitle(title);      
            myActionBar.setOnActionBarListener(this);
        }
        else {
            mActionBar = this.getActivity().getActionBar();
            if (mActionBar != null && title != null) {
                mActionBar.setTitle(title);   
            }
        }
        createViewInner(rootView, container, savedInstanceState);
        return rootView;
    }
    
    /**
     * 获取对话框工具类
     * @return
     * @author zhangyz created on 2014-3-11
     */
    protected DialogUtil getDialogUtil() {
        if (dialogUtil == null)
            dialogUtil = new DialogUtil(this.getContext());
        return dialogUtil;
    }
    
    @Override
    public void onActionBarItemClicked(int position) {
        if (position == OnActionBarListener.HOME_ITEM) {
            this.getActivity().setResult(RETURN_CODE_NULL, null);//返回为0
            this.getActivity().finish();//结束当前activity
        }
    }


    @Override
    public void doAsyncTask() {
        new MyMultiAsyncTask<>(this).execute();
    }

    @Override
    public void doAsyncTaskWithParam(Object... param) {
        new MyMultiAsyncTask<>(this).execute(param);
    }
    
    @Override
    public void doAsyncTask(int taskKind) {
        new MyMultiAsyncTask<>(this, taskKind).execute();
    }
    
    @Override
    public void doAsyncTask(int taskKind, Object... param) {
        new MyMultiAsyncTask<>(this, taskKind).execute(param);
    }
    
    @Override
    public void doAsyncUpdateUi(Object... param) {
        if (mHandler == null)
            mHandler = new Handler();
        mHandler.post(new UpdateResultsRunable<>(this, param));
    }

    @Override
    public ProgressDialog onPreExecute(int taskKind) {
        ProgressDialog dialog = genProgressDialog();
        return dialog;
        // 开启进度条
        //return ProgressDialog.show(this.getActivity(), "请稍等...", "正在处理中...", true);
    }

    @Override
    public Object doInBackground(int taskKind, Object... params) {
        return null;
    }

    @Override
    public void onProgressUpdate(int taskKind, Integer... values) {

    }

    @Override
    public void onClick(View arg0) {

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

    /**
     * 生成进度条对话框
     * @return
     */
    protected ProgressDialog genProgressDialog() {
        ProgressDialog dialog = DialogHelper.genProgressDialog(this.getActivity(), false, null);
        return dialog;
    }
    
    //这个方法最好让子类实现
    /*@Override
    public void onPostExecute(int taskKind, Object result, Object...params) {
        
    }*/


    
    public View getRootView() {
        return rootView;
    }
    
    /**
     * 获取所属activity的context
     * @return
     * @author zhangyz created on 2013-4-16
     */
    protected Context getContext() {
        return this.getActivity();
        /*if (rootView == null)
            rootView = getView();
        return rootView.getContext();//this.getActivity()*/
    }
    
    public BaseFragmentActive getMyActivity() {
        return (BaseFragmentActive)super.getActivity();
    }

    @Override
    public void doInBackgroundException(int taskKind, Throwable ex, Object... params) {
        Log.e("doInBackgroundException", ex.toString());
    }


}
