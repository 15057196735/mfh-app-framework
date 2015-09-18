package com.mfh.comna.bizz.material.view;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * 附加功能选项实体类
 * Created by yxm on 2015/1/23.
 * TODO,refer to mainTab
 */
public class Option {
    public static final int OPTION_MODE_PICTURE        = 0x01;
    public static final int OPTION_MODE_SHOOT          = 0x02;
    public static final int OPTION_MODE_CARDS          = 0x03;
    public static final int OPTION_MODE_VIREMENT       = 0x04;
    public static final int OPTION_MODE_RED_ENVELOPE   = 0x05;
    public static final int OPTION_MODE_BUSINESS_CARD  = 0x06;
    public static final int OPTION_MODE_FAVORITE       = 0x07;
    public static final int OPTION_MODE_POSITION       = 0x08;
    public static final int OPTION_MODE_CLOTHES        = 0x09;
    public static final int OPTION_MODE_EXPRESS        = 0x0A;
    public static final int OPTION_MODE_FRESH          = 0x0B;
    public static final int OPTION_MODE_HOUSE_KEEPING  = 0x0C;
    public static final int OPTION_MODE_FRUITS         = 0x0D;
    public static final int OPTION_MODE_HOUSE          = 0x0E;
    public static final int OPTION_MODE_COMPLAINT      = 0x0F;
    public static final int OPTION_MODE_MAINTAIN       = 0x10;
    public static final int OPTION_MODE_MILK           = 0x11;
    public static final int OPTION_MODE_FLOWERS        = 0x12;
    public static final int OPTION_MODE_VEGETABLED     = 0x13;
    public static final int OPTION_MODE_COMMODITY      = 0x14;

    private int id;
    private String name;
    private Drawable iconDrawable;

    public Option(int id, String name, Drawable iconDrawable) {
        this.id = id;
        this.name = name;
        this.iconDrawable = iconDrawable;
    }

    public Option(Context context, int id, String name, int iconResId) {
        super();
        this.id = id;
        this.name = name;
        this.iconDrawable = context.getResources().getDrawable(iconResId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIconDrawable() {
        return iconDrawable;
    }

    public void setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
    }
}
