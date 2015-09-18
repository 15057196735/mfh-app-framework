package com.mfh.comna.api.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mfh.comna.bizz.msg.MsgConstants;
import com.mfh.comn.utils.FileUtils;
import com.mfh.comn.utils.IOUtils;
import com.mfh.comna.R;
import com.mfh.comna.comn.database.dao.BaseFao;
import com.mfh.comna.view.img.FineImgView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图像文件的相关工具类
 * Created by Administrator on 14-5-16.
 */
public class ImageUtil {
    private static long defaultSize = 400 * 1024;
    public final static int DEF_CROP = 200;//默认裁剪尺寸

    /**
     * 将uri转换成文件
     * @param resolver
     * @param uri
     * @return
     */
    public static File uriToFile(ContentResolver resolver, Uri uri) {
        Cursor cursor = null;
        try {
            cursor = resolver.query(uri, null,null, null, null);
            if (cursor != null){
                cursor.moveToFirst();
                String imgPath = cursor.getString(1); // 图片文件路径
                //String imgSize = cursor.getString(2); // 图片大小
            /*String imgNo = cursor.getString(0); // 图片编号
            String imgName = cursor.getString(3); // 图片文件名*/
                return new File(imgPath);
            }else{
                return null;
            }
        }
        finally {
            if (cursor != null)
                cursor.close();
        }
    }

    /**
     * 将uri转换成文件
     * @param resolver
     * @param uri
     * @return
     */
    public static String uriToPath(ContentResolver resolver, Uri uri) {
        Cursor cursor = null;
        try {
            cursor = resolver.query(uri, null,null, null, null);
            if (cursor != null){
                cursor.moveToFirst();
                String imgPath = cursor.getString(1); // 图片文件路径
                //String imgSize = cursor.getString(2); // 图片大小
            /*String imgNo = cursor.getString(0); // 图片编号
            String imgName = cursor.getString(3); // 图片文件名*/
                return imgPath;
            }else{
                return null;
            }

        }
        finally {
            if (cursor != null)
                cursor.close();
        }
    }

    /**
     * bitMap对象转换为文件
     * @param bitmap
     * @return
     */
    public static File bitMapToFile(Bitmap bitmap, String... tempDir) {
        if (bitmap == null)
            throw new RuntimeException("位图对象为空!");
        File fileTmp = BaseFao.getNewTempFile(Bitmap.CompressFormat.JPEG.name(), tempDir);
        bitMapToFile(bitmap, fileTmp);
        return fileTmp;
    }

    /**
     * 按质量压缩，存储到新文件中
     * @param image
     * @param maxSize
     * @param destFile
     */
    public static void compressImageByQualityToFile(Bitmap image, long maxSize, File destFile) {
        if (image.getByteCount() < maxSize)
            bitMapToFile(image, destFile);
        else {
            InputStream isBm = compressImageByQualityInner(image, maxSize);
            IOUtils.copy(isBm, destFile);
        }
    }

    /**
     * 按质量压缩,返回新位图
     * @param image 原始位图
     * @param maxSize 压缩后最大大小，单位为字节
     * @return
     */
    public static Bitmap compressImageByQuality(Bitmap image, long maxSize) {
        if (image == null || image.getByteCount() < maxSize)
            return image;
        InputStream isBm = compressImageByQualityInner(image, maxSize);
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        try {
            if(isBm != null){
                isBm.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 质量压缩内部方法
     * @param image
     * @param maxSize
     * @return
     */
    private static InputStream compressImageByQualityInner(Bitmap image, long maxSize) {
        int options = 90;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            while ( baos.toByteArray().length > maxSize) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                options -= 10;//每次都减少10
                baos.reset();//重置baos即清空baos
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中,100表示不压缩
            }
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
            baos.close();
            return isBm;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 图片文件按比例大小压缩
     * @param file
     * @param maxHh
     * @param maxWw
     * @return
     */
    public static Bitmap compressImageBySize(File file, float maxWw, float maxHh) {
        return compressImageBySize(file.getAbsolutePath(), maxWw);
    }

    /**
     * 图片文件按比例大小压缩
     * @param srcPath
     * @param maxWw
     * @return
     */
    public static Bitmap compressImageBySize(String srcPath, float maxWw) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inJustDecodeBounds = true;

            double dh = options.outHeight;
            double dw = options.outWidth;
            int sample = calcSampleScale(options.outWidth, options.outHeight, maxWw, (int) (maxWw * (dh / dw)));
            options.inSampleSize = sample;
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(srcPath, options);
        }
        catch (Throwable e) {
            return null;
        }
    }

    /**
     * 图片位图按比例大小压缩方法
     * @param image
     * @return
     */
    public static Bitmap compressImageBySize(Bitmap image, float maxWw, float maxHh) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();

        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inJustDecodeBounds = false;
        newOpts.inSampleSize = calcSampleScale(newOpts.outWidth, newOpts.outHeight, maxWw, maxHh);//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return bitmap;
    }

