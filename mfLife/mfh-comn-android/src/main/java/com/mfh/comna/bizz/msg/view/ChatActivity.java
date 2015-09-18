package com.mfh.comna.bizz.msg.view;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mfh.comna.api.Constants;
import com.mfh.comna.api.helper.AppHelper;
import com.mfh.comna.api.helper.SharedPreferencesHelper;
import com.mfh.comna.api.helper.UIHelper;
import com.mfh.comna.api.utils.DeviceUtils;
import com.mfh.comna.api.utils.ImageUtil;
import com.mfh.comna.api.utils.MLog;
import com.mfh.comna.api.web.NativeWebViewActivity;
import com.mfh.comna.bizz.login.MsgBridgeUtil;
import com.mfh.comna.bizz.login.logic.MfhLoginService;
import com.mfh.comna.bizz.material.dao.ResourceNetDao;
import com.mfh.comna.bizz.material.view.MaterialFaceController;
import com.mfh.comna.bizz.material.view.MaterialFunctionController;
import com.mfh.comna.bizz.material.view.Option;
import com.mfh.comna.bizz.msg.MsgConstants;
import com.mfh.comna.bizz.msg.WXMenuListAdapter;
import com.mfh.comna.bizz.msg.entity.EmbMsg;
import com.mfh.comna.bizz.msg.entity.EmbSession;
import com.mfh.comna.bizz.msg.entity.ImageParam;
import com.mfh.comna.bizz.msg.entity.TextParam;
import com.mfh.comna.bizz.msg.logic.EmbMsgService;
import com.mfh.comna.bizz.msg.logic.EmbSessionService;
import com.mfh.comna.bizz.msg.logic.MsgHelper;
import com.mfh.comna.bizz.msg.wx.WXMenu;
import com.mfh.comna.bizz.msg.wx.WXMenuData;
import com.mfh.comn.bean.TimeCursor;
import com.mfh.comn.utils.FileUtils;
import com.mfh.comna.R;
import com.mfh.comna.comn.database.dao.FileNetDao;
import com.mfh.comna.comn.database.dao.NetProcessor;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.utils.CameraSessionUtil;
import com.mfh.comna.utils.ClickFilter;
import com.mfh.comna.utils.FaceUtil;
import com.mfh.comna.view.BaseFragmentActive;
import com.mfh.comna.bizz.member.MemberConstants;

import net.tsz.afinal.http.AjaxParams;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 对话
 * Created by Shicy on 14-3-20.
 */
public class ChatActivity extends BaseFragmentActive {
    public static final String EXTRA_KEY_SESSION_ID = "sessionId";//会话编号
    public static final String EXTRA_KEY_SESSION_TYPE = "chatType";//会话类型：商家/个人
    public static final String EXTRA_KEY_LAST_UPDATE = "lastupdate";
    public static final String EXTRA_KEY_MSG_MODE = "msgMode";

    private ImageButton ibBack;
    private TextView tvTopBarTitle;
    private Button btnMore;
    private ChatMessageFragment messageFragment;
    private ChatInput chatInput;
    private ChatMenuWX chatMenuWX;
    private MaterialFunctionController materialFunctionController;
    private MaterialFaceController materialFaceController;

    private EmbSessionService sessionService;
    private EmbMsgService msgService;
    private BroadcastReceiver receiver;

    private Long sessionId = null;//会话编号
    private Long lastupdate;//用于第一次进入消息列表时，请求最后一条消息。

    InputMethodManager inputMethodManager;

    private Integer fragmentId = null;
    protected String class_name;//获得类名

    private ResourceNetDao resourceDao;
    private int msgMode = -1;

    //对话类型：1:客服 2：商家 other：个人
    private int chatType = -1;
    //对话名称：客服/商家显示客服/商家的名字，个人显示对话
    private String chatHumanName;

