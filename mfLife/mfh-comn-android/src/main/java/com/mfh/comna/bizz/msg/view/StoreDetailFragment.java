package com.mfh.comna.bizz.msg.view;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfh.comna.bizz.BizApplication;
import com.mfh.comna.api.ui.CustomMapActivity;
import com.mfh.comna.api.utils.BitmapUtils;
import com.mfh.comna.api.utils.NetWorkUtil;
import com.mfh.comna.api.web.NativeWebViewActivity;
import com.mfh.comna.api.widgets.SettingsItem;
import com.mfh.comna.api.widgets.SettingsItemData;
import com.mfh.comna.bizz.msg.StoreDetail;
import com.mfh.comn.net.data.IResponseData;
import com.mfh.comn.net.data.RspBean;
import com.mfh.comna.R;
import com.mfh.comna.comn.database.dao.NetCallBack;
import com.mfh.comna.comn.database.dao.NetProcessor;
import com.mfh.comna.utils.DialogUtil;
import com.mfh.comna.view.BaseFragment;
import com.mfh.comna.widget.LoadingImageView;


/**
 * 小伙伴·商家·详情
 * Created by ZZN on 2015-05-27.
 */
public class StoreDetailFragment extends BaseFragment {
    private ImageView ivHeader, ivAuth, ivHeart;
    private TextView tvName;
    private SettingsItem[] groupItems;
    private Button btnChat;

    private LoadingImageView loadingImageView;
    private StoreDetail storeDetail;


    //Vip Icons
    private int[] heartIds = new int[]{
            R.drawable.red_heart_01, R.drawable.red_heart_02, R.drawable.red_heart_03,
            R.drawable.red_heart_04, R.drawable.red_heart_05
    };

    public StoreDetailFragment() {
        super();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_store_detail;
    }

    @Override
    protected void createViewInner(View rootView, ViewGroup container, Bundle savedInstanceState) {
        ivHeader = (ImageView) rootView.findViewById(R.id.iv_header);
        ivAuth = (ImageView) rootView.findViewById(R.id.iv_auth);
        ivHeart = (ImageView) rootView.findViewById(R.id.iv_heart);
        tvName = (TextView) rootView.findViewById(R.id.tv_name);
        btnChat = (Button) rootView.findViewById(R.id.button_chat);
        btnChat.setText(R.string.button_start_chat);
        btnChat.setOnClickListener(myOnClickListener);

        groupItems = new SettingsItem[5];
        groupItems[0] = (SettingsItem)  rootView.findViewById(R.id.item_1_0);
        groupItems[0].init(new SettingsItemData(0, getString(R.string.label_store_name), ""));
        groupItems[0].setButtonType(SettingsItem.ThemeType.THEME_TEXT_TEXT, SettingsItem.SeperateLineType.SEPERATE_LINE_MULTI_TOP, SettingsItem.DetailTextTheme.LEFT_SINGLE_LINE);
        groupItems[0].setOnClickListener(myOnClickListener);
        groupItems[1] = (SettingsItem)  rootView.findViewById(R.id.item_1_1);
        groupItems[1].init(new SettingsItemData(0, getString(R.string.label_service_telephone), ""));
        groupItems[1].setButtonType(SettingsItem.ThemeType.THEME_TEXT_TEXT_ARROW, SettingsItem.SeperateLineType.SEPERATE_LINE_MULTI_CENTER, SettingsItem.DetailTextTheme.LEFT_SINGLE_LINE);
        groupItems[1].setOnClickListener(myOnClickListener);
        groupItems[2] = (SettingsItem)  rootView.findViewById(R.id.item_1_2);
        groupItems[2].init(new SettingsItemData(0, getString(R.string.label_service_description), ""));
        groupItems[2].setButtonType(SettingsItem.ThemeType.THEME_TEXT_TEXT, SettingsItem.SeperateLineType.SEPERATE_LINE_MULTI_BOTTOM, SettingsItem.DetailTextTheme.LEFT_MULTI_LINE);
        groupItems[2].setOnClickListener(myOnClickListener);
        groupItems[3] = (SettingsItem)  rootView.findViewById(R.id.item_2_0);
        groupItems[3].init(new SettingsItemData(0, getString(R.string.label_address), ""));
        groupItems[3].setButtonType(SettingsItem.ThemeType.THEME_TEXT_TEXT_ARROW, SettingsItem.SeperateLineType.SEPERATE_LINE_MULTI_TOP, SettingsItem.DetailTextTheme.LEFT_SINGLE_LINE);
        groupItems[3].setOnClickListener(myOnClickListener);
        groupItems[4] = (SettingsItem)  rootView.findViewById(R.id.item_2_1);
        groupItems[4].init(new SettingsItemData(0, getString(R.string.label_shop_online), ""));
        groupItems[4].setButtonType(SettingsItem.ThemeType.THEME_TEXT_TEXT_ARROW, SettingsItem.SeperateLineType.SEPERATE_LINE_MULTI_BOTTOM, SettingsItem.DetailTextTheme.LEFT_SINGLE_LINE);
        groupItems[4].setOnClickListener(myOnClickListener);

        loadingImageView = (LoadingImageView) rootView.findViewById(R.id.loadingImageView);
        loadingImageView.setBackgroundResource(com.mfh.comna.R.drawable.loading_anim);
    }

