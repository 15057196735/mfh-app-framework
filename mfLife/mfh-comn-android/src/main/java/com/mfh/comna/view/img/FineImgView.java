package com.mfh.comna.view.img;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mfh.comna.api.utils.ImageUtil;
import com.mfh.comna.api.utils.MLog;
import com.mfh.comna.bizz.AppConfig;
import com.mfh.comna.bizz.material.logic.BitMapCacheService;
import com.mfh.comn.config.UConfig;
import com.mfh.comna.R;
import com.mfh.comna.comn.cfg.UConfigHelper;
import com.mfh.comna.comn.database.dao.FileNetDao;
import com.mfh.comna.comn.logic.MyAsyncTask;
import com.mfh.comna.comn.logic.ServiceFactory;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 关于图像显示的自定义类。
 * 支持先从网络（网络地址通过配置项serverUrlconfigItem指定）上下载存到本地(本地目录名在变量localDir中指定)，
 * 然后再显示；以后就直接从本地进行显示了。
 * 支持图片过大时使用缩略图。
 * Created by Shicy on 14-3-19.
 */
public class FineImgView extends FrameLayout {
    private ImageView imageView;
    private BitMapCacheService cache;
    private FileNetDao fao;
    private Bitmap bitmap;
    //private String serverUrlconfigItem = DEFAULT_ITEM;//图片服务器配置项，若为null代表是全地址，不需要。
    //private String localDir = LOCAL_DIR;
    private Integer maxWidth = null;//若设置了最大宽度，则应考虑采用缩略图显示，避免内存溢出。
    private boolean needSample = false;//是否需要使用缩略图。

    private static String DEFAULT_ITEM = "app.head.server.url";
    private static String LOCAL_DIR = "headImgDir";

    private static Map<String, FileNetDao> fileDaoMap = new HashMap<String, FileNetDao>();

    /**
     * 获取网络文件访问对象
     * @param configKeyParam 网络文件服务器url地址配置项，可以为空
     * @param localDir 网络文件下载到本地存储的目录
     * @return
     */
    public static FileNetDao getFao(String configKeyParam, String localDir) {
        String configKey = configKeyParam;
        if (configKey == null || configKey.length() == 0)
            configKey = localDir;

        FileNetDao ret = fileDaoMap.get(configKey);
        if (ret == null) {
            String imgServerUrl = null;
            if (configKey != null) {
                imgServerUrl = UConfigHelper.getConfig().getDomain(UConfig.CONFIG_COMMON)
                        .getString(configKey, "http://resource.manfenmm.com/mfh/image/wxuser/");
                if (!imgServerUrl.endsWith( "/"))
                    imgServerUrl += "/";
            }
            else
                imgServerUrl = "";
            ret = new FileNetDao(localDir, imgServerUrl);
            fileDaoMap.put(configKey, ret);
        }
        return ret;
    }

    /**
     * 布局文件
     * @return
     */
    protected int getLayOutResource() {
        return R.layout.subview_member_head;
    }

    public FineImgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(getLayOutResource(), this, true);
        imageView = (ImageView)view.findViewById(R.id.ms_headImgView);
        cache = ServiceFactory.getService(BitMapCacheService.class, context);
    }

    public static FileNetDao getHeadImgFao() {
        if(AppConfig.RELEASE){
            return getFao(DEFAULT_ITEM, LOCAL_DIR);
        }else{
            return getFao("dev." + DEFAULT_ITEM, LOCAL_DIR);
        }
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setFao(FileNetDao fao) {
        this.fao = fao;
    }

    public FileNetDao getFao() {
        return fao;
    }

    /**
     * 设置显示宽度，可以用于判断是否需要使用缩略图显示以节省内存加快速度。
     * @param reqWidth
     */
    public void setMaxWidth(Integer reqWidth) {
        this.maxWidth = reqWidth;
    }

    public void setNeedSample(boolean needSample) {
        this.needSample = needSample;
    }

    /*public void setServerUrlconfigItem(String serverUrlconfigItem) {
        this.serverUrlconfigItem = serverUrlconfigItem;
    }

    public void setLocalDir(String localDir) {
        this.localDir = localDir;
    }*/

    /**
     * 设置网络图片地址,未来内部应使用缓存。
     * @param url
     */
    public void setSrc(final String url) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(R.drawable.chat_tmp_user_head);
            return;
        }

