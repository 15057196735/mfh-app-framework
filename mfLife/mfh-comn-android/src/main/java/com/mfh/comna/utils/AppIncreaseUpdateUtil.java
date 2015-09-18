package com.mfh.comna.utils;

import java.io.IOException;

import android.content.Context;
import cn.sgwhp.patchdroid.PatchClient;

/**
 * 生成新apk的可能比较耗时，需要在子线程中执行
 */
public class AppIncreaseUpdateUtil {
	
	/**
	 * 将下载的文件和老版本的apk整合产生新版本apk
	 * @param context 上下文
	 * @param downPath 下载下来的差异文件的路径
	 * @param newApkPath 设置新产生apk文件的路径
	 * @throws java.io.IOException
	 */
	public static void makeNewApk(Context context,String downPath,String newApkPath) throws IOException {
		PatchClient.loadLib(); //加载so库
		PatchClient.applyPatchToOwn(context, newApkPath, downPath);
	}
}
