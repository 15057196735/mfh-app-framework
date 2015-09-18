package com.mfh.comna.utils.sdimage;

import android.graphics.Bitmap;

import com.mfh.comn.bean.EntityWrapper;

import java.io.Serializable;

/**
 * Created by Administrator on 2014/10/21.
 */
public class Image implements Serializable {
    private String path;
    private boolean isCheck;

    public Image(String path) {
        this.path = path;
    }

    public Image(){}

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

}