//        final String cacheKey = url + Boolean.toString(this.needSample)
//                + (maxWidth == null ? "" : maxWidth.toString());//生成cacheKey
        if (cache != null) {//优先使用缓存
            bitmap = cache.getBitmapFromMemCache(url);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                return;
            }
        }

        //***下面这一块是从内存卡中读取
        File file = new File(url);
        if (file.exists()) {
            new BitmapAysn(false,true).execute(url);
            return;
        }
        //******

        if (fao == null) {
            fao = getHeadImgFao();//使用默认的
        }

        fao.processFile(url, new FileNetDao.CallBack() {
            @Override
            public void processFile(File file) {
                MLog.d("processFile: " + file.toString());
                try {
                    bitmap = null;
                    if (needSample){
                        new BitmapAysn(false,false).executeOnExecutor(cache.getBitmapExecutor(), file.getName());
                    }
                    else {
                        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        imageView.setImageBitmap(bitmap);
                        if (cache != null)//加入缓存
                            cache.addBitmapToMemoryCache(url, bitmap);
                    }
                }
                catch (Throwable e) {
                    if (e instanceof OutOfMemoryError && !needSample) {
                        try {
                            bitmap = ImageUtil.compressImageBySize(file.getAbsolutePath(), maxWidth);
                            imageView.setImageBitmap(bitmap);
                        }
                        catch (Throwable e1) {
                            LoggerFactory.getLogger(this.getClass()).error(e1.getMessage(), e1);
                        }
                    }
                    else
                        LoggerFactory.getLogger(this.getClass()).error(e.getMessage(), e);
                }
            }

            @Override
            public void onFailure(String fileName, Throwable e) {
                MLog.d(String.format("file:%s, because:%s", fileName, e.toString()));
            }
        });
    }

//    /**
//     * 获取原始位图
//     * @param file
//     * @return
//     */
//    private Bitmap genBitmap(File file) {
//        InputStream is = null;
//        try {
//            is = new FileInputStream(file);
//            return BitmapFactory.decodeStream(is);
//        }
//        catch (IOException e) {
//            LoggerFactory.getLogger(this.getClass()).error(e.getMessage(), e);
//            return null;
//        }
//        finally {
//            if (is != null) {
//                try {
//                    is.close();
//                } catch (IOException e) {
//                }
//            }
//        }
//    }

    /**
//     * 获取缩略图
//     * @param theWidth  允许宽度
//     * @return
//     */
//    private Bitmap decodeSampledBitmap(String filePath, Integer theWidth) {
//        if (theWidth == null)
//            theWidth = this.getWidth();
//        try {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//
//            options.inJustDecodeBounds = true;
//            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
//
//            double dh = options.outHeight;
//            double dw = options.outWidth;
//            int sample = calculateInSampleSize(options, theWidth, (int) (theWidth * (dh / dw)));
//            options.inSampleSize = sample;
//            options.inJustDecodeBounds = false;
//            return BitmapFactory.decodeFile(filePath,options);
//        }
//        catch (Throwable e) {
//            LoggerFactory.getLogger(this.getClass()).error(e.getMessage(), e);
//            return null;
//        }
//    }

//    /**
//     * 获取缩放比例，大于1代表要缩小原图
//     * @param options
//     * @param reqWidth
//     * @param reqHeight
//     * @return
//     */
//    public static int calculateInSampleSize(
//            BitmapFactory.Options options, int reqWidth, int reqHeight) {
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//            if (width > height) {
//                inSampleSize = Math.round((float)height / (float)reqHeight);
//            } else {
//                inSampleSize = Math.round((float)width / (float)reqWidth);
//            }
//        }
//        return inSampleSize;
//    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    private class BitmapAysn extends MyAsyncTask<String, Bitmap> {

        private boolean isSd;

        protected BitmapAysn(boolean showDialog,boolean isSd) {
            super(showDialog);
            this.isSd = isSd;
        }

        @Override
        protected Bitmap doInBackgroundInner(String... params) {
            if (maxWidth == null) {
                maxWidth = getWidth();
            }
            String url = params[0];
            if (!isSd)
                url = fao.readFile(url).getAbsolutePath();
            bitmap =  ImageUtil.compressImageBySize(url,maxWidth);
            bitmap = ImageUtil.solveBitmap(bitmap);
            if (cache != null && !TextUtils.isEmpty(params[0]) && bitmap != null) //加入缓存
                cache.addBitmapToMemoryCache(params[0], bitmap);
            return bitmap;
        }

        @Override
        protected void onPostExecuteInner(Bitmap result, String... params) {
            imageView.setImageBitmap(result);
        }
    }

}
