package com.mfh.comna.bizz.msg.wx;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2015/5/12.
 */
public class WXMenu {
    public List<WXMenuData> menuList = new ArrayList<WXMenuData>();
    private boolean enabled;


    public WXMenu(){

    }

    public List<WXMenuData> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<WXMenuData> menuList) {
        this.menuList = menuList;
    }

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * */
    public void initWithSimulateData(){
        menuList = new ArrayList<WXMenuData>();
        enabled = false;

        int menuCount = new Random().nextInt(4);//随机产生0~3个主菜单(最多三个主菜单)
        if(menuCount > 0){
            List<WXMenuData> subList = new ArrayList<WXMenuData>();
            subList.add(new WXMenuData("子菜单1", "http://devmobile.manfenjiayuan.com/m/me/index.html", null));
            subList.add(new WXMenuData("子菜单2", "http://devmobile.manfenjiayuan.com/m/me/index.html", null));
            subList.add(new WXMenuData("子菜单3", "http://devmobile.manfenjiayuan.com/m/me/index.html", null));
            subList.add(new WXMenuData("子菜单4", "http://devmobile.manfenjiayuan.com/m/me/index.html", null));
            subList.add(new WXMenuData("子菜单5", "http://devmobile.manfenjiayuan.com/m/me/index.html", null));
            menuList.add(new WXMenuData("团购", "", subList));

            if(menuCount > 1){
                List<WXMenuData> subList2 = new ArrayList<WXMenuData>();
                subList2.add(new WXMenuData("子菜单1", "http://devmobile.manfenjiayuan.com/m/me/index.html", null));
                subList2.add(new WXMenuData("子菜单2", "http://devmobile.manfenjiayuan.com/m/me/index.html", null));
                menuList.add(new WXMenuData("物业", "", subList2));

                if(menuCount > 2){
                    menuList.add(new WXMenuData("我", "http://devmobile.manfenjiayuan.com/m/me/index.html", null));
                }
            }
            enabled = true;
        }
    }
}
