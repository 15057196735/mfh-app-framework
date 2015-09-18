package com.mfh.comna.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import com.mfh.comn.utils.FileUtils;
import com.mfh.comna.bizz.BizApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2014/10/11.
 * 表情使用的工具类
 */
public class FaceUtil {

    private static Map<String,Integer> faceMap;

    private static void init() {
        try {
            faceMap = new HashMap<String,Integer>();
            Context context = BizApplication.getAppContext();
            InputStream inputStream = context.getAssets().open("face.txt");
            String str = FileUtils.readFileToString(inputStream, "UTF-8");
            String[] faces = str.split("%");
            for (int i = 0; i < faces.length; i++) {
                int id = context.getResources().getIdentifier("smiley_" + i, "drawable",
                        context.getPackageName());
                String temp;
                if (i == 0) {
                    temp = faces[i].substring(1).trim();
                } else {
                    temp = faces[i].trim();
                }
                faceMap.put(temp, id);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("配置文件face.txt 不存在");
        }
    }


    /**
     * 根据id的内容获取资源图片名表情符号 ，不存在返回NO
     * */
    public static String getFaceString(int key) {
        if (null == faceMap)
            init();
        for (Map.Entry<String, Integer> entry : faceMap.entrySet()) {
            if (entry.getValue() == key) {
                return entry.getKey();
            }
        }
        return "NO";
    }

//    /**
//     * @param msg 显示的内容
//     * @param faceImageWidth 表情显示的宽度 内部已经适配，直接传入数值就可以
//     * @param faceImageHeight 表情显示的高度
//     * */
//   public static SpannableString getSpannable(Context context, String msg, int faceImageWidth, int faceImageHeight) {
//       if (faceMap == null)
//           init();
//       SpannableString spannableString = new SpannableString(msg);
//       for (int i = 0; i < msg.length(); i++) {
//            if ("/".equals(String.valueOf(msg.charAt(i)))) {
//                int end = i + 7;
//                if (i + 7 > msg.length())
//                    end = msg.length();
//                String temp = msg.substring(i,end);
//                for (Map.Entry<String, Integer> entry : faceMap.entrySet()) {
//                     String face = entry.getKey();
//                     if (TextUtils.isEmpty(face))
//                        continue;
//                    if (temp.contains(face)) {
//                        int id = entry.getValue();
//                        if (id == 0)
//                            continue;
//                        Drawable drawable = context.getResources().getDrawable(id);
//            //          drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//
//                        drawable.setBounds(0, 0, DensityUtil.dip2px(context,faceImageWidth), DensityUtil.dip2px(context,faceImageHeight));
//                        //要让图片替代指定的文字就要用ImageSpan
//                        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
//                        //开始替换，注意第2和第3个参数表示从哪里开始替换到哪里替换结束（start和end）
//                        //最后一个参数类似数学中的集合,[5,12)表示从5到12，包括5但不包括12
//                        spannableString.setSpan(span, i, i + face.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                        break;
//                    }
//                }
//            }
//       }
//       return spannableString;
//   }


    /**
     * @param msg 显示的内容
     * @param faceImageWidth 表情显示的宽度 内部已经适配，直接传入数值就可以
     * @param faceImageHeight 表情显示的高度
     * */
    public static SpannableString getSpannable(Context context, String msg, int faceImageWidth, int faceImageHeight) {
        if (faceMap == null)
            init();
        SpannableString spannableString = new SpannableString(msg);
        for (Map.Entry<String, Integer> entry : faceMap.entrySet()) {
            String face = entry.getKey();
            if (TextUtils.isEmpty(face))
                continue;
            if (msg.contains(face)) {
                int id = entry.getValue();
                if (id == 0)
                    continue;
                int index = 0;
                while (msg.indexOf(face,index) != -1) {
                    index = msg.indexOf(face, index);
                    Drawable drawable = context.getResources().getDrawable(id);
                    //          drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

                    if(drawable != null){
                        drawable.setBounds(0, 0, DensityUtil.dip2px(context, faceImageWidth),
                                DensityUtil.dip2px(context, faceImageHeight));
                        //要让图片替代指定的文字就要用ImageSpan
                        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
                        //开始替换，注意第2和第3个参数表示从哪里开始替换到哪里替换结束（start和end）
                        //最后一个参数类似数学中的集合,[5,12)表示从5到12，包括5但不包括12
                        spannableString.setSpan(span, index, index + face.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }

                    index = index + face.length();
                    if (index >= msg.length())
                        break;
                }
            }
        }
        return spannableString;
    }

}
