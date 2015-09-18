package com.mfh.comna.utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

/**
 * 手机震动工具类
 * @author Administrator
 *
 */
public class NoticeUtil {

    /**
     * final Activity activity  ：调用该方法的Activity实例
     * long milliseconds ：震动的时长，单位是毫秒
     * long[] pattern  ：自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
     * boolean isRepeat ： 是否反复震动，如果是true，反复震动，如果是false，只震动一次
     */

    public static void Vibrate(final Activity activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    public static void Vibrate(final Activity activity, long[] pattern,boolean isRepeat) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }

    /**
     * 播放提示音
     * @param context
     */
    public static boolean noticeVoice(Context context) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        if (r != null) {
            r.play();
            return true;
        }
        else
            return false;
    }

    private static MediaPlayer mp = null;

    /**
     * 播放一段提示音乐
     * @param context
     * @param rawId R.raw...中的资源文件，如mp3
     */
    public static void noticeMusic(Context context, int rawId) {
        try {
            if (mp == null) {
                mp = new MediaPlayer();
                //R.raw.error 是ogg格式的音频 放在res/raw/下
                AssetFileDescriptor afd = context.getResources().openRawResourceFd(rawId);
                mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                mp.setAudioStreamType(AudioManager.STREAM_RING);
                afd.close();
                mp.prepare();
            }
            else if(mp.isPlaying())
                mp.pause();
            //然后启动播放
            mp.seekTo(0);
            mp.setVolume(1000, 1000);//设置声音
            mp.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

  /*  *//**
     * 显示提示框，notification
     * params i 0:消息，1：工单（或者是特殊服务）
     *//*
    public static void showNotification(final Activity that, int i, String title, String Content) {
        // 创建一个NotificationManager的引用
        NotificationManager notificationManager = (NotificationManager)that.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.colour_logo, title, System.currentTimeMillis());
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        //notification.flags |= Notification.FLAG_NO_CLEAR;
        notification.defaults = Notification.DEFAULT_LIGHTS;
        notification.ledARGB = Color.BLUE;
        notification.ledOnMS =5000;
        CharSequence contentTitle = title; // 通知栏标题
        CharSequence contentText = Content; // 通知栏内容
        Intent notificationIntent =new Intent(that, InitActivity.class); // 点击该通知后要跳转的Activity

        notification.defaults |= Notification.DEFAULT_VIBRATE;
        long[] vibrate = {0,100,200,300};
        notification.vibrate = vibrate ;
       *//* Bundle bundle = new Bundle();
        bundle.putString("humanName", humanName);
        bundle.putLong("humanId", humanId);
        bundle.putInt("msgMode", msgMode);
        notificationIntent.putExtras(bundle);*//*
        PendingIntent contentItent = PendingIntent.getActivity(that, 0, notificationIntent, 0);
        notification.setLatestEventInfo(that, contentTitle, contentText, contentItent);

        // 把Notification传递给NotificationManager
        notificationManager.notify(i, notification);
    }

    *//**
     * 显示提示框，notification
     * params i 0:消息，1：工单（或者是特殊服务）
     *//*
    public static void showNotification(final Service that, int i, String title, String Content) {
        // 创建一个NotificationManager的引用
        NotificationManager notificationManager = (NotificationManager)that.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.colour_logo, title, System.currentTimeMillis());
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        //notification.flags |= Notification.FLAG_NO_CLEAR;
        notification.defaults=Notification.DEFAULT_SOUND;
        //notificationManager.notify(1, notification);
        notification.ledARGB = Color.BLUE;
        notification.ledOnMS =5000;
        CharSequence contentTitle = title; // 通知栏标题
        CharSequence contentText = Content; // 通知栏内容
        Intent notificationIntent =new Intent(that, InitActivity.class); // 点击该通知后要跳转的Activity

        notification.defaults |= Notification.DEFAULT_VIBRATE;
        long[] vibrate = {0,100,200,300};
        notification.vibrate = vibrate ;
       *//* Bundle bundle = new Bundle();
        bundle.putString("humanName", humanName);
        bundle.putLong("humanId", humanId);
        bundle.putInt("msgMode", msgMode);
        notificationIntent.putExtras(bundle);*//*
        PendingIntent contentItent = PendingIntent.getActivity(that, 0, notificationIntent, 0);
        notification.setLatestEventInfo(that, contentTitle, contentText, contentItent);

        // 把Notification传递给NotificationManager
        notificationManager.notify(i, notification);
    }*/
}
