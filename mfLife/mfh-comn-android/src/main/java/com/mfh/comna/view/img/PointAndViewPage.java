package com.mfh.comna.view.img;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mfh.comna.R;

/**
 * -----------------------------------------------------------------
 *
 * Copyright (C) 2014  春晨网络有限公司
 *
 * Description：自定义带描述和滑动点的ViewPager
 * 
 * @version: v1.0.0 
 *
 * @author: Caij
 *
 * Create Date: 2014年8月22日 
 *
 * -----------------------------------------------------------------
 */
public class PointAndViewPage extends RelativeLayout implements OnPageChangeListener, View.OnClickListener{
	
	private ViewPager item_viewpage;
	private TextView item_tv_desc;
	private LinearLayout item_llayout_point;
	private Context context;
	private int pointNormal = 0;	//非选取点的背景图片
	private int pointSelect = 0;  //当前页面选取时，点的背景图片
	private int prePointPositoin; //上一个点的positoin
	private boolean isAutoChange = false; //这是是否自动滑动
	private long autoChangeTime = 4000; //自动滑动的时间间隔,默认4秒
	private MyHandler handler = new MyHandler();
	private boolean isRunning = true; //在界面销毁时设置为false， 停止hanlder的消息发送
	private boolean isEnd;  //viewpager是否已经跳转到最后一页
	
//	public PointAndViewPage(Context context) {
//		super(context);
//		this.context = context;
//		initView();
//	}
	
	public PointAndViewPage(Context context,LayoutParams params) {
		super(context);
		this.context = context;
		initView();
		this.setLayoutParams(params);
	}

	public PointAndViewPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	/**
	 * 初始化所以控件
	 * @param
	 */
	private void initView() {
		View.inflate(context, R.layout.point_and_viewpage_item, this);
		item_viewpage = (ViewPager) this.findViewById(R.id.item_viewpage);
		item_tv_desc = (TextView) this.findViewById(R.id.item_tv_desc);
		item_llayout_point = (LinearLayout) this.findViewById(R.id.item_llayout_point);
		
		item_viewpage.setOnPageChangeListener(this);
	}
	
	/**
	 * 设置viewpager的adapter，而且动态将点添加到布局中,在调用这个方法之前一定要把需要的参数设置
	 * @param adapter
	 */
	public void setPageAdapter(PagerAdapter adapter){
		item_viewpage.setAdapter(adapter);
		if(pointNormal != 0 && pointSelect !=0) {
			for (int i = 0; i < adapter.getCount(); i++) {
				ImageView imageView = new ImageView(context);
				//设置点图片的参数
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				params.setMargins(4, 4, 4, 4);
				imageView.setLayoutParams(params);
				if(i == 0) {
					imageView.setBackgroundResource(pointSelect);
				}else {
					imageView.setBackgroundResource(pointNormal);
				}
				imageView.setOnClickListener(this);
				item_llayout_point.addView(imageView);
				
			}
		}
		
		if(isAutoChange) {
			handler.sendEmptyMessageDelayed(0, autoChangeTime);
		}
	}
	
	public void setNormalAndSelectPiontBackground(int nomal, int select) {
		this.pointNormal = nomal;
		this.pointSelect = select;
	}
	
	public void setAutoChangeTime(long time) {
		this.autoChangeTime = time;
	}
	
	private void changeState(boolean isSelect, int position) {
		ImageView imageView = (ImageView) item_llayout_point.getChildAt(position);
		if(pointNormal != 0 && pointSelect != 0){
			if(isSelect) {
				imageView.setBackgroundResource(pointSelect);
			}else {
				imageView.setBackgroundResource(pointNormal);
			}
		}
	}
	
	public void setText(String desc) {
		item_tv_desc.setText(desc);
	}
	
	
	public void setAutoChange(boolean isAutoChange) {
		this.isAutoChange = isAutoChange;
	}

	public void setIsRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	
	public ViewPager getViewPager() {
		return item_viewpage;
	}

	@SuppressLint("HandlerLeak") 
	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if(item_viewpage.getCurrentItem() + 2 > item_viewpage.getChildCount()) {
				isEnd = true;
			}
			
			if(item_viewpage.getCurrentItem()-1 <0) {
				isEnd = false;
			}
			
			if(isEnd){
				item_viewpage.setCurrentItem(item_viewpage.getCurrentItem() - 1);
			}else {
				item_viewpage.setCurrentItem(item_viewpage.getCurrentItem() + 1);
			}
			if(isRunning) {
				handler.sendEmptyMessageDelayed(0, autoChangeTime);
			}
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int arg0) {
		changeState(false,prePointPositoin);
		changeState(true,arg0);
		prePointPositoin = arg0;
	}

	@Override
	public void onClick(View v) {
		int count = item_llayout_point.getChildCount();
		for (int i = 0; i < count; i++) {
			if(v == item_llayout_point.getChildAt(i)) {
				changeState(false, prePointPositoin);
				changeState(true, i);
				item_viewpage.setCurrentItem(i);
				prePointPositoin=i;
			}
		}
	}
	
}
