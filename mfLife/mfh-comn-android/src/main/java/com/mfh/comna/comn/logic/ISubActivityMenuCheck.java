package com.mfh.comna.comn.logic;

import android.view.Menu;
/**
 * 子activity使用，声明本activity中对哪些菜单可见
 * 
 * @author zhangyz created on 2013-5-17
 * @since Framework 1.0
 */
public interface ISubActivityMenuCheck {
    public boolean OnOptionsMenuItemCheck(int groupId, int menuId);
    
    /**
     * 对指定菜单的分组情况整体设置可见性
     * @param menu
     * @return true 代表该方法调用有效，无需再逐个设置OnOptionsMenuItemCheck
     * @author zhangyz created on 2013-4-17
     */
    public boolean setGroupVisible(Menu menu);
}
