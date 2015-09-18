package com.mfh.comna.api.helper;

import java.util.Date;

/**
 * Created by Administrator on 2015/6/11.
 */
public class URLHelper {
    public static final String URL_KEY_T = "t";

    public static String append(String url, String key, String value){
        if(url == null){
            return "";
        }

        StringBuilder sb = new StringBuilder(url);
        if (!url.contains("?")){//.indexOf("?") > 0
            sb.append("?");
        }else{
            sb.append("&");
        }
        sb.append(String.format("%s=%s", key, value));
        return sb.toString();
    }

    public static String appendTime(String url){
        if(url == null){
            return "";
        }

        StringBuilder sb = new StringBuilder(url);
        if (!url.contains("?")){//.indexOf("?") > 0
            sb.append("?");
        }else{
            sb.append("&");
        }
        sb.append(String.format("t=%s", String.valueOf(new Date().getTime())));
        return sb.toString();
    }
}
