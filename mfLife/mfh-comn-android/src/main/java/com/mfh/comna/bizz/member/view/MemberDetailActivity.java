package com.mfh.comna.bizz.member.view;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mfh.comna.api.helper.AppHelper;
import com.mfh.comn.code.impl.CodeService;
import com.mfh.comna.R;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.utils.DensityUtil;
import com.mfh.comna.view.BaseComnActivity;
import com.mfh.comna.view.img.FineImgView;
import com.mfh.comna.view.widget.SimpleVerticalField;
import com.mfh.comna.bizz.member.MemberConstants;
import com.mfh.comna.bizz.member.entity.Human;
import com.mfh.comna.bizz.member.logic.MemberService;

/**
 * Created by Shicy on 14-4-16.
 */
public class MemberDetailActivity extends BaseComnActivity {
    private MemberService memService;

    static {
        MemberConstants.initCode();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_member_detail;
    }

    @Override
    protected void initActionBar(ActionBar actionBar) {
        super.initActionBar(actionBar);
        actionBar.setIcon(com.mfh.comna.R.drawable.white_logo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        memService = ServiceFactory.getService(MemberService.class);
        super.onCreate(savedInstanceState);

        Long humanId = this.getIntent().getLongExtra("humanId", -1L);
        final Human human = memService.getDao().getEntityById(humanId);
        final MemberDetailActivity that = this;
        if (human != null){
            ((TextView)findViewById(R.id.member_detail_name)).setText(human.getName());
            ((TextView)findViewById(R.id.member_detail_addr)).setText(human.getAddress() == null ? human.getSignname() : human.getAddress());

            FineImgView iv = (FineImgView)findViewById(R.id.member_round_head);
            iv.setFao(FineImgView.getHeadImgFao());
            iv.setNeedSample(true);
            iv.setMaxWidth(DensityUtil.dip2px(this, 80));
            iv.setSrc(human.getHeadimage());//"f984264aa7c89b48fc2d08a64c21bd08.jpg"

            SimpleVerticalField ht = (SimpleVerticalField)findViewById(R.id.member_human_type);
            if (human.getSftype() != null){
                String sfName = CodeService.getCodeService().getValue("humanType", human.getSftype());
                if (ht != null)
                    ht.setValue(sfName);
            }
           /* SimpleVerticalField btnField = (SimpleVerticalField)findViewById(R.id.member_guid);
            btnField.setValue(human.getGuid());*/
            SimpleVerticalField  btnField = (SimpleVerticalField)findViewById(R.id.member_tel_btn);
            btnField.setValue(human.getMobile());
            //保持上面四行代码的顺序，为了让下面的单击事件监听手机号码项/lxy
            btnField.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppHelper.callTel(that, human.getMobile());
                }
            });

            findViewById(R.id.member_sms_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppHelper.startSMSMessage(that, human.getMobile(), "你好");
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.member_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        /*if (item.getItemId() == R.id.action_modify) {
            startActivity(new Intent(this, MemberEditActivity.class));
        }*/
        return super.onMenuItemSelected(featureId, item);
    }


}
