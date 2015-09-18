package com.mfh.comna.utils.nfc;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Android设备检测到一个Tag时，会创建一个Tag对象，将其放在Intent对象，然后发送到此Activity
 * @author yxm
 * @version 1.0
 */
public class NfcActivity extends Activity {
    private String Token;//Intent对象经处理后获取的特定标记

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //processIntent(this.getIntent());//调用处理Intent对象的方法

        Token = getToken(this.getIntent());

        if (Token != null){
            //发送广播
            Intent intent = new Intent();
            intent.putExtra("Token", Token);
            intent.setAction("com.mfh.nfc.NFC_BROADCAST");
            sendBroadcast(intent);
        }

        //结束Activity。因为只有Activity可以接收包含Tag的Intent对象，
        //故通过使NfcActivity快速消失以达到近似隐藏运行的效果，以增强用户体验。
        finish();
    }

    /**
     * 字符序列转换为16进制字符串
     * @param src 字符序列
     * @return
     */
    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }

    /**
     * 从Intent对象中解析 NDEF 消息（NFC Data Exchange Format，即 NFC 数据交换格式，
     * NDEF Message 为 NFC forum 定义的数据格式）
     * @param intent 包含Tag的Intent对象
     */
    private void processIntent(Intent intent) {
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);//取出封装在intent中的TAG

        boolean auth = false;

        //读取TAG
        MifareClassic mfc = MifareClassic.get(tagFromIntent);
        try {
            String metaInfo = "";

            //从这个TagTechnology对象对tag启用I/O操作
            mfc.connect();
            //使用键值A鉴定扇区
            int j = 0;//需要获取的Token信息位于扇区0
            auth = mfc.authenticateSectorWithKeyA(j,
                    MifareClassic.KEY_DEFAULT);
            int bIndex;
            if (auth) {
                // 读取扇区中的块
                bIndex = mfc.sectorToBlock(j);
                byte[] data = mfc.readBlock(bIndex);
                metaInfo += bytesToHexString(data);
                metaInfo = metaInfo.substring(8, 10) + metaInfo.substring(6, 8) +
                        metaInfo.substring(4, 6) + metaInfo.substring(2, 4);
                metaInfo = String.valueOf(Integer.parseInt(metaInfo, 16));
            } else {
                metaInfo += "Sector " + j + ":验证失败\n";

            }
            Token = metaInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取UID
     * @param intent 扫描到的NFC的Intent对象
     * @return
     */
    private String getToken(Intent intent){
        byte[] bytesId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
        String uid = bytesToHexString(bytesId);
        uid = uid.substring(8, 10) + uid.substring(6, 8) + uid.substring(4, 6) + uid.substring(2, 4);
        uid = String.valueOf(Long.parseLong(uid, 16));
        return uid;
    }

}
