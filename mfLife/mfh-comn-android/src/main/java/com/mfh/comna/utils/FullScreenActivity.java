package com.mfh.comna.utils;

import android.app.ActionBar;
import android.os.Bundle;

import com.mfh.comna.R;
import com.mfh.comna.view.BaseComnActivity;
import com.mfh.comna.view.img.TouchImageView;

import net.tsz.afinal.FinalBitmap;


/**
 *全屏显示一张图片
 * Created by 李潇阳 on 14-6-23.
 */
public class FullScreenActivity extends BaseComnActivity{

    protected  TouchImageView imageView1;

    @Override
    protected void initActionBar(ActionBar actionBar) {
        super.initActionBar(actionBar);
        actionBar.setIcon(R.drawable.white_logo);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_fullscreen_image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        imageView1 = (TouchImageView)findViewById(R.id.full_screen_image);
        imageView1.setMaxZoom(3);
        setImage();
    }

    protected void setImage() {
        Bundle bundle = this.getIntent().getExtras();
        String imageUrl = bundle.getString("imgUrl");
        FinalBitmap.create(this).display(imageView1,imageUrl);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