    /**
     * 综合压缩位图，先根据尺寸大小压缩，再根据质量压缩。优先保证最大像素点。
     * @param image
     * @param maxSize 压缩后最大像素点（字节）
     * @param maxHh 压缩后最大高度
     * @param maxWw 压缩后最大宽度
     * @return
     */
    public static Bitmap compressImage(Bitmap image, long maxSize, float maxWw, float maxHh) {
        int imgCut = image.getByteCount();
        if (imgCut < maxSize)
            return image;
        image = compressImageBySize(image, maxWw, maxHh);
        return compressImageByQuality(image, maxSize);//压缩好比例大小后再进行质量压缩
    }

    /**
     * 综合压缩文件，先根据尺寸大小压缩，再根据质量压缩。优先保证最大像素点。
     * @param file
     * @param maxSize 压缩后最大像素点（字节）
     * @param maxHh 压缩后最大高度
     * @param maxWw 压缩后最大宽度
     * @return 压缩后位图
     */
    public static Bitmap compressImage(File file, long maxSize, float maxWw, float maxHh) {
        Bitmap image;
        if (file.length() > defaultSize)
            image = compressImageBySize(file, maxWw, maxHh);
        else
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        return compressImageByQuality(image, maxSize);//压缩好比例大小后再进行质量压缩
    }

    public static void compressFileToFile(File file, long maxSize, float maxWw, float maxHh, File destFile) {
        if (file.length() > defaultSize) {
            Bitmap image = compressImageBySize(file, maxWw, maxHh);
            compressImageByQualityToFile(image, maxSize, destFile);//压缩好比例大小后再进行质量压缩
        }
        else {
            FileUtils.copyFile(file, destFile);
        }
    }

    /**
     * 按照目前手机上的默认大小标准压缩图片(高度：800， 宽度：480，最大像素：500k).优先保证最大像素点。
     * @param image 原始位图
     * @return 压缩后位图
     */
    public static Bitmap compressImage(Bitmap image) {
        return compressImage(image, defaultSize, 800f, 480f);
    }

    /**
     * 按照目前手机上的默认大小标准压缩图片(高度：800， 宽度：480，最大像素：500k字节).优先保证最大像素点。
     * @param file 原始文件
     * @return 压缩后位图
     */
    public static Bitmap compressFile(File file) {
        return compressImage(file, defaultSize, 800f, 480f);
    }

    /**
     * 直接将原始文件压缩，并覆盖原始文件。
     * @param file
     */
    public static void compressFileToSelf(File file) {
        if (file.length() < defaultSize)
            return;
        Bitmap bitmap = compressImage(file, defaultSize, 800f, 480f);
        bitMapToFile(bitmap, file);
    }

    /**
     * 将原始文件压缩，并输出到新文件中；即使没有压缩，也输出到该文件。
     * @param file
     * @param destFile 新文件，可以为空，默认放到用户程序的temp/目录下
     * @return
     */
    public static void compressFileToFile(File file, File destFile) {
        if (destFile == null) {
            destFile = BaseFao.getNewTempFile(Bitmap.CompressFormat.JPEG.name(), "temp");
        }
        compressFileToFile(file, defaultSize, 800f, 480f, destFile);
    }

    /**
     * 将原始文件压缩，并返回新文件，位于程序私有目录的相对目录下。即使没有压缩，也会拷贝一份返回。
     * @param file
     * @param dirName 指定相对目录名
     * @return
     */
    public static File compressFileToFile(File file, String... dirName) {
        File destFile = BaseFao.getNewTempFile(Bitmap.CompressFormat.JPEG.name(), dirName);
        try{
            if(destFile != null){
                compressFileToFile(file, defaultSize, 720f, 1280f, destFile);
            }
        }
        catch(Exception e){
            MLog.e(e.toString());
            return null;
        }
        return destFile;
    }

