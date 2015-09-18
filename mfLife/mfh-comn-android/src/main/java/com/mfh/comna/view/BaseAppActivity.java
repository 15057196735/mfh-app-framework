package com.mfh.comna.view;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.mfh.comn.config.UConfig;
import com.mfh.comna.bizz.BizApplication;
import com.mfh.comna.R;

import net.tsz.afinal.FinalDb;


/**
 * 可作为系统一级模块的基类,可作为最顶层Acitivity.
 * 而BaseComnActivity往往是二级Acitivity
 * 本类继承于BaseFragmentActive,
 * 因此BaseFragmentActive既可以作为一级Acitivty也可以作为二级Activity。
 * @author zhangyz created on 2013-4-19
 * @since Framework 1.0
 */
public abstract class BaseAppActivity extends BaseFragmentActive implements DialogInterface.OnClickListener, 
    PopupMenu.OnMenuItemClickListener{
    protected static List<BaseAppActivity> activityList = new LinkedList<>();
    @SuppressLint("UseSparseArrays")
    protected static Map<Integer, Class<?>> menuActivityMap = new HashMap<>();
    
    private static void addActivity(BaseAppActivity activity){
        activityList.add(activity);
    }
    
    /**
     * 
     * @param menuId
     * @param acitvityClass
     * @author zhangyz created on 2013-6-16
     */
    public static void addActivity(Integer menuId, Class<?> acitvityClass) {
        menuActivityMap.put(menuId, acitvityClass);
    }

    /**
     * 关闭数据库
     */
    protected void exitDb() {
        String dbName = BizApplication.getUconfig().getDomain(UConfig.CONFIG_COMMON).getString("app.db.name", "mfh.db");
        FinalDb db = FinalDb.getDb(dbName);
        if (db != null) {
            db.close();
        }
    }

    /**
     * 完整退出程序
     * 
     * @author zhangyz created on 2013-4-19
     */
    protected void exit() {
        for(BaseAppActivity activity : activityList){
            activity.finish();
        }
        exitDb();
        System.exit(0);
    }

    private boolean bHaveCanced = false;//是否已经按过退出键
    private ImageView homeButton = null;
    protected int mSysMenuId = -1;//当前选中的哪个菜单项
    
    protected abstract int getMenuId();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeButton = (ImageView)this.findViewById(android.R.id.home);
        //if (homeButton != null) //用单击弹出了，不用上下文菜单了。
        //    registerForContextMenu(homeButton);
        mSysMenuId = this.getMenuId();        
        addActivity(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        if (v.getId() == android.R.id.home) {//禁用了
            getMenuInflater().inflate(R.menu.sys_menu, menu);
        }
        else
            super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {          
                if (!backUpFragment()) {
                    PopupMenu popup = new PopupMenu(this, homeButton);
                    MenuInflater inflater = popup.getMenuInflater();  
                    inflater.inflate(R.menu.sys_menu, popup.getMenu());  
                    popup.setOnMenuItemClickListener(this);
                    
                    Menu menu = popup.getMenu();
                    MenuItem menItem;
                    for (int ii = 0; ii < menu.size(); ii++) {
                        menItem = menu.getItem(ii);
                        if (menItem.getItemId() == mSysMenuId && menItem.getItemId() != R.id.exitAppmenu) {
                            menItem.setEnabled(false);
                        }
                    }                    
                    popup.show();  
                    //this.openContextMenu(findViewById(android.R.id.home));
                }
            }
        }
        return true;
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Intent intent = null;
        int itemId = item.getItemId();
        if (itemId == R.id.exitAppmenu) {
            this.showYesNoDialog("您真的要退出系统吗?", this);
        }
        else {
            Class<?> activityClass = menuActivityMap.get(itemId);
            if (activityClass != null) {
                intent = new Intent(this, activityClass);
                Object me = this;
                Class<?> curClass = me.getClass();
                if (!(intent.getClass().equals(curClass) )) {
                    this.startActivity(intent);
                }
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return onContextItemSelected(item);
    }

    @Override
    public void onClick(DialogInterface arg0, int which) {
        if (DialogInterface.BUTTON_POSITIVE == which) {     
            exit();
        }        
    }
    
    @Override 
    public void onBackPressed() { 
        if (!bHaveCanced) {
            this.showHint("再按一次退出程序!");
            bHaveCanced = true;
        }
        else
            exit();
    }
}
