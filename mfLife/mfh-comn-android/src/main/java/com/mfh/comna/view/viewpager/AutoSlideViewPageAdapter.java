package com.mfh.comna.view.viewpager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public abstract class AutoSlideViewPageAdapter extends PagerAdapter implements IAutoSlideViewPagerAdapter{

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
		object = null;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		position = position % getPageCount();
		return getCurrentPageTitle(position);
	}
	
	public int getPosition(int position) {
		return position % getPageCount();
	}

	/**
	 * Get description the title of the current page
	 * @param position
	 * @return
	 */
	public abstract CharSequence getCurrentPageTitle(int position);
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		position = position % getPageCount();
		View view = instantiatePageItem(position);
		container.addView(view);
		return view;
	}

}
