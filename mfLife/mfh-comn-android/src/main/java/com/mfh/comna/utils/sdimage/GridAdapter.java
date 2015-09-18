package com.mfh.comna.utils.sdimage;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.mfh.comna.R;
import com.mfh.comna.view.img.FineImgView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/10/21.
 * 图片列表的adapter
 */
public class GridAdapter extends BaseAdapter {

    public static final String DATA_IMAGE = "imageList";

    protected int width;
    private LayoutInflater inflater;
    protected Activity context;
    protected List<Image> images;
    protected List<String> checkImages;

    public GridAdapter(Activity context, List<Image> images, List<String> checkImages) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        WindowManager wm = context.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        this.images = images;
        this.checkImages = checkImages;
    }

    @Override
    public int getCount() {
        return images.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            // 获取行布局，对行布局中的控件进行设置赋值
            view = inflater.inflate(R.layout.list_image_item, null);
            holder.image = (FineImgView) view.findViewById(R.id.image);
            holder.imageCb = (CheckBox) view.findViewById(R.id.image_cb);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // 注意这里uri的使用,可以直接使用uri地址来构建一个image图片

        ViewGroup.LayoutParams params = holder.image.getLayoutParams();
        params.width = width/3;
        params.height = width/3;
        holder.image.setLayoutParams(params);
        fillData(position, holder);

        return view;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return 0;
        else
            return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    protected void fillData(final int position, final ViewHolder holder) {
        if (position == 0) {
            holder.image.getImageView().setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_button_add));
            holder.imageCb.setVisibility(View.GONE);
        }
        else {
            holder.image.getImageView().setImageResource(R.drawable.default_list_image);
//            cacheUtil.display(holder.image,images.get(position - 1).getPath());
            holder.image.setFao(FineImgView.getHeadImgFao());
            holder.image.setNeedSample(true);
            holder.image.setMaxWidth(width/9);
            holder.image.setSrc(images.get(position - 1).getPath());
            holder.imageCb.setVisibility(View.VISIBLE);
            holder.imageCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    images.get(position - 1).setCheck(isChecked);
                    if (isChecked) {
                        holder.imageCb.setBackgroundResource(R.drawable.checkbox_select);
                        if (checkImages != null) {
                            if (!checkImages.contains(images.get(position - 1).getPath()))
                                checkImages.add(images.get(position - 1).getPath());
                        }
                    }
                    else {
                        holder.imageCb.setBackgroundResource(R.drawable.checkbox_normal);
                        if (checkImages != null) {
                            if (checkImages.contains(images.get(position - 1).getPath()))
                                checkImages.remove(images.get(position - 1).getPath());
                        }
                    }
                }
            });
            holder.imageCb.setChecked(images.get(position - 1).isCheck());
        }
    }

    public void addData(Image image) {
        for (Image image1 : images){
            if (image1.getPath().equals(image.getPath()))
                return;
        }
        images.add(image);
    }

    public void addData(List<Image> images) {
        images.addAll(images);
    }

    public void removeData(String path) {
        List<Image> tempImages = images;
        for (Image image1 : images) {
            if (image1.getPath().equals(path)) {
                tempImages.remove(image1);
            }
        }
        images = tempImages;
    }

    public void removeData(List<String> paths) {
        List<Image> tempImages = new ArrayList<Image>();
        for (String path : paths) {
            for (Image image1 : images) {
                if (image1.getPath().equals(path)) {
                    tempImages.add(image1);
                }
            }
        }
        images.removeAll(tempImages);
    }

    protected static class ViewHolder {
        public CheckBox imageCb;
        public FineImgView image;
    }

}