    /**
     * 计算缩放比例
     * @param srcWidth 原始宽度
     * @param srcHeight 原始高度
     * @param maxWidth 目标宽度
     * @param maxHeight 目标高度
     * @return
     */
    private static int calcSampleScale(int srcWidth, int srcHeight, float maxWidth, float maxHeight) {
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (srcWidth >= srcHeight && srcWidth >= maxWidth) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (srcWidth / maxWidth);
        } else if (srcWidth < srcHeight && srcHeight > maxHeight) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (srcHeight / maxHeight);
        }
        if (be <= 0)
            be = 1;
        return be;
    }

    /**
     * 将位图对象存入文件
     * @param bitmap
     * @param destFile
     */
    private static void bitMapToFile(Bitmap bitmap, File destFile) {
        FileOutputStream b = null;
        try {
            b = new FileOutputStream(destFile);
            String fileName = destFile.getName();
            String suffixName = FileUtils.getSuffixName(fileName);
            // 把数据写入文件
            if (Bitmap.CompressFormat.JPEG.name().equalsIgnoreCase(suffixName))
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);
            else if(Bitmap.CompressFormat.PNG.name().equalsIgnoreCase(suffixName))
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);
            else if(Bitmap.CompressFormat.WEBP.name().equalsIgnoreCase(suffixName))
                bitmap.compress(Bitmap.CompressFormat.WEBP, 100, b);
            else
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);//throw new RuntimeException("不支持的文件扩展名:" + suffixName);
            b.flush();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(b != null){
                    b.close();
                }
            } catch (IOException e) {
            }
        }
    }

    public static int getScreenWidth(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        Point outSize = new Point();
        display.getSize(outSize);
        return outSize.x;
        //return display.getWidth();
    }

    public static Point getScreenSize(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        Point outSize = new Point();
        display.getSize(outSize);
        return outSize;
        //return display.getWidth();
    }

    /**
     * 显示大图片
     * @param context 上下文
     * @param imgSrc 图片源路径
     */
    public static void showBigImage(final Activity context, String imgSrc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Dialog_Fullscreen);
        // R.style.Dialog_Fullscreen 是加载一种样式用来将dialog全屏显示，这里dialog显示的事图片
        FineImgView imgView = new FineImgView(context, null);
        imgView.setFao(MsgConstants.getMsgImgFao());
        ImageView iv = imgView.getImageView();

        Point outSize = getScreenSize(context);
        ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
        layoutParams.width = outSize.x;
        layoutParams.height = outSize.y;
        iv.setLayoutParams(layoutParams);
        iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imgView.setSrc(imgSrc);

        //builder.setView(imgView);

        final AlertDialog dialog = builder.show();
        dialog.setContentView(imgView);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public static File uriToCompressFile(Activity context, Uri uri) {
        File file = ImageUtil.uriToFile(context.getContentResolver(), uri);
        if(file != null){
             return ImageUtil.compressFileToFile(file);
        }
        return null;
    }

    public static File bitMapToCompressFile(Bitmap bitmap) {
        //先压缩
        bitmap = ImageUtil.compressImage(bitmap);
        File file = ImageUtil.bitMapToFile(bitmap);
        return file;
    }

    /**
     * 裁剪图片,将bitmap裁剪为正方形，取长和宽中长度较小的长度，然后从中间取这个长度一块正方形
     * （比如长：5 ， 高：10 ， 将上面的2.5和下面的2.5裁剪， 去中间一块）
     * */
    public static Bitmap solveBitmap(Bitmap bitmap) {
        if(bitmap == null){
            return null;
        }

        int mwidth = bitmap.getWidth();
        int mheight = bitmap.getHeight();
        int dx = Math.abs(mheight - mwidth);
        if (mheight > mwidth) {
            return Bitmap.createBitmap(bitmap, 0, dx / 2, mwidth, mwidth);
        } else {
            return Bitmap.createBitmap(bitmap, dx / 2, 0, mheight, mheight);
        }
    }

    public static Bitmap loadImgThumbnail(String filePath, int w, int h) {
        Bitmap bitmap = getBitmapByPath(filePath);
        return zoomBitmap(bitmap, w, h);
    }

    /**
     * 获取bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap getBitmapByPath(String filePath) {
        return getBitmapByPath(filePath, null);
    }

    public static Bitmap getBitmapByPath(String filePath,
                                         BitmapFactory.Options opts) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis, null, opts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                if(fis != null){
                    fis.close();
                }
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

    /**
     * 获取bitmap
     *
     * @param file
     * @return
     */
    public static Bitmap getBitmapByFile(File file) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                if(fis != null){
                    fis.close();
                }
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

    /**
     * 放大缩小图片
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        Bitmap newbmp = null;
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            float scaleWidht = ((float) w / width);
            float scaleHeight = ((float) h / height);
            matrix.postScale(scaleWidht, scaleHeight);
            newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
                    true);
        }
        return newbmp;
    }

    /**
     * 判断当前Url是否标准的content://样式，如果不是，则返回绝对路径
     *
     * @param mUri
     * @return
     */
    public static String getAbsolutePathFromNoStandardUri(Uri mUri) {
        String filePath = null;

        String mUriString = mUri.toString();
        mUriString = Uri.decode(mUriString);

        String pre1 = "file://" + FileUtil.SDCARD;
        String pre2 = "file://" + FileUtil.SDCARD_MNT + File.separator;

        if (mUriString.startsWith(pre1)) {
            filePath = FileUtil.SDCARD + File.separator + mUriString.substring(pre1.length());
        } else if (mUriString.startsWith(pre2)) {
            filePath = FileUtil.SDCARD + File.separator + mUriString.substring(pre2.length());
        }
        return filePath;
    }

    /**
            * 通过uri获取文件的绝对路径
    *
            * @param uri
    * @return
            */
    @SuppressWarnings("deprecation")
    public static String getAbsoluteImagePath(Activity context, Uri uri) {
        String imagePath = "";
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.managedQuery(uri, proj, // Which columns to
                // return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)

        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                imagePath = cursor.getString(column_index);
            }
        }

        return imagePath;
    }

}
