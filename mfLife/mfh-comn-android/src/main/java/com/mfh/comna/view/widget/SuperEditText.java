package com.mfh.comna.view.widget;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * EditText强化版，可自由定制各种属性
 * Created by yxm on 2014/9/26.
 */
public class SuperEditText extends EditText {
    private int len;
    private int intTempMax;
    private Float floatTempMax;
    private String tempStr;//允许输入的字符

    /**
     * 构造方法
     *
     * @param context
     */
    public SuperEditText(Context context) {
        super(context);
    }

    public SuperEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public SuperEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    /**
     * 设置允许输入的字符
     *
     * @param str
     */
    public void setAcceptedChars(String str) {
        tempStr = str;
        setFilters(new InputFilter[]{
                new InputFilter() {
                    /**
                     * @param charSequence 输入的文字
                     * @param i 开始位置
                     * @param i2 结束位置
                     * @param spanned 当前显示的内容
                     * @param i3 当前开始位置
                     * @param i4 当前结束位置
                     * @return
                     */
                    @Override
                    public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
                        if (!contains(tempStr, charSequence.toString())) {
                            return "";
                        }
                        return null;
                    }
                }
        });
    }

    /**
     * 设置最大值
     *
     * @param max
     */
    public void setMax(int max) {
        setInputType(InputType.TYPE_CLASS_NUMBER);
        setAcceptedChars("1234567890");
        len = String.valueOf(max).length() - 1;
        intTempMax = max;
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > len) {
                    if (Integer.valueOf(editable.toString()) > intTempMax) {
                        setText(String.valueOf(intTempMax));
                        Toast.makeText(getContext(), "金额不能大于" + String.valueOf(intTempMax), Toast.LENGTH_SHORT).show();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(getText(), getText().length());
                }
            }
        });
    }

    /**
     * @param max
     * @param fraction 允许的小数位数
     */
    public void setMax(Float max, final int fraction) {
        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        setAcceptedChars("1234567890.");
        len = String.valueOf(max.intValue()).length() - 1;
        floatTempMax = max;
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (charCount(editable.toString(), '.') > 1) {
                    setText("");
                    Toast.makeText(getContext(), "非法输入", Toast.LENGTH_SHORT).show();
                }
                if (editable.length() > len) {
                    if (charCount(editable.toString(), '.') == 1) {
                        if ((editable.length() - 1 - editable.toString().lastIndexOf(".")) > fraction) {
                            setText(editable.toString().substring(0,
                                    editable.toString().lastIndexOf(".") + 3));
                            Toast.makeText(getContext(), "小数部分不可大于" + String.valueOf(fraction) +"位", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (charCount(editable.toString(), '.') <= 1) {
                        if (Float.valueOf(editable.toString()) > floatTempMax) {
                            setText(String.valueOf(floatTempMax));
                            Toast.makeText(getContext(), "金额不能大于" + String.valueOf(floatTempMax), Toast.LENGTH_SHORT).show();
                        }
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(getText(), getText().length());
                }
            }
        });
    }

    /**
     * 是否自动弹出键盘
     *
     * @param b
     */
    public void showSoftInput(Boolean b) {
        if (b == true) {
            setFocusable(true);
            setFocusableInTouchMode(true);
            requestFocus();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    show();
                }
            }, 200);
        }
    }

    public void show() {
        InputMethodManager inputManager = (InputMethodManager)
                getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(this, 0);
    }

    /**
     * 找个一个字符串中某个字符出现的次数
     *
     * @param s
     * @param c
     * @return
     */
    public int charCount(String s, char c) {
        int len = s.length();
        int count = 0;
        for (int i = 0; i < len; i++) {
            if (s.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    /**
     * 判定输入的字符是否为允许输入的字符
     *
     * @param str1 允许输入的字符
     * @param str2 输入的字符
     * @return
     */
    public Boolean contains(String str1, String str2) {
        for (int i = 0; i < str2.length(); i++) {
            if (!str1.contains(str2.subSequence(i, i + 1))) {
                return false;
            }
        }
        return true;
    }
}
