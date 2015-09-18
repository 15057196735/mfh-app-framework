package com.mfh.comna.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import com.mfh.comna.api.helper.UIHelper;
import com.mfh.comna.api.utils.ImageUtil;
import com.mfh.comna.comn.logic.IService;

import java.io.File;

/**
 * 相机一次拍照过程工具类，分成两个步骤：
 * 1、调用者先调用makeCameraRequest方法启动拍照程序
 * 2、调用者在onActivityResult中再调用getCameraResultFile获取拍照结果。
 * 注意必须使用服务工厂，不能使用new得到本对象
 * Created by Administrator on 14-5-17.
 */
public class CameraSessionUtil implements IService {
    private transient Uri cameraImgUrl = null;//用来存储相机拍照的文件url，临时变量
    /**
     * 生成调用相机的请求intent，指示生成外部文件。
     * requestCode为：MsgConstants.CODE_REQUEST_CAMERA
     * @param context
     * @param requestCode 若为空，默认为MsgConstants.CODE_REQUEST_CAMERA
     * @return 外部文件的uri地址，后面拍照完成后可以使用
     */
    public void makeCameraRequest(Activity context, int... requestCode) {
        cameraImgUrl = null;//先初始化
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues(1);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        cameraImgUrl = context.getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImgUrl);
        int factCode = UIHelper.REQUEST_CODE_CAMERA;
        if (requestCode != null && requestCode.length > 0)
            factCode = requestCode[0];

        context.startActivityForResult(intent, UIHelper.REQUEST_CODE_CAMERA);
        //return cameraImgUrl;
    }

    /**
     * 获取相机拍照后得到的压缩文件对象.
     * 要求resultCode == Activity.RESULT_OK，调用者自己判断
     * @param resultCode 相机程序返回码
     * @param data 相机程序返回的intent
     * @param context 当前操作的上下文
     * @return
     */
    public File getCameraResultFile(int resultCode, Intent data, Activity context, Uri uri) {
        if (resultCode != Activity.RESULT_OK)
            return null;
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            throw new RuntimeException("SD card is not avaiable/writeable right now.");
        }
        //Uri uri = null;
        if (data == null) {
            //uri = this.cameraImgUrl;
            if (uri == null)
                throw new RuntimeException("获取相机返回的图片地址为空，请进行版本适配!");
            return ImageUtil.uriToCompressFile(context, uri);
        }
        else  {
            uri = data.getData();
            if (uri == null) {
                Bundle bundle = data.getExtras();
                //但发现某些4.2版本返回的是缩略图，所以在调用相机时明确指定使用外部存储，然后使用第一个分支（此时intent为null）
                Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
                return ImageUtil.bitMapToCompressFile(bitmap);
            }
            else {
                return ImageUtil.uriToCompressFile(context, uri);
            }
        }
    }
    /**
     * 获取相机拍照后得到的压缩文件对象.
     * 要求resultCode == Activity.RESULT_OK，调用者自己判断
     * @param resultCode 相机程序返回码
     * @param data 相机程序返回的intent
     * @param context 当前操作的上下文
     * @return
     */
    public File getCameraResultFile(int resultCode, Intent data, Activity context) {
        if (resultCode != Activity.RESULT_OK)
            return null;
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            throw new RuntimeException("SD card is not avaiable/writeable right now.");
        }
        Uri uri = null;
        if (data == null) {
            uri = this.cameraImgUrl;
            if (uri == null)
                throw new RuntimeException("获取相机返回的图片地址为空，请进行版本适配!");
            return ImageUtil.uriToCompressFile(context, uri);
        }
        else  {
            uri = data.getData();
            if (uri == null) {
                Bundle bundle = data.getExtras();
                //但发现某些4.2版本返回的是缩略图，所以在调用相机时明确指定使用外部存储，然后使用第一个分支（此时intent为null）
                Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
                return ImageUtil.bitMapToCompressFile(bitmap);
            }
            else {
                return ImageUtil.uriToCompressFile(context, uri);
            }
        }
    }

    public String getFilePath(int resultCode, Intent data, Activity context) {
        if (resultCode != Activity.RESULT_OK)
            return null;
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            throw new RuntimeException("SD card is not avaiable/writeable right now.");
        }
        Uri uri = null;
        if (data == null) {
            uri = this.cameraImgUrl;
            if (uri == null)
                throw new RuntimeException("获取相机返回的图片地址为空，请进行版本适配!");
            return ImageUtil.uriToPath(context.getContentResolver(),uri);
        }else {
            uri = data.getData();
            return ImageUtil.uriToPath(context.getContentResolver(), uri);
        }
    }
}
