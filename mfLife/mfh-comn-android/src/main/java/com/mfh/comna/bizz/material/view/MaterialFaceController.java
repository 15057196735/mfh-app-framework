package com.mfh.comna.bizz.material.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mfh.comna.R;
import com.mfh.comna.utils.DensityUtil;
import com.mfh.comna.view.img.AutoSlideViewPager;

/**
 * Created by Caij on 2014/10/12.
 * 表情素材显示
 */
public class MaterialFaceController extends RelativeLayout{

    private final int PAGE_SIZE = 20;
    AutoSlideViewPager pointAndViewPage;

    private Context context;
    private CallBack callBack;

    public MaterialFaceController(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public MaterialFaceController(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public MaterialFaceController(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    private void init() {
        View.inflate(context, R.layout.chat_face_view, this);
        pointAndViewPage = (AutoSlideViewPager) this.findViewById(R.id.pav_face);
        pointAndViewPage.setAdapter(new MyPageAdapter());
    }

    public void setPage(int page) {
        pointAndViewPage.setCurrentItem(page);
    }

    private class MyPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(context,R.layout.chat_item_grid,null);
            GridView gridView = (GridView) view.findViewById(R.id.face_item_gv);
            gridView.setAdapter(new MyGridAdapter(position));
            container.addView(view);
            return view;
        }
    }

    private class MyGridAdapter extends BaseAdapter {
        private int page;

        public MyGridAdapter(int page) {
            this.page = page;
        }

        @Override
        public int getCount() {
            return 21;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View faceView = View.inflate(context,R.layout.face_item,null);
            ImageView imageView = (ImageView) faceView.findViewById(R.id.face_item);
            int id = 0;
            if (i != 20) {
                id = context.getResources().getIdentifier("smiley_" + (page * PAGE_SIZE + i), "drawable", context.getApplicationInfo().packageName);
                imageView.setBackgroundResource(id);
            }
            else {
                id = 20;
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                params.width = DensityUtil.dip2px(context,25);
                params.height = DensityUtil.dip2px(context,20);
                imageView.setLayoutParams(params);
                imageView.setBackgroundResource(R.drawable.chat_back);
            }
            final int finalId = id;
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.faceImageOnClick(finalId);
                }
            });
            return faceView;
        }
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack{
        public void faceImageOnClick(int id);
    }
}
