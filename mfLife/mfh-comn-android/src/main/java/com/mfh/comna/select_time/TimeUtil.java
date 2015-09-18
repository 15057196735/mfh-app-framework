package com.mfh.comna.select_time;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.widget.AbsSpinner;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import com.mfh.comn.bean.TimeCursor;
import com.mfh.comna.view.widget.SimpleHorizontalField;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**关于时间的工具类
 * Created by 李潇阳 on 14-8-1.
 */
public class TimeUtil {
    private static String dateStr;

    /**
     * 需要选择时间的时候就调用这个方法，精确到分
     * @param that 用来传Activity
     * @param tv 接受控件
     * created by cj
     */
    public static void setDate(Activity that, final SimpleHorizontalField tv){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        final MyTimePickerDialog timePickerDialog = new MyTimePickerDialog(that,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view,
                                          int hourOfDay, int minute) {
                        String sHour;
                        String sMinute;
                        if(hourOfDay < 10) {
                            sHour = "0" + hourOfDay;
                        }else {
                            sHour = hourOfDay + "";
                        }
                        if(minute < 10) {
                            sMinute = "0" + minute;
                        }else {
                            sMinute = minute + "";
                        }
                        String time = sHour + ":" + sMinute;
                        tv.setValue(dateStr + time);
                    }
                }, mHour, mMinute, true);
        MyDatePickerDialog datePickerDialog = new MyDatePickerDialog(that, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                dateStr = year + "-" + (monthOfYear+1) + "-" + dayOfMonth + " ";
                timePickerDialog.show();
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    /**
     * 需要选择时间的时候就调用这个方法，精确到分
     * @param that 用来传Activity
     * @param tv 接受控件
     * created by cj
     */
    public static void setDate(Activity that, final TextView tv){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        final MyTimePickerDialog timePickerDialog = new MyTimePickerDialog(that,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view,
                                          int hourOfDay, int minute) {
                        String sHour;
                        String sMinute;
                        if(hourOfDay < 10) {
                            sHour = "0" + hourOfDay;
                        }else {
                            sHour = hourOfDay + "";
                        }
                        if(minute < 10) {
                            sMinute = "0" + minute;
                        }else {
                            sMinute = minute + "";
                        }
                        String time = sHour + ":" + sMinute;
                        tv.setText(dateStr + time);
                    }
                }, mHour, mMinute, true);
        MyDatePickerDialog datePickerDialog = new MyDatePickerDialog(that, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                dateStr = year + "-" + (monthOfYear+1) + "-" + dayOfMonth + " ";
                timePickerDialog.show();
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public static void getTime(Activity that, final TextView textView) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        MyTimePickerDialog timePickerDialog = new MyTimePickerDialog(that,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view,
                                          int hourOfDay, int minute) {
                        String sHour;
                        String sMinute;
                        if(hourOfDay < 10) {
                            sHour = "0" + hourOfDay;
                        }else {
                            sHour = hourOfDay + "";
                        }
                        if(minute < 10) {
                            sMinute = "0" + minute;
                        }else {
                            sMinute = minute + "";
                        }
                        String time = sHour + ":" + sMinute;
                        textView.setText(time);
                    }
                }, mHour, mMinute, true);
    }

    /**
     * 需要选择时间的时候就调用这个方法，精确到分
     * @param that 用来传Activity
     * @param sp 接受控件
     * created by cj
     */
    public static void setTime(final Activity that, final Spinner sp){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        final MyTimePickerDialog timePickerDialog = new MyTimePickerDialog(that,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view,
                                          int hourOfDay, int minute) {
                        String sHour;
                        String sMinute;
                        if(hourOfDay < 10) {
                            sHour = "0" + hourOfDay;
                        }else {
                            sHour = hourOfDay + "";
                        }
                        if(minute < 10) {
                            sMinute = "0" + minute;
                        }else {
                            sMinute = minute + "";
                        }
                        String time = sHour + ":" + sMinute;
                        List<String> adapter = new ArrayList<String>();
                        adapter.add(time);
                        sp.setAdapter(new ArrayAdapter<String>(that, android.R.layout.simple_spinner_dropdown_item, adapter));

                    }
                }, mHour, mMinute, true);
        timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        timePickerDialog.show();
    }

    public static void setDate(final Activity that, final AbsSpinner sp) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        MyDatePickerDialog datePickerDialog = new MyDatePickerDialog(that, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                dateStr = year + "-" + (monthOfYear+1) + "-" + dayOfMonth + " ";
                List<String> adapter = new ArrayList<String>();
                adapter.add(dateStr);
                sp.setAdapter(new ArrayAdapter<String>(that, android.R.layout.simple_spinner_dropdown_item, adapter));

            }
        }, mYear, mMonth, mDay);
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }

        });
        datePickerDialog.show();
    }

    public static MyDatePickerDialog getDateDialog(final Activity that, final Spinner sp){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        MyDatePickerDialog datePickerDialog = new MyDatePickerDialog(that, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                dateStr = year + "-" + (monthOfYear+1) + "-" + dayOfMonth + " ";
                List<String> adapter = new ArrayList<String>();
                adapter.add(dateStr);
                sp.setAdapter(new ArrayAdapter<String>(that, android.R.layout.simple_spinner_dropdown_item, adapter));
            }
        }, mYear, mMonth, mDay);
            datePickerDialog.show();
        return datePickerDialog;
    }

    public static String monthBefore(Date date) {
        return new SimpleDateFormat(TimeCursor.INNER_DATAFORMAT).format(date.getTime() + 1000 * 60 * 60 * 24 * 30);
    }


}
