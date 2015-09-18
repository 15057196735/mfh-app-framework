package com.mfh.comna.utils.sdimage;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.mfh.comna.R;
import com.mfh.comna.utils.DensityUtil;
import com.mfh.comna.utils.FullScreenActivity;
import com.mfh.comna.view.BaseComnActivity;
import com.mfh.comna.view.img.FineImgView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014/10/18.
 */
public class EditImageActivity extends BaseComnActivity {

    private GridView addedImageslayout = null;
    private MyAdapter adapter;
    private List<Image> images;
    private List<String> imagePaths;

    @Override
    protected void initActionBar(ActionBar actionBar) {
        super.initActionBar(actionBar);
        actionBar.setIcon(R.drawable.white_logo);
        actionBar.setTitle("编辑图片");
        actionBar.setCustomView(R.layout.delete_image);
        actionBar.setDisplayShowCustomEnabled(true);

        LinearLayout linearLayout = (LinearLayout) actionBar.getCustomView().findViewById(R.id.ll_delete);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(GridAdapter.DATA_IMAGE, (Serializable) imagePaths);
                intent.putExtras(bundle);
                setResult(2,intent);
                finish();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addedImageslayout = (GridView) findViewById(R.id.gv);
        images = (List<Image>) getIntent().getExtras().get(GridAdapter.DATA_IMAGE);
        imagePaths = new ArrayList<String>();
        adapter = new MyAdapter(this, images, imagePaths);
        addedImageslayout.setAdapter(adapter);

    }

    private class MyAdapter extends GridAdapter{

        public MyAdapter(Activity context, List<Image> images, List<String> checkImages) {
            super(context, images, checkImages);
        }

        @Override
        public int getItemViewType(int position) {
            return 1;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        protected void fillData(final int position,final ViewHolder holder) {
//            cacheUtil.display(holder.image,images.get(position).getPath());
            holder.image.setFao(FineImgView.getHeadImgFao());
            holder.image.setNeedSample(true);
            holder.image.setMaxWidth(width/9);
            holder.image.setSrc(images.get(position).getPath());
            holder.imageCb.setVisibility(View.VISIBLE);
            holder.imageCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    images.get(position).setCheck(isChecked);
                    if (isChecked) {
                        holder.imageCb.setBackgroundResource(R.drawable.checkbox_select);
                        if (!checkImages.contains(images.get(position).getPath()))
                            checkImages.add(images.get(position).getPath());
                    }
                    else {
                        holder.imageCb.setBackgroundResource(R.drawable.checkbox_normal);
                        if (checkImages.contains(images.get(position).getPath()))
                            checkImages.remove(images.get(position).getPath());
                    }
                }
            });
            holder.imageCb.setChecked(images.get(position).isCheck());
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditImageActivity.this, ZoomImageActivity.class);
                    intent.putExtra("imagePath", images.get(position).getPath());
                    startActivity(intent);
                }
            });
        }
    }

}
