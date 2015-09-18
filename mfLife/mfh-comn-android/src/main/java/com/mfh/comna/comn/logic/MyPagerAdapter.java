package com.mfh.comna.comn.logic;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;

/**
 * 支持tabhost、viewPager和fragment组合的adapter
 * 
 * @author zhangyz created on 2013-4-17
 * @since Framework 1.0
 */
public class MyPagerAdapter extends PagerAdapter 
    implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
    private final Activity mContext;
    private final TabHost mTabHost;
    private final ViewPager mViewPager;
    private final List<TabInfo> mTabs = new ArrayList<TabInfo>();

    static final class TabInfo {
        private Object tag;
        private final Class<?> clss;
        private final Bundle args;

        TabInfo(Class<?> _class, Bundle _args) {
            clss = _class;
            args = _args;
        }

        public Object getTag() {
            return tag;
        }

        public Class<?> getClss() {
            return clss;
        }

        public Bundle getArgs() {
            return args;
        }
    }

    static class DummyTabFactory implements TabHost.TabContentFactory {

        private final Context mContext;

        public DummyTabFactory(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }

    public MyPagerAdapter(Activity activity, TabHost tabHost, ViewPager pager) {
        super();
        mContext = activity;
        mTabHost = tabHost;
        mViewPager = pager;
        mTabHost.setOnTabChangedListener(this);
        mViewPager.setAdapter(this);
        mViewPager.setOnPageChangeListener(this);
    }

    public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
        //tabSpec.setContent(new DummyTabFactory(mContext));
        tabSpec.setContent(new Intent(mContext, clss));
        TabInfo info = new TabInfo(clss, args);
        mTabs.add(info);
        mTabHost.addTab(tabSpec);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    /*@Override
    public Object getItem(int position) {
        TabInfo info = mTabs.get(position);
        Fragment fmt = (Fragment) info.tag;
        if (fmt == null) {
            fmt = Fragment.instantiate(mContext, info.clss.getName(), info.args);
            info.tag = fmt;
        }
        return fmt;
    }*/

    @Override
    public CharSequence getPageTitle(int position) {
        // TabInfo info = mTabs.get(position);
        return "";
    }

    @Override
    public void onTabChanged(String tabId) {
        int position = mTabHost.getCurrentTab();
        mViewPager.setCurrentItem(position);
        //mContext.OnFragmentSelected(getItem(position));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, 
            int positionOffsetPixels) {        
    }

    @Override
    public void onPageSelected(int position) {
        // Unfortunately when TabHost changes the current tab, it kindly
        // also takes care of putting focus on it when not in touch mode.
        // The jerk.
        // This hack tries to prevent this from pulling focus out of our
        // ViewPager.
        TabWidget widget = mTabHost.getTabWidget();
        int oldFocusability = widget.getDescendantFocusability();
        widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mTabHost.setCurrentTab(position);
        widget.setDescendantFocusability(oldFocusability);

        //mContext.OnFragmentSelected(getItem(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return false;
    }
}