    public static void actionStart(Context context, Long sessionId){
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_KEY_SESSION_ID, sessionId);
        context.startActivity(intent);
    }

    public static void actionStart(Context context, int chatType, EmbSession bean, int msgMode){
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_KEY_SESSION_TYPE, chatType);
        if(bean != null){
            intent.putExtra(EXTRA_KEY_SESSION_ID, bean.getId());
            intent.putExtra("humanName", bean.getHumanname());
            intent.putExtra("humanId", bean.getHumanid());
            intent.putExtra("headImgUrl", bean.getLocalheadimageurl());
            intent.putExtra("unReadCount", Integer.valueOf(String.valueOf(bean.getUnreadcount())));
            intent.putExtra(EXTRA_KEY_LAST_UPDATE, bean.getLastupdate());
        }

        intent.putExtra("msgMode", msgMode);

        context.startActivity(intent);
    }

    /**
     * create pendingIntent for notification
     * */
    public static PendingIntent generatePendingIntent(Context context, Long sessionId){
        Intent intent =new Intent(context, ChatActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FILL_IN_DATA);
        intent.setAction(String.valueOf(System.currentTimeMillis()));
        intent.putExtra(EXTRA_KEY_SESSION_ID, sessionId);

        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        msgService = ServiceFactory.getService(EmbMsgService.class, this);
        sessionService = ServiceFactory.getService(EmbSessionService.class);

        Intent intent = getIntent();
        if(intent != null){
            chatType = intent.getIntExtra(EXTRA_KEY_SESSION_TYPE, -1);
            sessionId = intent.getLongExtra(EXTRA_KEY_SESSION_ID, -1L);
            lastupdate = intent.getLongExtra(EXTRA_KEY_LAST_UPDATE, -1L);
            msgMode = this.getIntent().getExtras().getInt(EXTRA_KEY_MSG_MODE);
        }

        if (class_name == null || "".equals(class_name))
            class_name = this.getIntent().getStringExtra(MemberConstants.PMB_MEMBER_DETAIL_ACTIVITY_CLASS_NAME);

        sessionService.resetUnReadMsgCount(sessionId);// 把此会话的未读数置为0
        clearNotification(sessionId);

        //TODO,优化Fragment切换。
        if (savedInstanceState == null) {
            messageFragment = new ChatMessageFragment();//默认此时fragmentId为0
            Bundle bundle = new Bundle();
            bundle.putLong(EXTRA_KEY_SESSION_ID, sessionId);
            bundle.putString("msg_pmc_member", class_name);
            bundle.putInt(EXTRA_KEY_MSG_MODE, msgMode);
            messageFragment.setArguments(bundle);
            this.showFragment(R.id.messagesView, messageFragment, true);
            fragmentId = messageFragment.getId();
        }
        else {
            fragmentId = savedInstanceState.getInt("fragmentId");
            messageFragment = (ChatMessageFragment)this.getSupportFragmentManager().findFragmentById(fragmentId);
            messageFragment.setSessionId(sessionId);
        }

        initTopBar();
        findViewById(R.id.messagesView).setOnClickListener(this);

        final AnimationSet animdn = new AnimationSet(true);
        final TranslateAnimation mytranslateanimdn1 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, +1.0f, Animation.RELATIVE_TO_SELF,
                0f);
        mytranslateanimdn1.setDuration(100);
        animdn.addAnimation(mytranslateanimdn1);

        chatInput = (ChatInput) findViewById(R.id.chatInputBar);
        chatInput.setOnClickListener(this);
        chatInput.setListener(new ChatInput.ChatInputListerner() {
            @Override
            public void onPreHide() {
                chatInput.clearText();
                hideMaterialViews();
            }

            @Override
            public void onHiden() {
                chatMenuWX.startAnimation(animdn);

                // 动画监听，开始时显示加载状态，
                mytranslateanimdn1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        chatMenuWX.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
//                        chatMenuWX.setVisibility(View.GONE);
//                        if(listener != null){
//                            listener.onHide();
//                        }
                    }
                });
            }
        });


        chatMenuWX = (ChatMenuWX) findViewById(R.id.wxMenuBar);
        chatMenuWX.setListener(new ChatMenuWX.ChatMenuWXListerner() {

            @Override
            public void onPreHide() {
//                chatInput.setVisibility(View.GONE);
            }

            @Override
            public void onHiden() {
                chatInput.startAnimation(animdn);

                // 动画监听，开始时显示加载状态，
                mytranslateanimdn1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        chatInput.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
//                        chatInput.setVisibility(View.GONE);
//                        if(listener != null){
//                            listener.onHide();
//                        }
                    }
                });
            }

            @Override
            public void popupSubMenus(View parentView, List<WXMenuData> menus) {
                showWXPopupMenu(parentView, menus);
            }
        });

        materialFaceController = (MaterialFaceController) findViewById(R.id.chat_face_view);
        materialFaceController.setCallBack(new MaterialFaceController.CallBack() {
           @Override
           public void faceImageOnClick(int i) {
               if (i == 20) {
                   KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DEL);
                   chatInput.dispatchKeyEvent(keyEvent);
                   return;
               }
               String inputStr = chatInput.getText() + FaceUtil.getFaceString(i);
//               String face = FaceUtil.getFaceString(i);
//               chatInput.getEditText().setText(chatInput.getText() + face);
//               SpannableString spannableString = FaceUtil.getSpannable(ChatActivity.this, chatInput.getText(), 25, 25);
               SpannableString spannableString = FaceUtil.getSpannable(ChatActivity.this, inputStr, 25, 25);
               chatInput.getEditText().setText(spannableString);
               chatInput.getEditText().requestFocus();
               chatInput.getEditText().setSelection(chatInput.getText().length());
           }
       });

        initMaterialController();

        registerMsgReceiver();
        //msgTimer = ServiceFactory.getService(MsgTimer.class.getName());

        //设置是否显示微信菜单。
        if(chatType == 1 || chatType == 2){
            requestWXMenu();

            //TODO,如果没有就不显示
            chatInput.setToggleEnable(chatMenuWX.isEnabled());
        }else{
            chatInput.setToggleEnable(false);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null)
            outState.putInt("fragmentId", fragmentId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }


    @Override
    protected void onStop() {
        //session.setUnReadCount(0);
        EmbSession conversation = sessionService.getDao().getEntityById(this.sessionId);
        if(conversation != null){
            sessionService.getDao().saveOrUpdate(conversation);
        }

        MsgHelper.sendBroadcastForUpdateUnread(this, 0);

        sessionService.resetUnReadMsgCount(sessionId);// 把此会话的未读数置为0
        super.onStop();
    }

    /**
     * 初始化导航栏视图
     * */
    private void initTopBar(){
        tvTopBarTitle = (TextView) findViewById(R.id.topbar_title);
        ibBack = (ImageButton) findViewById(R.id.ib_back);
        btnMore = (Button) findViewById(R.id.btnMore);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, StoreDetailActivity.class);
