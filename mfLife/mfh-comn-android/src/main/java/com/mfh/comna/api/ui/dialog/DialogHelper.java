package com.mfh.comna.api.ui.dialog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.mfh.comna.R;

/**
 * Created by Administrator on 2015/5/28.
 */
public class DialogHelper {
    /***
     * 获取一个dialog
     * @param context
     * @return
     */
    public static AlertDialog.Builder getDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder;
    }

    public static CommonDialog getPinterestDialog(Context context) {
        return new CommonDialog(context, R.style.dialog_common);
    }

    public static CommonDialog getPinterestDialogCancelable(Context context) {
        CommonDialog dialog = new CommonDialog(context,
                R.style.dialog_common);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    public static AlertDialog.Builder getConfirmDialog(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(Html.fromHtml(message));
        builder.setPositiveButton(context.getString(R.string.dialog_button_ok), onClickListener);
        builder.setNegativeButton(context.getString(R.string.dialog_button_cancel), null);
//        builder.setNeutralButton("取消", null);
        return builder;
    }

    public static AlertDialog.Builder getConfirmDialog(Context context, String message,
                                                       String positiveText, DialogInterface.OnClickListener positiveClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(Html.fromHtml(message));
        builder.setPositiveButton(positiveText, positiveClickListener);
        builder.setNegativeButton(context.getString(R.string.dialog_button_cancel), null);
//        builder.setNeutralButton("取消", null);
        return builder;
    }

    /**
     * 生成进度框
     * @param context
     * @param meaasge 提示信息 设置为null 默认显示正在加载
     * @param isScreen 是否全屏
     * @return
     */
    public static ProgressDialog genProgressDialog(Context context, boolean isScreen, String meaasge) {
        ProgressDialog dialog;
        if (isScreen) {
            dialog = new ProgressDialog(context,R.style.Transparent_wait_screen);
        }else  {
            dialog =  new ProgressDialog(context);
        }
        View view = View.inflate(context,R.layout.activity_wait,null);
        TextView textView = (TextView) view.findViewById(R.id.tv_meaasge);
        if (meaasge != null)
            textView.setText(meaasge);
        dialog.show();
        dialog.setContentView(view);
        dialog.setCancelable(false);
//        dialog.show();
        return dialog;
    }
}
