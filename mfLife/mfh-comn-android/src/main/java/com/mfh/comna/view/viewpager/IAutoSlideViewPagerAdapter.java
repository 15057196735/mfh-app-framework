package com.mfh.comna.view.viewpager;

import android.view.View;


public interface IAutoSlideViewPagerAdapter {

	/**
	 * Take the total number of pages
	 * @return total
	 */
	public int getPageCount();
	
	/**
	 * Get a view of the page
	 * @param container
	 * @param position
	 * @return
	 */
	public View instantiatePageItem(int position);
	
}