//                intent.putExtra(ConversationActivity.EXTRA_KEY_CONVERSATION_TYPE, i);
                startActivity(intent);
            }
        });
        //TODO,根据当前对话类型，设置右侧图标
        if(chatType == 1){
            btnMore.setBackgroundResource(R.drawable.actionbar_store_indicator);
            tvTopBarTitle.setText(R.string.topbar_title_chat);
        }
        else if(chatType == 2){
            btnMore.setBackgroundResource(R.drawable.actionbar_store_indicator);
            tvTopBarTitle.setText(R.string.topbar_title_chat);
        }else {
            btnMore.setBackgroundResource(R.drawable.actionbar_individual_indicator);
            tvTopBarTitle.setText(R.string.topbar_title_chat);
//            ibMore.setVisibility(View.GONE);
        }
        btnMore.setVisibility(View.VISIBLE);
    }


    /**
     * 初始化素材面板视图
     * */
    private void initMaterialController(){
        materialFunctionController =  (MaterialFunctionController) findViewById(R.id.matetial_function_view);
        List<Option> functionData = new ArrayList<Option>();
        functionData.add(new Option(this, Option.OPTION_MODE_PICTURE,
                getString(R.string.label_material_picture), R.drawable.material_picture));
        functionData.add(new Option(this, Option.OPTION_MODE_SHOOT,
                getString(R.string.label_material_shoot), R.drawable.material_shoot));
        functionData.add(new Option(this, Option.OPTION_MODE_BUSINESS_CARD,
                getString(R.string.label_material_namecard), R.drawable .material_business_card));
        functionData.add(new Option(this, Option.OPTION_MODE_POSITION,
                getString(R.string.label_material_position), R.drawable.material_position));
        functionData.add(new Option(this, Option.OPTION_MODE_FAVORITE, "收藏", R.drawable.material_favorite));
        functionData.add(new Option(this, Option.OPTION_MODE_VIREMENT, "转账", R.drawable.material_virement));
        functionData.add(new Option(this, Option.OPTION_MODE_CARDS, "卡券", R.drawable.material_cards));
        functionData.add(new Option(this, Option.OPTION_MODE_RED_ENVELOPE, "红包", R.drawable.material_red_envelope));

        functionData.add(new Option(this, Option.OPTION_MODE_MILK, "订奶", R.drawable.material_milk));
        functionData.add(new Option(this, Option.OPTION_MODE_FRUITS, "水果", R.drawable.material_fruits));
        functionData.add(new Option(this, Option.OPTION_MODE_CLOTHES, "洗衣", R.drawable.material_clothes));
        functionData.add(new Option(this, Option.OPTION_MODE_FLOWERS, "鲜花", R.drawable.material_flowers));
        functionData.add(new Option(this, Option.OPTION_MODE_FRESH, "海鲜", R.drawable.material_seafood));
        functionData.add(new Option(this, Option.OPTION_MODE_VEGETABLED, "买菜", R.drawable.material_vegetables));
        functionData.add(new Option(this, Option.OPTION_MODE_COMMODITY, "商品", R.drawable.material_commodity));
//        functionData.add(new Option(this, Option.OPTION_MODE_MAINTAIN, "保修", R.drawable.material_maintain));
        materialFunctionController.setFunctionData(functionData);
        materialFunctionController.setCallBack(new MaterialFunctionController.CallBack() {
            @Override
            public void materialSelected(int optionId) {
                switch (optionId){
                    case Option.OPTION_MODE_PICTURE:
                        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                                UIHelper.REQUEST_CODE_XIANGCE);
                        break;
                    case Option.OPTION_MODE_SHOOT:
                        CameraSessionUtil cameraUtil = ServiceFactory.getService(CameraSessionUtil.class.getName());
                        cameraUtil.makeCameraRequest(ChatActivity.this);
                        break;
                    case Option.OPTION_MODE_CARDS:
                        showHint("卡券功能尚未开放");
                        break;
                    case Option.OPTION_MODE_VIREMENT:
                        showHint("转账功能尚未开放");
                        break;
                    case Option.OPTION_MODE_RED_ENVELOPE:
                        showHint("红包功能尚未开放");
                        break;
                    case Option.OPTION_MODE_BUSINESS_CARD:
                        showHint("名片功能尚未开放");
                        break;
                    case Option.OPTION_MODE_FAVORITE:
                        showHint("收藏功能尚未开放");
                        break;
                    case Option.OPTION_MODE_POSITION:
                        showHint("位置功能尚未开放");
                        break;
                    case Option.OPTION_MODE_CLOTHES:
                         showHint("洗衣功能尚未开放");
                        break;
                    case Option.OPTION_MODE_EXPRESS:
                        showHint("快递功能尚未开放");
                        break;
                    case Option.OPTION_MODE_FRESH:
                        showHint("生鲜功能尚未开放");
                        break;
                    case Option.OPTION_MODE_HOUSE_KEEPING:
                        showHint("家政功能尚未开放");
                        break;
                    case Option.OPTION_MODE_FRUITS:
                        showHint("水果功能尚未开放");
                        break;
                    case Option.OPTION_MODE_HOUSE:
                        showHint("房产功能尚未开放");
                        break;
                    case Option.OPTION_MODE_COMPLAINT:
                        showHint("投诉功能尚未开放");
                        break;
                    case Option.OPTION_MODE_MAINTAIN:
                        showHint("报修功能尚未开放");
                        break;
                    default:
//                      showHint("此功能尚未开放");
                        break;
                }
            }
        });
    }

    private void registerMsgReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(MsgConstants.ACTION_BEGIN_INPUT);//接收者只有在activity才起作用。
        filter.addAction(MsgConstants.ACTION_HIDE_MEDIAINPUT);
        filter.addAction(MsgConstants.ACTION_SEND_MSG);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
