package com.mfh.comna.bizz.msg.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mfh.comna.R;
import com.mfh.comna.bizz.msg.wx.WXMenuData;
import com.mfh.comna.api.web.NativeWebViewActivity;

import java.util.List;

/**
 * 对话--微信菜单
 * Created by Administrator on 2015/5/10.
 */
public class ChatMenuWX extends FrameLayout{

    private View rootView;
    private Button buttonToggle;
    private LinearLayout[] rlMenus;
    private Button[] buttonMenus;

    private List<WXMenuData> majorMenus;

    public interface ChatMenuWXListerner{
        void onPreHide();
        void onHiden();
        void popupSubMenus(View parentView, List<WXMenuData> menus);
    }
    private ChatMenuWXListerner listener;
    public void setListener(ChatMenuWXListerner listener){
        this.listener = listener;
    }


    public ChatMenuWX(Context context, AttributeSet attrs) {
        super(context, attrs);
        rootView = LayoutInflater.from(context).inflate(R.layout.chat_menu_wx, this, true);
        this.initAndSetUpView();
    }

    private void initAndSetUpView() {
        buttonToggle = (Button) rootView.findViewById(R.id.button_toggle);
        rlMenus = new LinearLayout[3];
        rlMenus[0] = (LinearLayout) rootView.findViewById(R.id.rl_menu_0);
        rlMenus[1] = (LinearLayout) rootView.findViewById(R.id.rl_menu_1);
        rlMenus[2] = (LinearLayout) rootView.findViewById(R.id.rl_menu_2);

        buttonMenus = new Button[3];
        buttonMenus[0] = (Button) rootView.findViewById(R.id.button_menu_0);
        buttonMenus[1] = (Button) rootView.findViewById(R.id.button_menu_1);
        buttonMenus[2] = (Button) rootView.findViewById(R.id.button_menu_2);

//        buttonMenu[0].setText("团购");//group buying
//        buttonMenu[1].setText("物业");//property
//        buttonMenu[2].setText("我");//me

        buttonToggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        for(int i = 0; i < rlMenus.length; i++){
            final int index  = i;
            rlMenus[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    WXMenuData wxMenuData = majorMenus.get(index);
                    if(wxMenuData.getSubList() != null && wxMenuData.getSubList().size() > 0){
                        if(listener != null){
                            listener.popupSubMenus(view, majorMenus.get(index).getSubList());
                        }
                    }else{
                        //TODO,无子菜单，直接跳转至网页
                        if(wxMenuData.getUrl() != null){
                            Intent intent = new Intent(getContext(), NativeWebViewActivity.class);
                            intent.putExtra("redirectUrl", wxMenuData.getUrl());
                            intent.putExtra("syncCookie", true);
                            getContext().startActivity(intent);
                        }
                    }
                }
            });
        }
    }

    public void setOnClickListener(OnClickListener listener){
        for(LinearLayout ll : rlMenus){
            ll.setOnClickListener(listener);
        }
    }

    /**
     * 隐藏菜单
     * */
    private void toggle(){
        final AnimationSet animdn = new AnimationSet(true);
        final TranslateAnimation mytranslateanimdn0 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                +1.0f);
        mytranslateanimdn0.setDuration(100);
        animdn.addAnimation(mytranslateanimdn0);

        rootView.startAnimation(animdn);

        // 动画监听，开始时显示加载状态，
        mytranslateanimdn0.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(listener != null){
                    listener.onPreHide();
                }
//                        rootView.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                rootView.setVisibility(View.INVISIBLE);
                if(listener != null){
                    listener.onHiden();
                }
            }
        });
    }

    public List<WXMenuData> getMajorMenus() {
        return majorMenus;
    }

    /**
     * 设置菜单
     * */
    public void setMajorMenus(List<WXMenuData> majorMenus) {
        this.majorMenus = majorMenus;

        for(int i = 0; i < rlMenus.length; i++){
            rlMenus[i].setVisibility(View.GONE);
        }

        if(majorMenus != null){
            switch(majorMenus.size()){
                case 3:
                    WXMenuData menu = majorMenus.get(2);
                    buttonMenus[2].setText(menu.getName());
                    rlMenus[2].setVisibility(View.VISIBLE);
                case 2:
                    buttonMenus[1].setText(majorMenus.get(1).getName());
                    rlMenus[1].setVisibility(View.VISIBLE);
                case 1:
                    buttonMenus[0].setText(majorMenus.get(0).getName());
                    rlMenus[0].setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

}
