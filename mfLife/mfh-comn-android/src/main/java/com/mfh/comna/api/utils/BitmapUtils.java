package com.mfh.comna.api.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

/**
 * Created by Administrator on 2015/5/8.
 */
public class BitmapUtils {
    /**
     * 转换图片成圆形
     * @param bitmap
     *            传入Bitmap对象
     * @return bitmap
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;

            left = 0;
            top = 0;
            right = width;
            bottom = width;

            height = width;

            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;

            float clip = (width - height) / 2;

            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;

            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);// 设置画笔无锯齿

        canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas

        // 以下有两种方法画圆,drawRounRect和drawCircle
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
        // canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置两张图片相交时的模式
        canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

        return output;
    }

    public static Bitmap decodeBitmapFromFile(File paramFile)
    {
        BitmapFactory.Options localOptions = new BitmapFactory.Options();
        localOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(paramFile.getAbsolutePath(), localOptions);
    }

    public static Bitmap decodeBitmapFromFile(File paramFile, int paramInt1, int paramInt2)
    {
        BitmapFactory.Options localOptions = new BitmapFactory.Options();
        localOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(paramFile.getAbsolutePath(), localOptions);
        localOptions.inSampleSize = (int)Math.floor(Math.max(localOptions.outHeight / paramInt2, localOptions.outWidth / paramInt1));
        localOptions.inJustDecodeBounds = false;
        Bitmap localBitmap = BitmapFactory.decodeFile(paramFile.getAbsolutePath(), localOptions);
        if (localBitmap != null){
            while (true)
            {
                if ((localBitmap.getWidth() <= paramInt1) && (localBitmap.getHeight() <= paramInt2)){
                    return localBitmap;
                }
                double d = Math.max(localBitmap.getHeight() / paramInt2, localBitmap.getWidth() / paramInt1);
                localBitmap = Bitmap.createScaledBitmap(localBitmap, (int)(localBitmap.getWidth() / d), (int)(localBitmap.getHeight() / d), true);
            }
        }

        return localBitmap;
    }

    public static Bitmap decodeBitmapFromResource(Context paramContext, int paramInt1, int paramInt2, int paramInt3)
    {
        BitmapFactory.Options localOptions = new BitmapFactory.Options();
        localOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(paramContext.getResources(), paramInt1, localOptions);
        localOptions.inSampleSize = (int)Math.floor(Math.max(localOptions.outHeight / paramInt3, localOptions.outWidth / paramInt2));
        localOptions.inJustDecodeBounds = false;
        Bitmap localBitmap = BitmapFactory.decodeResource(paramContext.getResources(), paramInt1, localOptions);
        if (localBitmap != null){
            while (true)
            {
                if ((localBitmap.getWidth() <= paramInt2) && (localBitmap.getHeight() <= paramInt3))
                    return localBitmap;
                double d = Math.max(localBitmap.getHeight() / paramInt3, localBitmap.getWidth() / paramInt2);
                localBitmap = Bitmap.createScaledBitmap(localBitmap, (int)(localBitmap.getWidth() / d), (int)(localBitmap.getHeight() / d), true);
            }
        }

        return localBitmap;
    }

    public static Bitmap decodeBitmapFromStream(InputStream paramInputStream) {
        return BitmapFactory.decodeStream(paramInputStream);
    }

    /**
     * Bitmap.CompressFormat.PNG
     * */
    public static byte[] bmpToByteArray(final Bitmap bmp, Bitmap.CompressFormat compressFormat, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(compressFormat, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
