package com.mfh.comna.view.widget;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Spinner;

/**
 * The class inherited from Spinner. The drop-down list has the following characteristics:
 * <ol>
 * <li> Use{@link #setOnClickListener(OnClickListener)} to listen the click event and can
 * implement self-fulfilling click response action (Spinner's default response action is to open the
 * list selection box).</li>
 * <li> Click event occurs, the default response is no longer open the list selection box, please
 * call {@link #performClick()} instead.</li>
 * </ol>
 *
 * @author yxm
 * @date 2014/10/28
 */
public class ClickControlledSpinner extends Spinner {
    private boolean isMoved = false;
    private Point touchedPoint = new Point();
    private OnClickMyListener onClickMyListener;

    public ClickControlledSpinner(Context context) {
        super(context);
    }

    public ClickControlledSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ClickControlledSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickControlledSpinner(Context context, int mode) {
        super(context, mode);
    }

    public ClickControlledSpinner(Context context, AttributeSet attrs, int defStyle, int mode) {
        super(context, attrs, defStyle, mode);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchedPoint.x = x;
                touchedPoint.y = y;
                break;
            case MotionEvent.ACTION_MOVE:
                isMoved = true;
                break;
            case MotionEvent.ACTION_UP:
                if (isMoved) {
                    //slide from top down
                    if (y - touchedPoint.y > 20) {

                    }
                    //slide from the bottom up
                    else if (touchedPoint.y - y > 20) {

                    }
                    //a small amplitude slide event is considered as a click event
                    else {
                        onClick();
                    }
                    isMoved = false;
                } else {
                    onClick();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void onClick() {
        if (onClickMyListener != null && isEnabled()) {
            onClickMyListener.onClick(); 
        }
    }

    public void setOnClickMyListener(OnClickMyListener onClickMyListener) {
        this.onClickMyListener = onClickMyListener;
    }

    public interface OnClickMyListener {
        public void onClick();
    }
}
