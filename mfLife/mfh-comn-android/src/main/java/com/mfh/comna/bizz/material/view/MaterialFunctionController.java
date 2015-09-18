package com.mfh.comna.bizz.material.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mfh.comna.R;
import com.mfh.comna.view.img.AutoSlideViewPager;

import java.util.List;

/**
 * Created by zhangzn on 2015/04/07.
 * 功能素材显示
 */
public class MaterialFunctionController extends RelativeLayout{

    private final int PAGE_SIZE = 8;
    AutoSlideViewPager pointAndViewPage;

    private int pageSize = 0;//from 1
//    List<View> functionGridViewList = new ArrayList<View>();;
    private List<Option> functionData;

    private Context context;
    private CallBack callBack;

    public MaterialFunctionController(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public MaterialFunctionController(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public MaterialFunctionController(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    private void init() {
        View.inflate(context, R.layout.chat_material_view, this);
        pointAndViewPage = (AutoSlideViewPager) this.findViewById(R.id.pav_matetial);
        pointAndViewPage.setAdapter(new MyPageAdapter());

//        pointAndViewPage.setPointImageResId(R.drawable.dot_unselected,
//                R.drawable.dot_selected);
//        pointAndViewPage.setPointWidthAndMargin(20, 8);
//        autoSlideViewPager.setAutoScrollDurationFactor(8);
//        autoSlideViewPager.startSlide();
    }

    public void setPage(int page) {
        pointAndViewPage.setCurrentItem(page);
    }

    private class MyPageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
//            Log.d("Nat: MyPageAdapter", String.format("cound %d", pageSize));
            return pageSize;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            System.out.println(container + "  " + position + "  " + object);
//            container.removeView(functionGridViewList.get(position));
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(context,R.layout.material_item_grid,null);
            GridView gridView = (GridView) view.findViewById(R.id.material_item_gv);

            gridView.setAdapter(new MyGridAdapter(functionData.subList(position * 8,
                    ((position + 1) * 8) > functionData.size() ? functionData.size() : ((position + 1) * 8))));
            container.addView(view);
            return view;
        }
    }

    private class MyGridAdapter extends BaseAdapter {
        private List<Option> data;
        private int size = 0;

//        private LayoutInflater inflater;

        class ViewHolder {
            public ImageView iconImageView;
            public TextView nameTextView;
        }

        public MyGridAdapter(List<Option> list) {
            this.data = list;
            this.size = this.data.size();
        }

        @Override
        public int getCount() {
            return this.size;
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final Option option = data.get(i);
            ViewHolder viewHolder = null;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = View.inflate(context, R.layout.material_function_item, null);
                viewHolder.iconImageView = (ImageView) view.findViewById(R.id.iconImageView);
                viewHolder.nameTextView = (TextView) view.findViewById(R.id.nameTextView);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.iconImageView.setImageDrawable(option.getIconDrawable());
            viewHolder.nameTextView.setText(option.getName());

            final int index = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.materialSelected(option.getId());
                }
            });
            return view;
        }
    }

    public List<Option> getFunctionData() {
        return functionData;
    }

    public void setFunctionData(List<Option> functionData) {
        this.functionData = functionData;
        this.pageSize = (functionData.size() % 8 == 0 ? functionData.size() / 8 : (functionData.size() / 8) + 1);

        // java.lang.IllegalStateException: The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged!
//        pageAdapter.notifyDataSetChanged();
//        pageAdapter = new MyPageAdapter();
        pointAndViewPage.setAdapter(new MyPageAdapter());
    }

//    private class FunctionAdapter extends BaseAdapter {
//        private List<Option> data;
//        private int size = 0;
//        private LayoutInflater inflater;
//
//        public FunctionAdapter(Context context, List<Option> list) {
//            this.inflater = LayoutInflater.from(context);
//            this.data = list;
//            this.size = list.size();
//        }
//
//        @Override
//        public int getCount() {
//            return this.size;
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return data.get(i);
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return i;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//            Option option = data.get(i);
//            ViewHolder viewHolder = null;
//            if (view == null) {
//                viewHolder = new ViewHolder();
//                view = inflater.inflate(R.layout.material_function_item, null);
//                viewHolder.iconImageView = (ImageView) view.findViewById(R.id.iconImageView);
//                viewHolder.nameTextView = (TextView) view.findViewById(R.id.nameTextView);
//                view.setTag(viewHolder);
//            } else {
//                viewHolder = (ViewHolder) view.getTag();
//            }
//            viewHolder.iconImageView.setImageDrawable(option.getIconDrawable());
//            viewHolder.nameTextView.setText(option.getName());
//
//            final int index = i;
//            view.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    callBack.materialSelected(pointAndViewPage.getCurrentItem(), index);
//                }
//            });
//            return view;
//        }
//
//        class ViewHolder {
//            public ImageView iconImageView;
//            public TextView nameTextView;
//        }
//    }



    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack{
        void materialSelected(int optionId);
    }

}
