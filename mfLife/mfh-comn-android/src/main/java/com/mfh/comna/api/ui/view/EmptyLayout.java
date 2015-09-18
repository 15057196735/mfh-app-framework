package com.mfh.comna.api.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mfh.comna.api.utils.NetWorkUtil;
import com.mfh.comna.R;
import com.mfh.comna.widget.LoadingImageView;

public class EmptyLayout extends LinearLayout implements
        View.OnClickListener {// , ISkinUIObserver {

    public static final int HIDE_LAYOUT = 4;
    public static final int NETWORK_ERROR = 1;
    public static final int NETWORK_LOADING = 2;
    public static final int NODATA = 3;
    public static final int NODATA_ENABLE_CLICK = 5;
    public static final int NO_LOGIN = 6;
    //扩展
    public static final int BIZ_LOADING = 7;

    //进度条
    private int loadingTheme = 0;//样式
    private LoadingImageView loadingImageView;//样式0
    private ProgressBar animProgress;//样式1

    private boolean clickEnable = true;
    private final Context context;
    public ImageView img;//图片
    private OnClickListener listener;
    private int mErrorState;
    private RelativeLayout mLayout;
    private String strNoDataContent = "";
    private String strLoadingContent = "";
    private TextView tv;//文字提示

    public EmptyLayout(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        View view = View.inflate(context, R.layout.view_error_layout, null);
        img = (ImageView) view.findViewById(R.id.img_error_layout);
        tv = (TextView) view.findViewById(R.id.tv_error_layout);
        mLayout = (RelativeLayout) view.findViewById(R.id.pageerrLayout);

        loadingImageView = (LoadingImageView) view.findViewById(R.id.loadingImageView);
        loadingImageView.setBackgroundResource(com.mfh.comna.R.drawable.loading_anim);
        animProgress = (ProgressBar) view.findViewById(R.id.animProgress);

        setBackgroundColor(-1);
        setOnClickListener(this);
        img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (clickEnable) {
                    // setErrorType(NETWORK_LOADING);
                    if (listener != null)
                        listener.onClick(v);
                }
            }
        });
        addView(view);
        changeErrorLayoutBgMode(context);
    }

    public void changeErrorLayoutBgMode(Context context1) {
        // mLayout.setBackgroundColor(SkinsUtil.getColor(context1,
        // "bgcolor01"));
        // tv.setTextColor(SkinsUtil.getColor(context1, "textcolor05"));
    }

    public void dismiss() {
        mErrorState = HIDE_LAYOUT;
        setVisibility(View.GONE);
    }

    public int getErrorState() {
        return mErrorState;
    }

    public boolean isLoadError() {
        return mErrorState == NETWORK_ERROR;
    }

    public boolean isLoading() {
        return mErrorState == NETWORK_LOADING;
    }

    @Override
    public void onClick(View v) {
        if (clickEnable) {
            // setErrorType(NETWORK_LOADING);
            if (listener != null)
                listener.onClick(v);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // ComnApplication.getInstance().getAtSkinObserable().registered(this);
        onSkinChanged();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // ComnApplication.getInstance().getAtSkinObserable().unregistered(this);
    }

    public void onSkinChanged() {
        // mLayout.setBackgroundColor(SkinsUtil
        // .getColor(getContext(), "bgcolor01"));
        // tv.setTextColor(SkinsUtil.getColor(getContext(), "textcolor05"));
    }

    public void setDayNight(boolean flag) {}

    public void setErrorMessage(String msg) {
        tv.setText(msg);
    }

    /**
     * 新添设置背景
     * 
     * @author 火蚁 2015-1-27 下午2:14:00
     * 
     */
    public void setErrorImag(int imgResource) {
        try {
            img.setImageResource(imgResource);
        } catch (Exception e) {
        }
    }

    public void setErrorType(int i) {
        setVisibility(View.VISIBLE);
        switch (i) {
        case NETWORK_ERROR:
            mLayout.setBackgroundColor(Color.parseColor("#E8E8E7"));
            mErrorState = NETWORK_ERROR;
            // img.setBackgroundDrawable(SkinsUtil.getDrawable(context,"pagefailed_bg"));
            if (NetWorkUtil.isConnect(getContext())) {
                tv.setText(R.string.error_view_load_error_click_to_refresh);
                img.setBackgroundResource(R.drawable.pagefailed_bg);
            } else {
                tv.setText(R.string.error_view_network_error_click_to_refresh);
                img.setBackgroundResource(R.drawable.page_icon_network);
            }
            img.setVisibility(View.VISIBLE);
            if(loadingTheme == 0){
                loadingImageView.toggle(false);
            }else{
                animProgress.setVisibility(View.GONE);
            }
            clickEnable = true;
            break;
        case NETWORK_LOADING:
            mLayout.setBackgroundColor(Color.parseColor("#E8E8E7"));
            mErrorState = NETWORK_LOADING;
            if(loadingTheme == 0){
                loadingImageView.toggle(true);
            }else{
                animProgress.setVisibility(View.VISIBLE);
            }
            img.setVisibility(View.GONE);
//            tv.setText(R.string.error_view_loading);
            setTvLoadingContent();
            clickEnable = false;
            break;
        case NODATA:
            mLayout.setBackgroundColor(Color.parseColor("#E8E8E7"));
            mErrorState = NODATA;
            // img.setBackgroundDrawable(SkinsUtil.getDrawable(context,"page_icon_empty"));
            img.setBackgroundResource(R.drawable.page_icon_empty);
            img.setVisibility(View.VISIBLE);
            if(loadingTheme == 0){
                loadingImageView.toggle(false);
            }else{
                animProgress.setVisibility(View.GONE);
            }
            setTvNoDataContent();
            clickEnable = true;
            break;
        case HIDE_LAYOUT:
            mLayout.setBackgroundColor(Color.parseColor("#E8E8E7"));
            setVisibility(View.GONE);
            break;
        case NODATA_ENABLE_CLICK:
            mLayout.setBackgroundColor(Color.parseColor("#E8E8E7"));
            mErrorState = NODATA_ENABLE_CLICK;
            img.setBackgroundResource(R.drawable.page_icon_empty);
            // img.setBackgroundDrawable(SkinsUtil.getDrawable(context,"page_icon_empty"));
            img.setVisibility(View.VISIBLE);
            if(loadingTheme == 0){
                loadingImageView.toggle(false);
            }else{
                animProgress.setVisibility(View.GONE);
            }
            setTvNoDataContent();
            clickEnable = true;
            break;
        case BIZ_LOADING:
//            mLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
            mLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//            mLayout.setBackground(null);
            mErrorState = BIZ_LOADING;
            if(loadingTheme == 0){
                loadingImageView.toggle(true);
            }else{
                animProgress.setVisibility(View.VISIBLE);
            }
            img.setVisibility(View.GONE);
//            tv.setText(R.string.error_view_loading);
            setTvLoadingContent();
            clickEnable = false;
            break;
        default:
            mLayout.setBackgroundColor(Color.parseColor("#E8E8E7"));
            break;
        }
    }

    public void setNoDataContent(String noDataContent) {
        strNoDataContent = noDataContent;
    }
    public void setLoadingContent(String loadingContent) {
        strLoadingContent = loadingContent;
    }

    public void setOnLayoutClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void setTvNoDataContent() {
        if (!strNoDataContent.equals(""))
            tv.setText(strNoDataContent);
        else
            tv.setText(R.string.error_view_no_data);
    }

    /**
     * loading text
     * 默认显示 R.string.error_view_loading
     * */
    public void setTvLoadingContent() {
        if(strLoadingContent != null){
            tv.setText(strLoadingContent);
        } else{
            tv.setText(R.string.error_view_loading);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.GONE)
            mErrorState = HIDE_LAYOUT;
        super.setVisibility(visibility);
    }

    /**
     * 设置进度条样式
     * 0,loadingImageView
     * 1,progressBar
     * */
    public void setLoadingTheme(int theme){
        this.loadingTheme = theme;
    }
}
