package com.mfh.comna.bizz.material.logic;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import com.mfh.comna.comn.logic.ComnService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 位图缓存服务
 * Created by Administrator on 14-5-16.
 */
public class BitMapCacheService extends ComnService {
    private LruCache<String, Bitmap> mMemoryCache;
    private ExecutorService bitmapLoadAndDisplayExecutor = Executors.newFixedThreadPool(4, new ThreadFactory() {
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            // 设置线程的优先级别，让线程先后顺序执行（级别越高，抢到cpu执行的时间越多）
            t.setPriority(Thread.NORM_PRIORITY - 1);
            return t;
        }
    });

    /**
     * 默认构造函数
     */
    public BitMapCacheService() {
        super();
        init();
    }

    private void init() {
        // Get memory class of this device, exceeding this amount will throw an
        // OutOfMemory exception.
        final int memClass = ((ActivityManager) this.getContext().getSystemService(
                Context.ACTIVITY_SERVICE)).getMemoryClass();

        // Use 1/8th of the available memory for this memory cache.
        int cacheSize = 1024 * 1024 * memClass / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                int count = bitmap.getByteCount();
                return count;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public ExecutorService getBitmapExecutor() {
        return bitmapLoadAndDisplayExecutor;
    }
}
