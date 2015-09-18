package com.mfh.comna.bizz;

import android.content.Context;

import com.mfh.comna.api.utils.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * 应用程序配置类：用于保存用户相关信息及设置
 * Created by Administrator on 2015/5/26.
 */
public class AppConfig {
    //正式发布版本为true(production,beta,develop)
    public static boolean RELEASE = true;
    public static boolean DEBUG = !RELEASE;

    private final static String APP_CONFIG = "false";

    public static String PREF_APP_UPDATE = "app_update";
    public static final String KEY_APP_NAME = "app_name";
    public static final String VALUE_APP_NAME_DEF = "default";

    public static final String EXTRA_KEY_APP_NAME = "app_name";

    //com.mfh.owner "manfenhome"
    public static final String DEFAULT_ROOT_FOLDER = BizApplication.getAppContext().getPackageName();
    public final static String DEFAULT_SAVE_PATH = FileUtil.SDCARD + File.separator + DEFAULT_ROOT_FOLDER + File.separator;

    // 默认缓存路径
    public final static String DEFAULT_CACHE_PATH = DEFAULT_SAVE_PATH + "cache" + File.separator;
    // 默认缓存图片路径
    public final static String DEFAULT_KJBITMAP_CACHEPATH = DEFAULT_ROOT_FOLDER + File.separator + "imagecache";
    public final static String DEFAULT_CACHE_IMAGE_PATH = DEFAULT_SAVE_PATH + "cache" + File.separator + "image";
    // 默认存放截屏的路径
    public final static String DEFAULT_SCRESHOOT_PATH = DEFAULT_SAVE_PATH + "screenshoot" + File.separator;
    // 默认存放拍照的路径
    public final static String DEFAULT_SAVE_CAMERA_PATH = DEFAULT_SAVE_PATH + "Camera" + File.separator;
    // 默认存放裁剪的路径
    public final static String DEFAULT_SAVE_CROP_PATH = DEFAULT_SAVE_PATH + "crop" + File.separator;
    // 默认存放文件下载的路径
    public final static String DEFAULT_SAVE_FILE_PATH = DEFAULT_SAVE_PATH + "download" + File.separator;

    private Context mContext;
    private static AppConfig instance;

    public static AppConfig getInstance(Context context) {
        if (instance == null) {
            instance = new AppConfig();
            instance.mContext = context;
        }
        return instance;
    }

    public String get(String key) {
        Properties props = get();
        return (props != null) ? props.getProperty(key) : null;
    }

    public Properties get() {
        FileInputStream fis = null;
        Properties props = new Properties();
        try {
            // 读取files目录下的config
            // fis = activity.openFileInput(APP_CONFIG);

            // 读取app_config目录下的config
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            fis = new FileInputStream(dirConf.getPath() + File.separator
                    + APP_CONFIG);

            props.load(fis);
        } catch (Exception e) {
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return props;
    }

    private void setProps(Properties p) {
        FileOutputStream fos = null;
        try {
            // 把config建在files目录下
            // fos = activity.openFileOutput(APP_CONFIG, Context.MODE_PRIVATE);

            // 把config建在(自定义)app_config的目录下
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            File conf = new File(dirConf, APP_CONFIG);
            fos = new FileOutputStream(conf);

            p.store(fos, null);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    public void set(Properties ps) {
        Properties props = get();
        props.putAll(ps);
        setProps(props);
    }

    public void set(String key, String value) {
        Properties props = get();
        props.setProperty(key, value);
        setProps(props);
    }

    public void remove(String... key) {
        Properties props = get();
        for (String k : key)
            props.remove(k);
        setProps(props);
    }



}
