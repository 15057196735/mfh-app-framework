package com.mfh.comna.select_time;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;

/**
 * Created by 蔡静 on 14-8-15.
 */
public class MyTimePickerDialog extends TimePickerDialog {
    private final OnTimeSetListener mCallback;

    public MyTimePickerDialog(Context context, OnTimeSetListener callBack,
                              int hourOfDay, int minute, boolean is24HourView) {
        super(context, callBack, hourOfDay, minute, is24HourView);
        setButton("确定", this);
        mCallback = callBack;
    }


    @Override
    protected void onStop() {
    }
}