    private View.OnClickListener myOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.button_chat){
                //TODO,跳转至对话页面
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
            else if(view.getId() == R.id.item_1_1){
                //TODO 客服电话
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "4008666671"));
                startActivity(intent);
            }
            else if(view.getId() == R.id.item_2_0){
                //TODO,跳转至地理位置页面
                CustomMapActivity.actionStart(getActivity(), storeDetail.getNickname(), "31.238608", "121.501654", storeDetail.getName(), storeDetail.getDescription());
            }else if(view.getId() == R.id.item_2_1){
                //TODO,网上店铺
                if(storeDetail != null){
                    NativeWebViewActivity.actionStart(getActivity(), storeDetail.getUrl());
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        refresh();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public ProgressDialog onPreExecute(int taskKind) {
        /*if (taskKind == 1) {
            Integer myCount = sessionDao.getMyCount(ls.getLoginName());
            sessionService.queryFromNet();
            if (myCount != null && myCount <= 0) {
                return super.onPreExecute(taskKind);//首次加载，时间比较长，故显示等待框。
            }
            else
                return null;
        }
        else*/
            return null;// super.onPreExecute(taskKind);//屏蔽等待框
    }

    @Override
    public void onPostExecute(int taskKind, Object result, Object... params) {

    }

    private void refresh(){
        Bitmap bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.chat_tmp_user_head);
        if (bmp != null){
            ivHeader.setImageBitmap(BitmapUtils.toRoundBitmap(bmp));
        }


        loadTestData();

        if(NetWorkUtil.isConnect(getContext())){
            loadingImageView.toggle(true);

            //TODO
//        params.put(PARAM_KEY_JSESSIONID, sessionId);
//        fh.post(URL_MY_PROFILE, params, responseCallback);

            loadingImageView.toggle(false);
        }else{
            DialogUtil.showHint(getString(R.string.toast_network_error));
        }
    }

    //TODO,请求详细数据
    private void loadTestData(){
        //显示模拟数据
        StoreDetail data = new StoreDetail();
        data.setNickname("满分家园");
        data.setName("苏州春晨网络科技有限公司");
        data.setTelphone("400 8866 671");
        data.setDescription("洗衣代收/鲜奶预定/生鲜代存/水果代买/海鲜待存/快递代收");
        data.setAddress("星湖街328号");
        data.setStarLevel("5");
        data.setUrl("");
        onStoreDataChanged(data);
//        String sessionId = SharedPreferencesUtil.get(ComnApplication.getAppContext(),
//                AppConfig.PREF_NAME_LOGIN, AppConfig.PREF_KEY_USER_LAST_SESSIONID, null);
    }

    //回调
    private NetCallBack.NetTaskCallBack responseCallback = new NetCallBack.NetTaskCallBack<StoreDetail,
            NetProcessor.Processor<StoreDetail>>(
            new NetProcessor.Processor<StoreDetail>() {
                @Override
                public void processResult(IResponseData rspData) {
                    RspBean<StoreDetail> retValue = (RspBean<StoreDetail>) rspData;
                    StoreDetail data = retValue.getValue();

                    onStoreDataChanged(data);

                    loadingImageView.toggle(false);
                }

                @Override
                protected void processFailure(Throwable t, String errMsg) {
                    super.processFailure(t, errMsg);
                    loadingImageView.toggle(false);
                }
            }
            , StoreDetail.class
            , BizApplication.getAppContext())
    {
    };

    private void onStoreDataChanged(StoreDetail data){
        if(data != null){
            tvName.setText(data.getNickname());
            ivAuth.setVisibility(View.VISIBLE);
            showHeart(Integer.valueOf(data.getStarLevel()));
            groupItems[0].setDetailText(data.getName());
            groupItems[1].setDetailText(data.getTelphone());
            groupItems[2].setDetailText(data.getDescription());
            groupItems[3].setDetailText(data.getAddress());
            groupItems[4].setDetailText("");

            storeDetail = data;
        }
    }

    private void showHeart(int number){
        if(number <= 0){
            ivHeart.setVisibility(View.GONE);
            return;
        }

        if(number >= heartIds.length){
            number = heartIds.length - 1;
        }

        ivHeart.setImageResource(heartIds[number]);
    }

}
