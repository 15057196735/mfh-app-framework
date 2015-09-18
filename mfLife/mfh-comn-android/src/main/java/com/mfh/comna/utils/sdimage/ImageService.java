package com.mfh.comna.utils.sdimage;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*******************************************
 * 
 * 获取手机内存图片
 *
 *******************************************/
public class ImageService {
	private Context context;

	public ImageService(Context context) {
		this.context = context;
	}

	/**
	 * @description:通过contentprovider获得sd卡上的图片
	 * @return:void
	 */

	public List<HashMap<String, String>> getImages() {
		// 指定要查询的uri资源
		Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		// 获取ContentResolver
		ContentResolver contentResolver = context.getContentResolver();
		// 查询的字段
		String[] projection = { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DISPLAY_NAME,
				MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE };
		// 条件
		String selection = MediaStore.Images.Media.MIME_TYPE + "=?";
		// 条件值(這裡的参数不是图片的格式，而是标准，所有不要改动)
		String[] selectionArgs = { "image/jpeg" };
		// 排序
		String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " desc";
		// 查询sd卡上的图片
		Cursor cursor = contentResolver.query(uri, projection, selection,
				selectionArgs, sortOrder);
		List<HashMap<String, String>> imageList = new ArrayList<HashMap<String, String>>();
		if (cursor != null) {
			HashMap<String, String> imageMap;
			while (cursor.moveToNext()) {
				imageMap = new HashMap<String, String>();
				// 获得图片的id
				imageMap.put("imageID", cursor.getString(cursor
						.getColumnIndex(MediaStore.Images.Media._ID)));
				// 获得图片显示的名称
				imageMap.put("imageName", cursor.getString(cursor
						.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
				// 获得图片的信息
				imageMap.put(
						"imageInfo",
						""
								+ cursor.getLong(cursor
										.getColumnIndex(MediaStore.Images.Media.SIZE) / 1024)
								+ "kb");
				// 获得图片所在的路径(可以使用路径构建URI)
				imageMap.put("data", cursor.getString(cursor
						.getColumnIndex(MediaStore.Images.Media.DATA)));
				imageList.add(imageMap);
			}
			// 关闭cursor
			cursor.close();
		}
		return imageList;
	}
}