package com.mfh.comna.select_time;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.mfh.comna.R;

import java.util.Date;

/**
 * Created by 蔡静 on 14-8-15.
 */
public class MyDatePickerDialog extends DatePickerDialog {
    public MyDatePickerDialog(Context context,
                              OnDateSetListener callBack,
                              int year,
                              int monthOfYear,
                              int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
        setButton("确定", this);
    }

    @Override
    protected void onStop() {
    }
}
