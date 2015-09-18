package com.mfh.comna.utils.sdimage;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.WindowManager;
import android.widget.ImageView;

import com.mfh.comna.bizz.material.logic.BitMapCacheService;
import com.mfh.comna.comn.logic.MyAsyncTask;
import com.mfh.comna.comn.logic.ServiceFactory;

/**
 * Created by Administrator on 2014/10/21.
 * 图片缓存工具类
 */
public class CacheUtil {

    private BitMapCacheService cache;
    private int width;

    public CacheUtil(Activity context) {
        cache = ServiceFactory.getService(BitMapCacheService.class, context);
        WindowManager wm = context.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
    }

    public void display(final ImageView imageView, final String path) {
        final Bitmap bitmap = cache.getBitmapFromMemCache(path);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }
        else {
            new MyAsyncTask<Void,Bitmap>(false){

                @Override
                protected Bitmap doInBackgroundInner(Void... params) {
                    Bitmap bitmap = compressImageFromFile(path);
                    bitmap.getByteCount();
                    if (bitmap == null)
                        return null;
                    bitmap = solveBitmap(bitmap);
                    return bitmap;
                }

                @Override
                protected void onPostExecuteInner(Bitmap result, Void... params) {
                    imageView.setImageBitmap(result);
                    cache.addBitmapToMemoryCache(path, result);
                }
            }.execute();
        }
    }

    private Bitmap compressImageFromFile(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float ww = width/8;//
        int be = 1;
//                if (w > h && w > ww) {
//                        be = (int) (newOpts.outWidth / ww);
//                    } else if (w < h && h > hh) {
//                        be = (int) (newOpts.outHeight / hh);
//                    }
        be = (int) (newOpts.outWidth / ww);
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置采样率

        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收
        newOpts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        //      return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
        //其实是无效的,大家尽管尝试
        return bitmap;
    }

    private Bitmap solveBitmap(Bitmap bitmap) {
        int mwidth = bitmap.getWidth();
        int mheight = bitmap.getHeight();
        int dx = Math.abs(mheight - mwidth);
        if (mheight >  mwidth)
        {
            bitmap = Bitmap.createBitmap(bitmap, 0, dx/2, mwidth, mwidth);
        }
        else
        {
            bitmap = Bitmap.createBitmap(bitmap,dx/2,0,mheight,mheight);
        }
        return bitmap;
    }
}