//                System.out.print(String.format("Nat: onReceive action=%s", action));
                if (MsgConstants.ACTION_BEGIN_INPUT.equals(action)) {
                    messageFragment.scrollToLast();

                    materialFunctionController.setVisibility(View.GONE);
                    materialFaceController.setVisibility(View.GONE);
                    chatInput.setFaceButtonSelected(false);
                }
                /*else if (MsgConstants.ACTION_RECEIVE_MSG_BACK.equals(action)){
                   // showNotification();//发送通知
                }*/
                else if (MsgConstants.ACTION_HIDE_MEDIAINPUT.equals(action)) {
                    materialFunctionController.setVisibility(View.GONE);
                    materialFaceController.setVisibility(View.GONE);
                    chatInput.setFaceButtonSelected(false);
                }
                else if (MsgConstants.ACTION_SEND_MSG.equals(action)) {
                    Object obj = intent.getExtras().get("content");
                    if (obj instanceof  String) {
                        String msg = (String)obj;
                        sendMessage(new TextParam(msg));
                    }
                    else {
                        sendMessage((Long)obj);
                    }
                }
            }
        };
        registerReceiver(receiver, filter);
    }



    //删除通知
    private void clearNotification(Long sessionId){
        // 启动后删除之前我们定义的通知
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(NOTIFICATION_SERVICE);
        if (sessionId != null && sessionId != 0 && !sessionId.equals("") ){
            notificationManager.cancel(Integer.parseInt(String.valueOf(sessionId)));
        }
        else {
            notificationManager.cancel(MsgConstants.MSG_NOTIFICATION);//0,表示消息
        }
    }

    @Override//只要发生切换，一定会调用到stop
    protected void onPause() {
        //不在onstop调用，是因为，onStop之前会先调用MessageSessionFragment的onResume。
       // msgTimer.changeToSessionDirect();
       // msgTimer.addSessionPeroid();
        //unregisterReceiver(receiver);
        super.onPause();
    }



    @Override
    protected void onDestroy() {//如果只是锁屏，不会调用destroy。
        super.onDestroy();
        unregisterReceiver(receiver);
        EmbSession session = sessionService.getDao().getEntityById(this.sessionId);
        if(session != null){
            session.setUnreadcount(0L);
            sessionService.getDao().saveOrUpdate(session);
        }

        MsgHelper.changeSessionUnReadCount(this, sessionId, 0);
    }

    public ResourceNetDao getResourceDao() {
        if (resourceDao == null)
            resourceDao = new ResourceNetDao(this);
        return resourceDao;
    }

    /**
     * 上传指定uri资源到服务器并发送给消息服务器
     * @param uri
     */
    private void doUploadAndSend(Uri uri) {
//        Log.d("Nat: doUploadAndSend", String.format("uri:%s", uri.toString()));
        File newFile = ImageUtil.uriToCompressFile(this, uri);
        doUploadAndSend(newFile);
    }

    private void doUploadAndSend(Bitmap bitmap) {
        File file = ImageUtil.bitMapToCompressFile(bitmap);
        doUploadAndSend(file);
    }

    /**
     * 上传本地图片到服务器并发送给消息服务器
     * @param file 本地文件
     */
    private void doUploadAndSend(File file) {
        AjaxParams params = new AjaxParams();
        getResourceDao().doUpload(params, file, new ResourceNetDao.UploadCallback() {
            @Override
            public void processImg(long imgId, String imgUrl, File srcFile) {
//                Log.d("Nat: 处理图片", String.format("imgUrl=%s", imgUrl));
                ImageParam wxparam = new ImageParam("发了一张图片", "", imgUrl, null);
//                Log.d("Nat: ImageParam.type", wxparam.getType());
                sendMessage(wxparam, srcFile, imgId);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        MLog.d(String.format("onActivityResult, requestCode = %d, resultCode=%d", requestCode, resultCode));

        try {
            if (requestCode == UIHelper.REQUEST_CODE_XIANGCE) {//相册
                if(data != null){
                    Uri uri = data.getData();
                    doUploadAndSend(uri);
                }
            }
            else if (requestCode == UIHelper.REQUEST_CODE_CAMERA) {//相机
                CameraSessionUtil cameraUtil = ServiceFactory.getService(CameraSessionUtil.class.getName());
//                CameraSessionUtil camera = this.materialController.getCameraUtil();
                File caFile = cameraUtil.getCameraResultFile(resultCode, data, this);
                doUploadAndSend(caFile);
            }
            else if (requestCode == MsgConstants.CODE_REQUEST_MATERIAL_LIB) {//素材库
//                Log.d("Nat:onActivityResult", "关闭素材库");
                Long materialId = data.getLongExtra("materialId", -1L);
                sendMessage(materialId);
            }
            else if (requestCode == MsgConstants.CODE_REQUEST_CYY) {
//                Log.d("Nat: onActivityResult", "关闭常用语");
                if (resultCode == Activity.RESULT_OK){
                    return;
                }
                Object obj = data.getExtras().get("content");
                if (obj instanceof  String) {
                    String msg = (String)obj;
                    sendMessage(new TextParam(msg));
                }
                else {
                    sendMessage((Long)obj);
                }
            }
        }
        catch (Throwable ex) {
            showHint(ex.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_add) { //添加素材
            toggleMaterial(view);
        }  else if (view.getId() == R.id.chat_face) {
            toggleEmotion(view);
        } else if (view.getId() == R.id.button_send) { //发送
            if(!ClickFilter.filter()){
                sendMessage(new TextParam(chatInput.getText()));

                materialFunctionController.setVisibility(View.GONE);
                materialFaceController.setVisibility(View.GONE);
                chatInput.setFaceButtonSelected(false);
            }
        } else if (view.getId() == R.id.messagesView) {
            AppHelper.hideSoftInput(this);
            materialFaceController.setVisibility(View.GONE);
        } else if (view.getId() == R.id.button_voice){
            showHint("语音功能尚未开放");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.chat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        hideMaterialViews();

        if (!backUpFragment()) {
            this.finish();//结束当前activity
        }
        else
            super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 消息发送成功后的处理逻辑
     * @param result
     */
    private void doAfterSendSuccess(EmbMsg result) {
        chatInput.clearText();
        if(result != null){
            msgService.saveNewMsg(result);//存储到本地数据库
            messageFragment.showSendMsgAgain(result);
            sessionService.changeSessionBySendMessage(result);
        }
    }

    /**
     * 发送一个资源库中的消息
     * @param materialId 可以是消息格式或服务器端素材库Id
     */
    protected void sendMessage(Long materialId) {
        NetProcessor.ComnProcessor processor = new NetProcessor.ComnProcessor<EmbMsg>(){
            @Override
            protected void processOperResult(EmbMsg result){
                doAfterSendSuccess(result);
            }
        };
        msgService.sendMessage(messageFragment.getSessionId(), materialId, this, processor);
    }

    /**
     * 发送一个本地文本消息
     * @param wxParam
     */
    protected void sendMessage(TextParam wxParam) {
        NetProcessor.ComnProcessor processor = new NetProcessor.ComnProcessor<EmbMsg>(){
            @Override
            protected void processOperResult(EmbMsg result){
                doAfterSendSuccess(result);
            }
        };
        msgService.sendMessage(messageFragment.getSessionId(), wxParam, this, processor);
    }

    /**
     * 发送一个本地图像消息
     * @param wxParam
     * @param msgAttach
     */
    protected void sendMessage(ImageParam wxParam, final File msgAttach, Long imageId) {
        final String picUrl = wxParam.getPicurl();

        NetProcessor.ComnProcessor processor = new NetProcessor.ComnProcessor<EmbMsg>(){
            @Override
            protected void processOperResult(EmbMsg result){
                //先把原始文件纳入缓存
                if (msgAttach != null) {
                    String localName = FileNetDao.genLocalFileName(picUrl);
                    File newFile = new File(MsgConstants.getMsgImgFao().getFileDir(), localName);
                    if (!msgAttach.renameTo(newFile)) {
                        FileUtils.copyFile(msgAttach, newFile);
                        msgAttach.delete();
                    }
                }
                doAfterSendSuccess(result);
            }
        };
        //发送图片
        String value = String.valueOf(MsgBridgeUtil.ConverterToMsgParameterForImage(SharedPreferencesHelper.getPushClientId(),
                Long.valueOf(MfhLoginService.get().getCurrentGuId()), messageFragment.getSessionId(), wxParam));

        AjaxParams params = new AjaxParams();
        params.put(Constants.PARAM_KEY_JSON_STR, value);
        params.put(Constants.PARAM_KEY_PIC_URL, picUrl);
        msgService.sendImageMsg(params, processor);
    }

    /**
     * 刷新消息列表
     * */
    private void refresh(){
        //解决列表加载时首次为空时无法上滑加载问题。
        if(lastupdate != null){
            Date time = new Date();
            time.setTime((lastupdate - 1) * 1000);//前一秒
            msgService.queryFromNet(sessionId, new SimpleDateFormat(TimeCursor.INNER_DATAFORMAT).format(time));
            return;
        }else{
            msgService.queryFromNet(sessionId);
        }
        sessionService.resetUnReadMsgCount(sessionId);// 把此会话的未读数置为0
    }

    /**
     * 素材
     * */
    private void toggleMaterial(View view){
        DeviceUtils.hideSoftInput(ChatActivity.this, view);
//            //判断键盘是否打开，如果打开则关闭键盘
//            //若返回true，则表示输入法打开
//            if (inputMethodManager.isActive()) {
//                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
//            }

        messageFragment.scrollToLast();
        //隐藏表情面板
        materialFaceController.setVisibility(View.GONE);
        chatInput.setFaceButtonSelected(false);

        //显示/隐藏素材面板
        if (materialFunctionController.getVisibility() == View.GONE){
            materialFunctionController.setVisibility(View.VISIBLE);
            materialFunctionController.setPage(0);
        }else {
            materialFunctionController.setVisibility(View.GONE);
        }
    }

    /**
     * Emotion
     * */
    private void toggleEmotion(View view){
        //隐藏键盘
        //            AppHelper.hideSoftInput(ChatActivity.this);
        if(getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN){
            if (getCurrentFocus() != null){
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }


        messageFragment.scrollToLast();
        //隐藏素材面板
        materialFunctionController.setVisibility(View.GONE);
        chatInput.getEditText().requestFocus();

        //显示/隐藏表情面板
        if (materialFaceController.getVisibility() == View.GONE){
            materialFaceController.setVisibility(View.VISIBLE);
            materialFaceController.setPage(0);
            chatInput.setFaceButtonSelected(true);
        }else {
            materialFaceController.setVisibility(View.GONE);
            chatInput.setFaceButtonSelected(false);
        }
    }


    /**
     * 隐藏素材视图
     * */
    private void hideMaterialViews(){
//隐藏键盘
//            AppHelper.hideSoftInput(this);
        if(getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN){
            if (getCurrentFocus() != null){
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }


        messageFragment.scrollToLast();
        //隐藏素材面板
        materialFunctionController.setVisibility(View.GONE);
        materialFaceController.setVisibility(View.GONE);
        chatInput.setFaceButtonSelected(false);
    }

    /**
     * 显示微信菜单
     * */
    private void showWXPopupMenu(View parentView, List<WXMenuData> menus){
        int parentViewMeasuredWidth = parentView.getMeasuredWidth();
        int parentViewMeasuredHeight = parentView.getMeasuredHeight();

        View contentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup_wx_listview, null);
//            contentView.setLayoutParams(new ViewGroup.LayoutParams(view.getMeasuredWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));
        //强制绘制contentView，并且马上初始化contentView的尺寸，解决PopupWindow宽/高为空问题。
//        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        //        contentView.measure(w, h);
//        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

//        LinearLayout layout = new LinearLayout(this);
//        layout.setBackgroundColor(Color.GRAY);
//        final PopupWindow popupWindow = new PopupWindow(contentView, parentView.getMeasuredWidth(), 400);
//        //            popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.setContentView(contentView);
//        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final PopupWindow popupWindow = new PopupWindow(contentView, parentViewMeasuredWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.update();

        popupWindow.setFocusable(true);// 使其获取焦点
        popupWindow.setOutsideTouchable(true);// 设置允许在外点击消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景

        ListView menuList = (ListView) contentView.findViewById(R.id.listview_popup_wx);
        // 加载数据
        final WXMenuListAdapter menuAdapter = new WXMenuListAdapter(this, menus);
        menuList.setAdapter(menuAdapter);
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                //跳转网页
                WXMenuData wxMenuData = (WXMenuData) menuAdapter.getItem(position);
                if(wxMenuData.getUrl() != null){
                    Intent intent = new Intent(ChatActivity.this, NativeWebViewActivity.class);
                    intent.putExtra("redirectUrl", wxMenuData.getUrl());
                    intent.putExtra("syncCookie", true);
                    startActivity(intent);
                }

                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });

        View bottomBarV = findViewById(R.id.bottom_bar);//以bottomBar的上分割线为基准，防止显示位置出现偏差
        int[] location = new int[2];
        parentView.getLocationOnScreen(location);
//        popupWindow.getContentView().setLayoutParams(new ViewGroup.LayoutParams(parentViewMeasuredWidth,
// ViewGroup.LayoutParams.WRAP_CONTENT));
        popupWindow.getContentView().measure(parentViewMeasuredWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.showAtLocation(parentView, Gravity.NO_GRAVITY, location[0], location[1]-parentViewMeasuredHeight);
        popupWindow.showAtLocation(parentView, Gravity.START|Gravity.BOTTOM, location[0], bottomBarV.getMeasuredHeight());//
    }


    /**
     * 获取微信菜单
     * */
    private void requestWXMenu(){
        WXMenu wxMenu = new WXMenu();
        //TODO,以下为测试数据,等待集成后台提供接口,根据当前对话类型决定是否显示菜单。
        wxMenu.initWithSimulateData();

        //设置菜单
        chatMenuWX.setMajorMenus(wxMenu.getMenuList());
    }

}
