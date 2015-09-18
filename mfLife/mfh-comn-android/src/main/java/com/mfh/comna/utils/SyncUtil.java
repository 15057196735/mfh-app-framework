package com.mfh.comna.utils;

import java.io.File;
import java.io.IOException;
import com.mfh.comn.utils.IOUtils;
import android.content.Context;
import android.content.res.AssetManager;

/**
 * 同步工具类
 * 
 * @author zhangyz created on 2013-5-25
 * @since Framework 1.0
 */
public class SyncUtil {

    /**
     * 把初始的数据文件从资源中拷贝到目录下。
     * @param context
     * @author zhangyz created on 2013-5-25
     */
    public static void copyDatabase(Context context, String dbName){
        File dbfile = context.getDatabasePath(dbName);//结果为：/data/data/com.mfh.itm/databases/itm.db; context.getFilesDir()结果为：/data/data/com.dins.itm/databases/files
        //File dbfile = new File(context.getFilesDir().getAbsolutePath() + File.separator + Constants.DBNAME);
        File dir = dbfile.getParentFile();
        if(dir.exists() == false){
            dir.mkdirs();
        }        
        if(dbfile.exists()){
            dbfile.delete();
        }
        
        AssetManager am = context.getAssets();//放在assert目录下
        try {
            IOUtils.copy(am.open(dbName), dbfile);
        }
        catch (IOException e) {
            throw new RuntimeException("初始化数据失败:" + e.getMessage(), e);
        }
    }

}
