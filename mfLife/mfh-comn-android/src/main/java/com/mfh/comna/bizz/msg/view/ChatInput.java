package com.mfh.comna.bizz.msg.view;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import com.mfh.comna.R;
import com.mfh.comna.bizz.msg.MsgConstants;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Shicy on 14-3-20.
 */
public class ChatInput extends FrameLayout implements View.OnClickListener {
    private View rootView;
    private EditText input;
    private Button buttonMore;
    private Button buttonSend;
    private Button buttonFace;
    private Button buttonVoice;
    private Button buttonToggle;

    public interface ChatInputListerner{
        void onPreHide();
        void onHiden();
    }
    private ChatInputListerner listener;
    public void setListener(ChatInputListerner listener){
        this.listener = listener;
    }

    public ChatInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        rootView = LayoutInflater.from(context).inflate(R.layout.chat_subview_input, this, true);
        this.initAndSetUpView();
    }

    private void initAndSetUpView() {
        input = (EditText) rootView.findViewById(R.id.chat_input);
        buttonToggle = (Button) rootView.findViewById(R.id.button_toggle);
        buttonMore = (Button) rootView.findViewById(R.id.button_add);
        buttonSend = (Button) rootView.findViewById(R.id.button_send);
        buttonFace = (Button) rootView.findViewById(R.id.chat_face);
        buttonVoice = (Button) rootView.findViewById(R.id.button_voice);

        final AnimationSet animdn = new AnimationSet(true);
        final TranslateAnimation mytranslateanimdn0 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                +1.0f);
        mytranslateanimdn0.setDuration(100);
        animdn.addAnimation(mytranslateanimdn0);

        buttonToggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                rootView.startAnimation(animdn);

                // 动画监听，开始时显示加载状态，
                mytranslateanimdn0.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
//                        rootView.setVisibility(View.VISIBLE);
                        if(listener != null){
                            listener.onPreHide();
                        }
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
        });

        // 监听文字框
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (!TextUtils.isEmpty(charSequence)) {
                    buttonMore.setVisibility(GONE);
                    buttonSend.setVisibility(VISIBLE);
                }
                else {
                    buttonMore.setVisibility(VISIBLE);
                    buttonSend.setVisibility(GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        input.setOnClickListener(this);
        //首次点击对话页面“表情”按键，表情面板不显示问题
//        input.setOnFocusChangeListener(new OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
////                Log.d("Nat: onFocusChange", (b ? "focus = true" : "focus = false"));
//                if(true){
//                    Intent intent = new Intent(MsgConstants.ACTION_BEGIN_INPUT);
//                    getContext().sendBroadcast(intent);
//                }
//            }
//        } );
        input.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
//                Log.d("Nat: onTouch", String.format("event.action = %d", event.getAction()));
                if(event.getAction() == MotionEvent.ACTION_UP){
                    Intent intent = new Intent(MsgConstants.ACTION_BEGIN_INPUT);
                    getContext().sendBroadcast(intent);
                }

                return false;
            }
        });

    }

    public String getText() {
        return input.getText().toString();
    }

    /**
     * 清空现有输入
     */
    public void clearText() {
        input.setText("");
    }

    public void setOnClickListener(OnClickListener listener){
        buttonMore.setOnClickListener(listener);
        buttonSend.setOnClickListener(listener);
        buttonFace.setOnClickListener(listener);
        buttonVoice.setOnClickListener(listener);
//        input.setOnClickListener(listener);
    }

    /**
     * 输入框被点击时
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.chat_input){
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(MsgConstants.ACTION_BEGIN_INPUT);
                    getContext().sendBroadcast(intent);
                }
            }, 250);//延迟一下，是因为要等到输入法界面完全显示后再定位才精确。
        }
    }

    /**
     * 设置是否支持切换菜单
     * */
    public void setToggleEnable(boolean enabled){
        if(enabled){
            buttonToggle.setVisibility(View.VISIBLE);
        }else{
            buttonToggle.setVisibility(View.GONE);
        }
    }

    public void setFaceButtonSelected(boolean selected){
        buttonFace.setSelected(selected);
    }

    public EditText getEditText() {
        return input;
    }

}
