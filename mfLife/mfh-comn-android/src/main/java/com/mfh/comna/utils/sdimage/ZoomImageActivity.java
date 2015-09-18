package com.mfh.comna.utils.sdimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.WindowManager;

import com.mfh.comna.utils.FullScreenActivity;

/**
 * Created by Administrator on 2014/10/22.
 */
public class ZoomImageActivity extends FullScreenActivity{
    @Override
    protected void setImage() {
        String path = getIntent().getStringExtra("imagePath");
        WindowManager wm = getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        Bitmap bitmap = compressImageFromFile(path,width,height);
        imageView1.setImageBitmap(bitmap);
    }

    private Bitmap compressImageFromFile(String srcPath, int width, int height) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        int be = 1;
        if (newOpts.outWidth/width > newOpts.outHeight/height){
            be = newOpts.outWidth/width;
        }
        else {
            be = newOpts.outHeight/height;
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置采样率

        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        //      return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
        //其实是无效的,大家尽管尝试
        return bitmap;
    }
}
